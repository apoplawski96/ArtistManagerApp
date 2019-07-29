package com.example.artistmanagerapp.fragments

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.activities.TaskListActivity
import com.example.artistmanagerapp.adapters.UsersListAdapter
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.interfaces.UsersListListener
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.UsersHelper
import com.example.artistmanagerapp.utils.Utils
import com.google.firebase.firestore.CollectionReference

class TaskDetailsDialogFragment : DialogFragment(), UserInterfaceUpdater, UsersListListener {

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

    // HARDCODED
    var currentArtistPage : CollectionReference? = null

    // Boolean UI controllers
    var isCalendarVisible : Boolean = false
    var isMembersListVisible : Boolean = false
    var isTaskTitleEditTextEnabled : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_details_dialog, container, false)

        // Utils objects
        usersHelper = UsersHelper()

        // Collections
        var usersList : ArrayList<User> = ArrayList()

        // Views
        taskTitleEditText = rootView.findViewById(R.id.task_details_title)
        usersListRecyclerView = rootView.findViewById(R.id.users_list_recycler_view)
        addMembersWrapper = rootView.findViewById(R.id.add_members_wrapper)
        setDueDateWrapper = rootView.findViewById(R.id.set_due_date_wrapper)
        calendarView = rootView.findViewById(R.id.calendar_view)

        // Getting data from TaskListActivity
        val taskTitle : String = arguments?.getString("TASK_TITLE").toString()

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
                true -> hideMembersList()
                false -> showMembersList()
            }
        }

        setDueDateWrapper?.setOnClickListener {
            when (isCalendarVisible){
                true -> hideCalendar()
                false -> showCalendar()
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
        fun newInstance(taskTitle: String?) : TaskDetailsDialogFragment {
            val fragment = TaskDetailsDialogFragment()
            val bundle = Bundle().apply{ putString ("TASK_TITLE", taskTitle) }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initializeUI() {
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

    override fun updateUI(option: String) {

    }

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

    interface BottomSheetListener {

        fun sendDataBack(){

        }

    }
}