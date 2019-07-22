package com.example.artistmanagerapp.activities

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.fragments.ModalBottomSheetFragment
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.utils.TaskHelper

class TaskListActivity : BaseActivity(), TaskUpdater{

    // Constants
    val ACTIVITY_DESCRIPTION = "Tasks"

    // Views
    var checkBox : CheckBox? = null
    var showCompletedTasks : TextView? = null
    var taskListRecyclerView : RecyclerView? = null
    var completedTaskListRecyclerView : RecyclerView? = null
    var noTasksTextView : TextView? = null
    var noCompletedTasksTextView : TextView? = null
    var toolbarProgressBar : ProgressBar? = null

    // Collections
    private var tasksList : ArrayList <Task> = ArrayList()
    private var completedTasksList : ArrayList <Task> = ArrayList()

    // Adapters
    private var adapter : TaskListAdapter? = null
    private var adapterCompleted : TaskListAdapter? = null

    // Others
    var isCompletedTasksListVisible : Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        initializeUI()

        // Others
        val context : Context = this

        // Views
        taskListRecyclerView = findViewById(R.id.task_list_recyclerview) as RecyclerView
        completedTaskListRecyclerView = findViewById(R.id.completed_task_list_recyclerview) as RecyclerView
        checkBox = findViewById(R.id.check_box)
        showCompletedTasks = findViewById(R.id.show_completed_tasks)
        noTasksTextView = findViewById(R.id.no_open_tasks_tv)
        noCompletedTasksTextView = findViewById(R.id.no_completed_tasks_tv)
        toolbarProgressBar = findViewById(R.id.toolbar_progress_bar)

        // Generate path to tasks list in Firestore
        val pathToTasksCollection = perfectArtistPagePath.collection("tasks")

        // Setting up toolbar
        toolbarActivityDescription = findViewById(R.id.toolbar_activity_description)
        toolbarActivityDescription?.text = ACTIVITY_DESCRIPTION

        // Tasks parsing
        TaskHelper.parseTasks(perfectArtistPagePath.collection("tasks"), this)

        // TaskListRecyclerView setup
        taskListRecyclerView?.layoutManager = LinearLayoutManager(MainActivity(), OrientationHelper.VERTICAL, false)
        adapter = TaskListAdapter(this, context, tasksList, pathToTasksCollection) { taskItem : Task -> taskItemClicked(taskItem)}
        taskListRecyclerView?.adapter = adapter

        // CompletedTaskListRecyclerView setup
        completedTaskListRecyclerView?.layoutManager = LinearLayoutManager(MainActivity(), OrientationHelper.VERTICAL, false)
        adapterCompleted = TaskListAdapter(this, context, completedTasksList, pathToTasksCollection) { taskItem : Task -> taskItemClicked(taskItem) }
        completedTaskListRecyclerView?.adapter = adapterCompleted

        // Setting boolean value for completed tasks list
        isCompletedTasksListVisible = false

        // ShowCompletedTasks onClick setup
        showCompletedTasks?.setOnClickListener {
            if (isCompletedTasksListVisible == false){
                completedTaskListRecyclerView?.visibility = View.VISIBLE
                isCompletedTasksListVisible = true
            } else {
                completedTaskListRecyclerView?.visibility = View.GONE
                isCompletedTasksListVisible = false
            }
        }

        /*TaskHelper.addTask(Task("task1", "low", true), pathToTasksCollection)
        TaskHelper.addTask(Task("task2", "low", true), pathToTasksCollection)
        TaskHelper.addTask(Task("task3", "low", false), pathToTasksCollection)
        TaskHelper.addTask(Task("task4", "low", false), pathToTasksCollection)*/

    }

    // Setting up system back button custom behaviour
    override fun onBackPressed() {
        /*if (mBottomSheetBehavior?.state == (BottomSheetBehavior.STATE_EXPANDED)){
            mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }*/

        super.onBackPressed()
    }

    // **********  TaskUpdater interface implementation **********

    override fun updateTasks(tasksOutput: ArrayList<Task>) {
        var tasksOpen : ArrayList<Task> = TaskHelper.sortTasks(false, tasksOutput)
        var tasksCompleted : ArrayList<Task> = TaskHelper.sortTasks(true, tasksOutput)

        adapter?.updateItems(tasksOpen)
        adapterCompleted?.updateItems(tasksCompleted)

        hideProgressBar()

        // To chyba mozna lepiej zrobic ale chuj tam na razie
        if (tasksOpen.isEmpty()){
            noTasksTextView?.visibility = View.VISIBLE
        } else {
            noTasksTextView?.visibility = View.GONE
        }

        if (tasksCompleted.isEmpty()){
            noCompletedTasksTextView?.visibility = View.VISIBLE
        } else {
            noCompletedTasksTextView?.visibility = View.GONE
        }
    }

    override fun triggerUpdate() {
        TaskHelper.parseTasks(perfectArtistPagePath.collection("tasks"), this)
    }

    override fun showProgressBar() {
        toolbarProgressBar?.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        toolbarProgressBar?.visibility = View.GONE
    }

    // **********  TaskUpdater interface implementation **********

    fun taskItemClicked (taskItem : Task) {
        var bottomSheet : ModalBottomSheetFragment = ModalBottomSheetFragment.newInstance(taskItem.title)
        bottomSheet.show(supportFragmentManager, "bottomSheet")
    }

    fun initializeUI() {
        completedTaskListRecyclerView?.visibility = View.GONE
        toolbarProgressBar?.visibility = View.GONE
        noTasksTextView?.visibility = View.GONE
        noCompletedTasksTextView?.visibility = View.GONE
    }

}
