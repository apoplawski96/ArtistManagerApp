package com.example.artistmanagerapp.fragments

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
import com.example.artistmanagerapp.adapters.UsersListAdapter
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.interfaces.UsersListListener
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.TaskHelper
import com.example.artistmanagerapp.utils.UsersHelper
import com.example.artistmanagerapp.utils.Utils
import com.google.firebase.firestore.CollectionReference
import kotlinx.android.synthetic.main.fragment_task_details_dialog.*
import java.util.*
import kotlin.collections.ArrayList

class TaskDetailsDialogFragment : DialogFragment(), UsersListListener, TaskUpdater {

    // Utils objects
    var usersHelper : UsersHelper? = null

    // Adapters
    var adapter : UsersListAdapter? = null

    // Views
    var taskTitleEditText : EditText? = null
    var usersListRecyclerView : RecyclerView? = null
    var addMembersWrapper : LinearLayout? = null
    var setDueDateWrapper : LinearLayout? = null
    var calendarView : CalendarView? = null
    var toolbar : AppBarLayout? = null

    // Views - Toolbar
    var toolbarMidText : TextView? = null
    var toolbarBackButton : ImageView? = null
    var toolbarDismissButton : ImageView? = null
    var toolbarConfirmButton : ImageView? = null
    var toolbarProgressBar : ProgressBar? = null

    // HARDCODED
    var currentArtistPage : CollectionReference? = null

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

    // Others
    var date : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_details_dialog, container, false)

        // Utils objects
        usersHelper = UsersHelper

        // Collections
        var usersList : ArrayList<User> = ArrayList()

        // HARDCODED
        currentArtistPageId = "perfect_artistpage_id"

        // Views
        taskTitleEditText = rootView.findViewById(R.id.task_details_title)
        usersListRecyclerView = rootView.findViewById(R.id.users_list_recycler_view)
        addMembersWrapper = rootView.findViewById(R.id.add_members_wrapper)
        setDueDateWrapper = rootView.findViewById(R.id.set_due_date_wrapper)
        calendarView = rootView.findViewById(R.id.calendar_view)

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
                    hideCalendar()
                    disableActionToolbar()
                }
                false -> {
                    showCalendar()
                }
            }
        }
        
        calendarView?.setOnDateChangeListener { calendarView, year, month, day ->
            date = "$day/${month+1}/$year"
            Log.d("CALENDAR", date)
            activateActionToolbar()
        }

        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    companion object {
        @JvmStatic
        fun newInstance(taskTitle: String?, taskId: String?) : TaskDetailsDialogFragment {
            val fragment = TaskDetailsDialogFragment()
            val bundle = Bundle().apply{
                putString ("TASK_TITLE", taskTitle)
                putString("TASK_ID", taskId)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    fun initializeUI() {
        // Setting up boolean controllers
        isCalendarVisible = false
        isMembersListVisible = false
        isTaskTitleEditTextEnabled = false

        // Setting up views
        Utils.disableEditText(taskTitleEditText)
        usersListRecyclerView?.visibility = View.GONE
        calendarView?.visibility = View.GONE
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

    fun showCalendar(){
        isCalendarVisible = true
        calendarView?.visibility = View.VISIBLE
    }

    fun hideCalendar(){
        isCalendarVisible = false
        calendarView?.visibility = View.GONE
    }

    // ************ Show&Hide stuff ************

    override fun updateList(usersOutput: ArrayList<User>) {

    }

    fun userItemClicked(userItem : User){

    }

    fun populateUsersListWithFakeData(usersList : ArrayList<User>){
        usersList.add(User ("123", "hui", "hui2"))
        usersList.add(User ("124", "hui", "mui2"))
        usersList.add(User ("125", "hui", "dui2"))
        usersList.add(User ("126", "hui", "wui2"))
    }

    // Whole ActionToolbar setup here!
    fun activateActionToolbar(){
        isToolbarActivated = true
        toolbarBackButton?.visibility = View.GONE
        toolbarConfirmButton?.visibility = View.VISIBLE
        toolbarDismissButton?.visibility = View.VISIBLE
        toolbarMidText?.text = "Active"

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

            if (isCalendarVisible) hideCalendar()
            else if (isMembersListVisible) hideMembersList()

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

    override fun updateTasks(tasksOutput: ArrayList<Task>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun triggerUpdate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgressBar() {
        toolbarProgressBar?.visibility = View.GONE
    }

    override fun hideAddTaskDialog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTaskDetailChanged() {
        hideProgressBar()
        hideCalendar()
        disableActionToolbar()
    }

    override fun onError(errorLog : String?) {
        Toast.makeText(activity, errorLog.toString(), Toast.LENGTH_LONG).show()
    }

    interface BottomSheetListener {

        fun sendDataBack(){

        }

    }
}