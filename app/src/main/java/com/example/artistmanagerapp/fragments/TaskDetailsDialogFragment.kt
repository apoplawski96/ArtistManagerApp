package com.example.artistmanagerapp.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.DialogFragment
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
import com.example.artistmanagerapp.activities.TaskListActivity
import com.example.artistmanagerapp.adapters.CommentsListAdapter
import com.example.artistmanagerapp.adapters.UsersListAdapter
import com.example.artistmanagerapp.firebase.CommentsHelper
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
import java.util.*
import kotlin.collections.ArrayList

class TaskDetailsDialogFragment : DialogFragment(), UsersListListener, TaskUpdater, DatePickerDialog.OnDateSetListener, CommentsHelper.CommentsUpdater {

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

    // Views
    var taskTitleEditText : EditText? = null
    var usersListRecyclerView : RecyclerView? = null
    var commentsListRecyclerView : RecyclerView? = null
    var addMembersWrapper : LinearLayout? = null
    var setDueDateWrapper : LinearLayout? = null
    var toolbar : AppBarLayout? = null

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

    // Data bundle from activity
    var taskTitle : String? = null
    var taskId : String? = null

    // Firebase
    var currentArtistPageId : String? = null
    val db = FirebaseFirestore.getInstance()
    var artistPagesCollectionPath : CollectionReference? = null
    var pathToCommentsCollection : CollectionReference? = null

    // Others
    var date : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_details_dialog, container, false)

        taskInstance = arguments?.getSerializable(Constants.BUNDLE_TASK_INSTANCE) as Task?
        userInstance = arguments?.getSerializable(Constants.BUNDLE_USER_INSTANCE) as User?
        artistPageInstance = arguments?.getSerializable(Constants.BUNDLE_ARTIST_PAGE_INSTANCE) as ArtistPage?

        // Paths initialization
        artistPagesCollectionPath = db.collection(FirebaseConstants.ARTIST_PAGES_COLLECTION_NAME)
        pathToCommentsCollection = artistPagesCollectionPath?.document(artistPageInstance?.artistPageId.toString())?.collection("tasks")?.document(taskInstance?.taskId.toString())?.collection("comments")

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

        // Views - Toolbar
        toolbarMidText = rootView.findViewById(R.id.toolbar_task_details_text)
        toolbarBackButton = rootView.findViewById(R.id.toolbar_back_button_task_details)
        toolbarDismissButton = rootView.findViewById(R.id.toolbar_dismiss_button)
        toolbarConfirmButton = rootView.findViewById(R.id.toolbar_confirm_button)
        toolbarProgressBar = rootView.findViewById(R.id.task_details_toolbar_progress_bar)

        // Getting data from TaskListActivity
        taskTitle = arguments?.getString("TASK_TITLE").toString()
        taskId = arguments?.getString("TASK_ID").toString()

        // Getting users from Firebase and setting up the RecyclerView
        populateUsersListWithFakeData(usersList)
        //usersHelper?.parseUsers(currentArtistPage, this)
        usersListRecyclerView?.layoutManager = LinearLayoutManager(TaskListActivity(), OrientationHelper.HORIZONTAL, false)
        adapter = UsersListAdapter(this, context, usersList) {user: User ->  userItemClicked (user)}
        usersListRecyclerView?.adapter = adapter

        // Comments list setup
        CommentsHelper.parseComments(pathToCommentsCollection, this)

        populateCommentsListWithFakeData(commentsList)
        commentsListRecyclerView?.layoutManager = LinearLayoutManager(TaskListActivity(), OrientationHelper.VERTICAL, false)
        commentsListAdapter = CommentsListAdapter(commentsList)
        commentsListRecyclerView?.adapter = commentsListAdapter

        // Setting up data to corresponding views
        taskTitleEditText?.setText(taskTitle)

        initializeUI()

        addMembersWrapper?.setOnClickListener {
            when (isMembersListVisible){
                true -> {
                    hideMembersList()
                    disableActionToolbar()
                }
                false -> {
                    showMembersList()
                }
            }
        }

        setDueDateWrapper?.setOnClickListener {
            when (isCalendarVisible){
                true -> {

                    disableActionToolbar()
                }
                false -> {
                }
            }
        }

        return rootView
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

    override fun onDateSet(p0: DatePicker?, day: Int, month: Int, year: Int) {
        date = "$day/${month+1}/$year"
        Log.d("CALENDAR", date)
        activateActionToolbar(Constants.CALENDAR_ON_DATE_CHANGED)
    }

    fun initializeUI() {
        // Setting up boolean controllers
        isCalendarVisible = false
        isMembersListVisible = false
        isTaskTitleEditTextEnabled = false

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

    override fun updateList(usersOutput: ArrayList<User>) {

    }

    fun userItemClicked(userItem : User){

    }

    override fun onTaskLongClicked(itemView: View, task : Task) {

    }

    fun populateUsersListWithFakeData(usersList : ArrayList<User>){
        usersList.add(User ("123", "hui", "hui2"))
        usersList.add(User ("124", "hui", "mui2"))
        usersList.add(User ("125", "hui", "dui2"))
        usersList.add(User ("126", "hui", "wui2"))
    }

    fun populateCommentsListWithFakeData(commentsList : ArrayList<Comment>){
        commentsList.add(Comment("Content1 ", "Arczi Poplawko", "Aug 7, 2019", "Arczi Poplawko"))
        commentsList.add(Comment("Content2 asdasdasd ", "Arcziasdasd Poplaasdasdwko", "Aug 9, 2013", "Nie arczi lecz ktos inny"))
    }

    // Whole ActionToolbar setup here!
    override fun activateActionToolbar(option : String){
        val c = Constants

        // Standard set of UI changes
        isToolbarActivated = true
        toolbarBackButton?.visibility = View.GONE
        toolbarConfirmButton?.visibility = View.VISIBLE
        toolbarDismissButton?.visibility = View.VISIBLE


        when (option){
            c.CALENDAR_ON_DATE_CHANGED -> {
                toolbarMidText?.text = "Active"
            }
            c.TASKS_ON_LONG_CLICKED -> {
                toolbarMidText?.text = "Active"
            }
        }



        // Confirm button onclick here!
        toolbarConfirmButton?.setOnClickListener {
            Toast.makeText(activity, "Confirmed", Toast.LENGTH_SHORT).show()

            if (isCalendarVisible){
                toolbarConfirmButton?.visibility = View.GONE
                toolbarProgressBar?.visibility = View.VISIBLE
                TaskHelper.setTaskDueDate(taskId, date, currentArtistPageId, this)
            }
            else if (isMembersListVisible){

            }

        }

        // Dismiss button onclick here!
        toolbarDismissButton?.setOnClickListener {
            Toast.makeText(activity, "Dismissed", Toast.LENGTH_SHORT).show()

            if (isMembersListVisible) hideMembersList()

            disableActionToolbar()
        }

    }

    fun disableActionToolbar(){
        isToolbarActivated = false
        toolbarBackButton?.visibility = View.VISIBLE
        toolbarConfirmButton?.visibility = View.GONE
        toolbarDismissButton?.visibility = View.GONE
        toolbarMidText?.text = "Disabled"
    }

    override fun hideProgressBar() {
        toolbarProgressBar?.visibility = View.GONE
    }

    override fun onTaskDetailChanged() {
        hideProgressBar()
        disableActionToolbar()
    }

    override fun onCommentAdded(option: CommentsHelper.Option?) {
        CommentsHelper.parseComments(pathToCommentsCollection, this)
    }

    override fun onCommentsParsed(option: CommentsHelper.Option?, commentsList: ArrayList<Comment>) {
        commentsListAdapter?.updateItems(commentsList)
    }

    override fun updateTasks(tasksOutput: ArrayList<Task>) {}
    override fun triggerUpdate() {}
    override fun showProgressBar() {}
    override fun onError(errorLog : String?) {}
    override fun hideAddTaskDialog() {}
    override fun onTaskDeleted() {}
    override fun onTasksListEmpty() {}
}