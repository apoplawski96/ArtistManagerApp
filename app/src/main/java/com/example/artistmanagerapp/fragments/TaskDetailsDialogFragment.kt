package com.example.artistmanagerapp.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.view.menu.MenuView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.activities.CreateUserProfileActivity
import com.example.artistmanagerapp.activities.EventsManagerActivity
import com.example.artistmanagerapp.activities.TaskListActivity
import com.example.artistmanagerapp.adapters.CommentsListAdapter
import com.example.artistmanagerapp.adapters.UsersListAdapter
import com.example.artistmanagerapp.firebase.CommentsHelper
import com.example.artistmanagerapp.interfaces.IOnBackPressed
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.interfaces.UsersListListener
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.Comment
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_task_details_dialog.*
import kotlinx.android.synthetic.main.item_user_avatar.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TaskDetailsDialogFragment : DialogFragment(), UsersListListener, TaskUpdater, DatePickerDialog.OnDateSetListener, CommentsHelper.CommentsUpdater, IOnBackPressed{

    // Others
    val ACT_TAG = "TaskDetailsFragment"

    // Bundle objects
    var userInstance : User? = null
    var artistPageInstance : ArtistPage? = null
    var taskInstance : Task? = null

    // Utils objects
    var usersHelper : UsersHelper? = null

    // Adapters
    var adapter : UsersListAdapter? = null
    var commentsListAdapter : CommentsListAdapter? = null

    // Collections
    var usersList : ArrayList<User> = ArrayList()
    var commentsList : ArrayList<Comment> = ArrayList()
    var usersAssignedTemp : ArrayList<User?> = ArrayList()
    var userItemViewsSelected : ArrayList<View?> = ArrayList()

    // Views
    var taskTitleEditText : EditText? = null
    var usersListRecyclerView : RecyclerView? = null
    var commentsListRecyclerView : RecyclerView? = null
    var addMembersWrapper : LinearLayout? = null
    var setDueDateWrapper : LinearLayout? = null
    var toolbar : AppBarLayout? = null
    var taskDescription : EditText? = null
    var addCommentField : EditText? = null
    var progressBarDate : ProgressBar? = null
    var setDueDateTextView : TextView? = null
    var dueDateDisplay : TextView? = null
    var progressBarCommentsList : ProgressBar? = null
    var assignMembersTv : TextView? = null
    var assignedMembersDisplay : TextView? = null

    // Views - Toolbar
    var toolbarMidText : TextView? = null
    var toolbarBackButton : ImageView? = null
    var toolbarDismissButton : ImageView? = null
    var toolbarConfirmButton : ImageView? = null
    var toolbarProgressBar : ProgressBar? = null

    // Boolean UI controllers
    var isCalendarVisible : Boolean = false
    var isMembersListVisible : Boolean = false
    var isTaskTitleEditTextEnabled : Boolean = false
    var isToolbarActivated : Boolean = false

    // Firebase
    var currentArtistPageId : String? = null
    val db = FirebaseFirestore.getInstance()
    var artistPagesCollectionPath : CollectionReference? = null
    var pathToCommentsCollection : CollectionReference? = null
    var tasksCollectionPath : CollectionReference? = null

    // Others
    var date : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_details_dialog, container, false)

        // Getting bundled data
        taskInstance = arguments?.getSerializable(Constants.BUNDLE_TASK_INSTANCE) as Task?
        userInstance = arguments?.getSerializable(Constants.BUNDLE_USER_INSTANCE) as User?
        artistPageInstance = arguments?.getSerializable(Constants.BUNDLE_ARTIST_PAGE_INSTANCE) as ArtistPage?

        // Paths initialization
        artistPagesCollectionPath = db.collection(FirebaseConstants.ARTIST_PAGES_COLLECTION_NAME)
        tasksCollectionPath = artistPagesCollectionPath?.document(artistPageInstance?.artistPageId.toString())?.collection("tasks")
        pathToCommentsCollection = artistPagesCollectionPath?.document(artistPageInstance?.artistPageId.toString())?.collection("tasks")?.document(taskInstance?.taskId.toString())?.collection("comments")

        // Standard log set
        Log.d(ACT_TAG, "TaskDetailsFragment entered")
        Log.d(ACT_TAG, artistPageInstance.toString())
        Log.d(ACT_TAG, userInstance.toString())
        Log.d(ACT_TAG, taskInstance.toString())

        // Utils objects
        usersHelper = UsersHelper

        // Views
        taskTitleEditText = rootView.findViewById(R.id.task_details_title)
        usersListRecyclerView = rootView.findViewById(R.id.users_list_recycler_view)
        commentsListRecyclerView = rootView.findViewById(R.id.comments_recycler_view)
        addMembersWrapper = rootView.findViewById(R.id.add_members_wrapper)
        setDueDateWrapper = rootView.findViewById(R.id.set_due_date_wrapper)
        taskDescription = rootView.findViewById(R.id.task_description)
        addCommentField = rootView.findViewById(R.id.add_comment_field)
        progressBarDate = rootView.findViewById(R.id.progress_bar_date)
        setDueDateTextView = rootView.findViewById(R.id.set_due_date_tv)
        dueDateDisplay = rootView.findViewById(R.id.due_date_display)
        progressBarCommentsList = rootView.findViewById(R.id.progress_bar_comments_list)
        assignMembersTv = rootView.findViewById(R.id.assign_members_tv)
        assignedMembersDisplay = rootView.findViewById(R.id.assigned_members_display)

        // Views - Toolbar
        toolbarMidText = rootView.findViewById(R.id.toolbar_task_details_text)
        toolbarBackButton = rootView.findViewById(R.id.toolbar_back_button_task_details)
        toolbarDismissButton = rootView.findViewById(R.id.toolbar_dismiss_button)
        toolbarConfirmButton = rootView.findViewById(R.id.toolbar_confirm_button)
        toolbarProgressBar = rootView.findViewById(R.id.toolbar_progress_bar)

        // Users list setup
        populateUsersListWithFakeData(usersList)
        //usersHelper?.parseUsers(currentArtistPage, this)
        usersListRecyclerView?.layoutManager = LinearLayoutManager(TaskListActivity(), OrientationHelper.HORIZONTAL, false)
        adapter = UsersListAdapter(this, context, usersList) {user: User, itemView : View ->  userItemClicked (user, itemView)}
        usersListRecyclerView?.adapter = adapter

        // Comments list setup
        CommentsHelper.parseComments(pathToCommentsCollection, this)
        progressBarCommentsList?.visibility = View.VISIBLE
        commentsListRecyclerView?.layoutManager = LinearLayoutManager(TaskListActivity(), OrientationHelper.VERTICAL, false)
        commentsListAdapter = CommentsListAdapter(commentsList)
        commentsListRecyclerView?.adapter = commentsListAdapter

        // Setting up data to corresponding views
        taskTitleEditText?.setText(taskInstance?.title)
        if (taskInstance?.description != "null"){
            taskDescription?.setText(taskInstance?.description)
        }

        // UI initialization
        initializeUI()

        // OnClicks handled
        addMembersWrapper?.setOnClickListener {
            when (isMembersListVisible){
                true -> hideMembersList()
                false -> showMembersList()
            }
        }

        setDueDateTextView?.setOnClickListener {
            showDatePickerDialog()
        }

        dueDateDisplay?.setOnClickListener{
            showDatePickerDialog()
        }

        assignedMembersDisplay?.setOnClickListener{
            when (isMembersListVisible){
                true -> hideMembersList()
                false -> showMembersList()
            }
        }

        taskTitleEditText?.setOnFocusChangeListener { view, b ->
            enableActionToolbar(Constants.TASK_TITLE_MODIFIED, null)
            Utils.enableEditText(taskTitleEditText)
        }

        taskDescription?.setOnFocusChangeListener { view, b ->
            enableActionToolbar(Constants.DESCRIPTION_ADDED, null)
            view.isEnabled = true
        }

        addCommentField?.setOnFocusChangeListener { view, b ->
            enableActionToolbar(Constants.COMMENT_ADDED, null)
        }

        toolbarBackButton?.setOnClickListener{
            Toast.makeText(activity, "hui", Toast.LENGTH_SHORT).show()
        }

        return rootView
    }

    override fun onBackPressed(tasksUpdater : TaskUpdater): Boolean {
        tasksUpdater.triggerUpdate()
        return false
    }

    fun putDataToBundle(intent : Intent?){
        intent?.putExtra (Constants.PAGE_ID_BUNDLE, artistPageInstance?.artistPageId)
        intent?.putExtra (Constants.ARTIST_NAME_BUNDLE, artistPageInstance?.artistName)
        //intent?.putExtra (Constants.EPK_SHARE_CODE_BUNDLE, e)
        intent?.putExtra (Constants.BUNDLE_USER_INSTANCE, userInstance)
        intent?.putExtra (Constants.BUNDLE_ARTIST_PAGE_INSTANCE, artistPageInstance)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    companion object {
        @JvmStatic
        fun newInstance(task : Task?, user : User?, artistPage : ArtistPage?) : TaskDetailsDialogFragment {
            val fragment = TaskDetailsDialogFragment()
            val bundle = Bundle().apply{
                putSerializable(Constants.BUNDLE_ARTIST_PAGE_INSTANCE, artistPage)
                putSerializable(Constants.BUNDLE_USER_INSTANCE, user)
                putSerializable(Constants.BUNDLE_TASK_INSTANCE, task)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    fun showDatePickerDialog(){
        var datePickerDialog = DatePickerDialog(
            activity,
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Date setup
        date = "$dayOfMonth/${month+1}/$year"
        // UI changes
        progressBarDate?.visibility = View.VISIBLE
        setDueDateTextView?.visibility = View.GONE
        // Setting dueDate in database
        TaskHelper.setTaskDueDate(taskInstance?.taskId, date, artistPageInstance?.artistPageId, this)
    }

    fun initializeUI() {
        // Setting up boolean controllers
        dueDateDisplay?.visibility = View.GONE
        isCalendarVisible = false
        isMembersListVisible = false
        isTaskTitleEditTextEnabled = false
        toolbarMidText?.text = "Task Details"

        // Setting up views
        Utils.disableEditText(taskTitleEditText)
        usersListRecyclerView?.visibility = View.GONE
    }

    // ************ Show&Hide stuff ************

    fun showMembersList(){
        isMembersListVisible = true
        usersListRecyclerView?.visibility = View.VISIBLE
    }

    fun hideMembersList(){
        isMembersListVisible = false
        usersListRecyclerView?.visibility = View.GONE
    }


    // ************ Show&Hide stuff ************

    fun populateUsersListWithFakeData(usersList : ArrayList<User>){
        usersList.add(User ("123", "hui", "hui2"))
        usersList.add(User ("124", "hui", "mui2"))
        usersList.add(User ("125", "hui", "dui2"))
        usersList.add(User ("126", "hui", "wui2"))
    }

    // Whole ActionToolbar setup here!
    fun enableActionToolbar(option : String, data : Any?){
        val c = Constants

        // Standard set of UI changes
        isToolbarActivated = true
        toolbarBackButton?.visibility = View.GONE
        toolbarConfirmButton?.visibility = View.VISIBLE
        toolbarDismissButton?.visibility = View.VISIBLE

        when (option){
            c.TASK_TITLE_MODIFIED -> {
                toolbarMidText?.text = "Change Task Title"
                // TaskHelper.updateTitle()
            }
            c.DESCRIPTION_ADDED -> {
                toolbarMidText?.text = "Save Description"
                // TasksHelper.addDescription()
            }
            c.MEMBERS_ASSIGNED -> {
                var usersCount = data as Int
                toolbarMidText?.text = "$usersCount users selected"
                // TasksHelper.assignMembers()
            }
            c.COMMENT_ADDED -> {
                toolbarMidText?.text = "Add Comment"
                // CommentsHelper.addComment()
            }
        }

        // Confirm button onclick here!
        toolbarConfirmButton?.setOnClickListener {

            when (option){
                c.TASK_TITLE_MODIFIED -> {
                    // TaskHelper.updateTitle()
                }
                c.DESCRIPTION_ADDED -> {
                    val description = taskDescription?.text.toString()
                    TaskHelper.setTaskDescription(taskInstance?.taskId.toString(), tasksCollectionPath, description)
                    Toast.makeText(activity, "Description updated", Toast.LENGTH_SHORT).show()
                    taskDescription?.isEnabled = false
                    disableActionToolbar()
                }
                c.MEMBERS_ASSIGNED -> {
                    toolbarProgressBar?.visibility = View.VISIBLE
                    toolbarConfirmButton?.visibility = View.GONE
                    toolbarDismissButton?.visibility = View.GONE
                    TaskHelper.assignMembers(taskInstance?.taskId, usersAssignedTemp, artistPageInstance?.artistPageId, this)
                }
                c.COMMENT_ADDED -> {
                    addCommentField?.isEnabled = false
                    disableActionToolbar()
                    CommentsHelper.addComment(userInstance?.id, userInstance?.getDisplayName(), addCommentField?.text.toString(), Utils.getCurrentDate(), pathToCommentsCollection as CollectionReference, this, null)
                    addCommentField?.setText(null)
                }
            }

        }

        // Dismiss button onclick here!
        toolbarDismissButton?.setOnClickListener {
            disableActionToolbar()
        }

    }


    // Whole ActionToolbar setup here!
    override fun activateActionToolbar(option : String){ }

    fun disableActionToolbar(){
        isToolbarActivated = false
        toolbarBackButton?.visibility = View.VISIBLE
        toolbarConfirmButton?.visibility = View.GONE
        toolbarDismissButton?.visibility = View.GONE
        toolbarMidText?.text = "Task Details"
    }

    override fun hideProgressBar() {
        //toolbarProgressBar?.visibility = View.GONE
    }

    override fun onTaskDetailChanged(option : String?, data : Any?) {
        when (option){
            Constants.TASK_DUE_DATE_SET -> {
                Toast.makeText(activity, "Due date saved successfully", Toast.LENGTH_SHORT).show()
                progressBarDate?.visibility = View.GONE
                setDueDateTextView?.visibility = View.GONE
                dueDateDisplay?.text = date
                dueDateDisplay?.visibility = View.VISIBLE
            }
            Constants.MEMBERS_ASSIGNED -> {
                Toast.makeText(activity, "${usersAssignedTemp.size} assignees added", Toast.LENGTH_SHORT).show()
                hideMembersList()
                disableActionToolbar()
                toolbarProgressBar?.visibility = View.GONE
                assignMembersTv?.visibility = View.GONE
                isMembersListVisible = false
                assignedMembersDisplay?.text = "${usersAssignedTemp.size} members assigned"
                assignedMembersDisplay?.visibility = View.VISIBLE
            }
        }
    }

    override fun onCommentAdded(option: CommentsHelper.Option?) {
        progressBarCommentsList?.visibility = View.VISIBLE
        CommentsHelper.parseComments(pathToCommentsCollection, this)
    }

    override fun onCommentsParsed(option: CommentsHelper.Option?, commentsList: ArrayList<Comment>) {
        progressBarCommentsList?.visibility = View.GONE
        commentsListAdapter?.updateItems(commentsList)
    }

    fun userItemClicked(user : User?, itemView: View){

        if (!userItemViewsSelected.contains(itemView)){
            usersAssignedTemp.add(user)
            userItemViewsSelected.add(itemView)
            itemView.circle_avatar_background.setImageResource(R.color.warningRed)
        } else {
            userItemViewsSelected.remove(itemView)
            usersAssignedTemp.remove(user)
            itemView.circle_avatar_background.setImageResource(R.color.colorAccent)
        }


        if (usersAssignedTemp.isEmpty()) {
            disableActionToolbar()
        } else {
            enableActionToolbar(Constants.MEMBERS_ASSIGNED, usersAssignedTemp.size)
        }

    }

    override fun updateTasks(tasksOutput: ArrayList<Task>) {}
    override fun triggerUpdate() {}
    override fun showProgressBar() {}
    override fun onError(errorLog : String?) {}
    override fun hideAddTaskDialog() {}
    override fun onTaskDeleted() {}
    override fun onTasksListEmpty() {}
    override fun updateList(usersOutput: ArrayList<User>) {}
    override fun onTaskLongClicked(itemView: View, task : Task) {}
}