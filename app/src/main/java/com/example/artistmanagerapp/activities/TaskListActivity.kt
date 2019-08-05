package com.example.artistmanagerapp.activities

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.fragments.TaskDetailsDialogFragment
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.utils.TaskHelper
import com.google.firebase.firestore.CollectionReference

class TaskListActivity : BaseActivity(), TaskUpdater, UserInterfaceUpdater{

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
    var fabTasksActivity : FloatingActionButton? = null
    var addNewTaskDialog : Dialog? = null
    var taskNameInput : EditText? = null
    var tasksDialogClose : TextView? = null
    var addTaskSubmitButton : Button? = null

    // Collections
    private var tasksList : ArrayList <Task> = ArrayList()
    private var completedTasksList : ArrayList <Task> = ArrayList()

    // Adapters
    private var adapter : TaskListAdapter? = null
    private var adapterCompleted : TaskListAdapter? = null

    // Others
    var isCompletedTasksListVisible : Boolean? = false
    var isAddNewTaskDialogOpen : Boolean? = false
    var isBottomSheetExpanded : Boolean? = false

    // Firebase
    lateinit var pathToTasksCollection : CollectionReference

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
        fabTasksActivity = findViewById(R.id.fab_tasks_list)

        // AddNewTaskDialog stuff
        addNewTaskDialog = Dialog(this)
        addNewTaskDialog?.setContentView(R.layout.dialog_add_new_task)

        // Generate path to tasks list in Firestore
        pathToTasksCollection = perfectArtistPagePath.collection("tasks")

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

        initializeUI()

        // Setting boolean value for completed tasks list
        isCompletedTasksListVisible = false

        // ************************** ONCLICKS SETUP **************************

        // ShowCompletedTasks onClick setup
        showCompletedTasks?.setOnClickListener {
            if (isCompletedTasksListVisible == false){
                showCompletedTasks?.text = "Hide completed tasks"
                completedTaskListRecyclerView?.visibility = View.VISIBLE
                isCompletedTasksListVisible = true
            } else {
                showCompletedTasks?.text = "Show completed tasks"
                completedTaskListRecyclerView?.visibility = View.GONE
                noCompletedTasksTextView?.visibility = View.GONE
                isCompletedTasksListVisible = false
            }
        }

        fabTasksActivity?.setOnClickListener {
            showAddNewTaskDialog()
        }

        // ************************** ONCLICKS SETUP **************************

        /*TaskHelper.addTask(Task("task1", "low", true), pathToTasksCollection)
        TaskHelper.addTask(Task("task2", "low", true), pathToTasksCollection)
        TaskHelper.addTask(Task("task3", "low", false), pathToTasksCollection)
        TaskHelper.addTask(Task("task4", "low", false), pathToTasksCollection)*/

    }

    // Setting up system back button custom behaviour
    override fun onBackPressed() {
        if (isAddNewTaskDialogOpen == true){
            hideAddNewTaskDialog()
        } else if (isBottomSheetExpanded == true){

        } else{
            super.onBackPressed()
        }
    }

    // **********  TaskUpdater interface implementation **********

    override fun updateTasks(tasksOutput: ArrayList<Task>) {
        var tasksOpen : ArrayList<Task> = TaskHelper.sortTasks(false, tasksOutput)
        var tasksCompleted : ArrayList<Task> = TaskHelper.sortTasks(true, tasksOutput)

        adapter?.updateItems(tasksOpen)
        adapterCompleted?.updateItems(tasksCompleted)

        hideProgressBar()

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

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(errorLog : String?) {

    }

    // **********  TaskUpdater interface implementation **********

    fun taskItemClicked (taskItem : Task) {
        var bottomSheet : TaskDetailsDialogFragment = TaskDetailsDialogFragment.newInstance(taskItem.title, taskItem.taskId)
        bottomSheet.show(supportFragmentManager, "bottomSheet")
    }

    override fun initializeUI() {
        completedTaskListRecyclerView?.visibility = View.GONE
        toolbarProgressBar?.visibility = View.GONE
        noTasksTextView?.visibility = View.GONE
        noCompletedTasksTextView?.visibility = View.GONE
    }

    override fun updateUI(option: String) {

    }

    override fun onTaskDetailChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideAddTaskDialog() {
        onBackPressed()
    }

    private fun showAddNewTaskDialog(){
        isAddNewTaskDialogOpen = true

        // Views
        taskNameInput = addNewTaskDialog?.findViewById(R.id.task_name_input)
        addTaskSubmitButton = addNewTaskDialog?.findViewById(R.id.add_task_submit_button)
        tasksDialogClose = addNewTaskDialog?.findViewById(R.id.tasks_dialog_close_x)

        addNewTaskDialog?.show()

        addTaskSubmitButton?.setOnClickListener {
            val taskTitle : String = taskNameInput?.text.toString()

            TaskHelper.addTask(Task(taskTitle, false), pathToTasksCollection, this)
            hideAddNewTaskDialog()
            showProgressBar()
        }

        tasksDialogClose?.setOnClickListener {
            hideAddNewTaskDialog()
        }
    }

    private fun hideAddNewTaskDialog(){
        isAddNewTaskDialogOpen = false
        addNewTaskDialog?.hide()
    }

}
