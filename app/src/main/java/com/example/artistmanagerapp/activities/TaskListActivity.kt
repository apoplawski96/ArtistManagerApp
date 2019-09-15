package com.example.artistmanagerapp.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.constants.Constants
import com.example.artistmanagerapp.constants.FirebaseConstants
import com.example.artistmanagerapp.firebase.FirebaseActivityLogsManager
import com.example.artistmanagerapp.firebase.FirebaseTasksManager
import com.example.artistmanagerapp.fragments.GridMenuFragment
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.ui.DialogCreator
import com.google.firebase.firestore.CollectionReference
import kotlinx.android.synthetic.main.item_task.view.*
import java.io.Serializable

class TaskListActivity : BaseActivity(), TaskUpdater, UserInterfaceUpdater, DialogCreator.DialogControllerCallback, Serializable{

    // Constants
    val ACTIVITY_DESCRIPTION = "Tasks"
    val ACT_TAG = "TasksListActivity"

    // Variables
    var pageId : String? = null
    var selectedTaskId : String? = null
    var selectedTaskItemView : View? = null

    // Bundled data
    var artistPageInstance : ArtistPage? = null
    var userInstance : User? = null
    var taskInstance : Task? = null

    // Views
    var checkBox : CheckBox? = null
    var showCompletedTasks : TextView? = null
    var taskListRecyclerView : RecyclerView? = null
    var completedTaskListRecyclerView : RecyclerView? = null
    var noCompletedTasksTextView : TextView? = null
    var fabTasksActivity : FloatingActionButton? = null
    var addNewTaskDialog : Dialog? = null
    var taskNameInput : EditText? = null
    var tasksDialogClose : TextView? = null
    var addTaskSubmitButton : Button? = null
    var tasksListEmptyTv : TextView? = null
    var addFirstTaskText : TextView? = null

    // Views - Toolbar
    var toolbarMidText : TextView? = null
    var toolbarBackButton : ImageView? = null
    var toolbarProgressBar : ProgressBar? = null
    var toolbarDismissButton : ImageView? = null
    var toolbarDeleteButton : ImageView? = null

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
    var isToolbarActivated : Boolean? = false

    // Task onLongClick variables
    var isTaskSelected : Boolean = false
    var itemTemp : View? = null

    // Firebase
    var pathToTasksCollection : CollectionReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        initializeUI()

        // Others
        val context : Context = this
        pageId = intent.getStringExtra(Constants.PAGE_ID_BUNDLE)

        // Getting ArtistPage and User bundled data
        artistPageInstance = intent.extras.getSerializable(GridMenuFragment.c.BUNDLE_ARTIST_PAGE_INSTANCE) as ArtistPage?
        userInstance = intent.extras.getSerializable(GridMenuFragment.c.BUNDLE_USER_INSTANCE) as User?

        // Generate path to tasks list in Firestore
        pathToTasksCollection = artistPagesCollectionPath.document(pageId.toString()).collection(
            FirebaseConstants.ARTIST_TASKS)
        // Tasks parsing
        FirebaseTasksManager.parseTasks(pathToTasksCollection as CollectionReference, this)

        Log.d(ACT_TAG, "TasksListActivity")
        Log.d(ACT_TAG, artistPageInstance.toString())
        Log.d(ACT_TAG, userInstance.toString())

        // Views
        taskListRecyclerView = findViewById(R.id.task_list_recyclerview) as RecyclerView
        completedTaskListRecyclerView = findViewById(R.id.completed_task_list_recyclerview) as RecyclerView
        checkBox = findViewById(R.id.check_box)
        showCompletedTasks = findViewById(R.id.show_completed_tasks)
        fabTasksActivity = findViewById(R.id.fab_tasks_list)
        tasksListEmptyTv = findViewById(R.id.tasks_list_empty_tv)
        addFirstTaskText = findViewById(R.id.add_first_task_text)

        // Views - Toolbar
        toolbarMidText = findViewById(R.id.toolbar_tasks_list_textview)
        toolbarBackButton = findViewById(R.id.toolbar_back_button)
        toolbarDismissButton = findViewById(R.id.toolbar_dismiss_button)
        toolbarDeleteButton = findViewById(R.id.toolbar_delete)
        toolbarProgressBar = findViewById(R.id.toolbar_progress_bar)

        // AddNewTaskDialog stuff
        addNewTaskDialog = Dialog(this)
        addNewTaskDialog?.setContentView(R.layout.dialog_add_new_task)

        // Setting up toolbar
        //toolbarActivityDescription = findViewById(R.id.toolbar_activity_description)
        //toolbarActivityDescription?.text = ACTIVITY_DESCRIPTION

        // TaskListRecyclerView setup
        taskListRecyclerView?.layoutManager = LinearLayoutManager(MainActivity(), OrientationHelper.VERTICAL, false)
        adapter = TaskListAdapter(this, context, tasksList, pathToTasksCollection as CollectionReference) { taskItem : Task -> taskItemClicked(taskItem)}
        taskListRecyclerView?.adapter = adapter

        // CompletedTaskListRecyclerView setup
        completedTaskListRecyclerView?.layoutManager = LinearLayoutManager(MainActivity(), OrientationHelper.VERTICAL, false)
        adapterCompleted = TaskListAdapter(this, context, completedTasksList, pathToTasksCollection as CollectionReference) { taskItem : Task -> taskItemClicked(taskItem) }
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

        addFirstTaskText?.setOnClickListener {
            showAddNewTaskDialog()
        }

        toolbarBackButton?.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onResume() {
        triggerUpdate()
        super.onResume()
    }

    // **********  TaskUpdater interface implementation **********

    override fun updateTasks(tasksOutput: ArrayList<Task>) {
        var tasksOpen : ArrayList<Task> = FirebaseTasksManager.sortTasks(false, tasksOutput)
        var tasksCompleted : ArrayList<Task> = FirebaseTasksManager.sortTasks(true, tasksOutput)

        adapter?.updateItems(tasksOpen)
        onTasksListNotEmpty()
        adapterCompleted?.updateItems(tasksCompleted)

        hideProgressBar()

        if (tasksOpen.isEmpty()){
            //noTasksTextView?.visibility = View.VISIBLE
        } else {
            //noTasksTextView?.visibility = View.GONE
        }

        if (tasksCompleted.isEmpty()){
            showCompletedTasks?.visibility = View.GONE
            noCompletedTasksTextView?.visibility = View.GONE
        } else {
            noCompletedTasksTextView?.visibility = View.GONE
        }
    }

    override fun triggerUpdate() {
        FirebaseTasksManager.parseTasks(pathToTasksCollection, this)
    }

    fun triggerTasksListReload(pathToTasksCollection : CollectionReference?){
        FirebaseTasksManager.parseTasks(pathToTasksCollection, this)
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
        taskInstance = taskItem
        val taskUpdater : TaskUpdater? = this
//        var bottomSheet : TaskDetailsDialogFragment = TaskDetailsDialogFragment.newInstance(taskInstance, userInstance, artistPageInstance)
//        replaceFragment(bottomSheet)

        val intent = Intent(applicationContext, TaskDetailsActivity::class.java).apply{
            putExtra (Constants.BUNDLE_USER_INSTANCE, userInstance)
            putExtra (Constants.BUNDLE_ARTIST_PAGE_INSTANCE, artistPageInstance)
            putExtra (Constants.BUNDLE_TASK_INSTANCE, taskInstance)
        }
        startActivity(intent)

        //bottomSheet.show(supportFragmentManager, "bottomSheet")
    }

    fun replaceFragment(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.tasks_list_layout, fragment)
        fragmentTransaction.addToBackStack(null).commit()
    }

    override fun initializeUI() {
        completedTaskListRecyclerView?.visibility = View.GONE
        toolbarProgressBar?.visibility = View.GONE
        //noTasksTextView?.visibility = View.GONE
        noCompletedTasksTextView?.visibility = View.GONE
        toolbarMidText?.text = "Tasks Manager"
    }

    override fun updateUI(option: String, data : Any?) {

    }

    override fun onTaskDetailChanged(option : String?, data : Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideAddTaskDialog() {
        onBackPressed()
    }

    override fun onTaskLongClicked(itemView : View, task : Task) {

        // PRZEROBIĆ NA WHEN

        if (isTaskSelected == false){
            isTaskSelected = true
            itemTemp = itemView
            setTaskColorWarning(itemView)
            selectedTaskId = task.taskId
            selectedTaskItemView = itemView
            activateActionToolbar(Constants.TASKS_ON_LONG_CLICKED)
        } else {
            if (itemTemp!!.equals(itemView)){
                disableActionToolbar()
                itemTemp = null
                setTaskColorDefault(itemView)
                isTaskSelected = false
            } else {
                // Disabling previous item
                setTaskColorDefault(itemTemp)
                itemTemp = itemView
                selectedTaskId = task.taskId
                selectedTaskItemView = itemView
                setTaskColorWarning(itemView)
            }
        }

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
            if (taskTitle.isNotBlank()){
                taskNameInput?.text = null
                FirebaseTasksManager.addTask(Task(taskTitle, false, true), artistPageInstance, userInstance, this)
                hideAddNewTaskDialog()
                showProgressBar()
            } else {
                Toast.makeText(this, "Task title can't be empty!", Toast.LENGTH_SHORT).show()
            }
        }

        tasksDialogClose?.setOnClickListener {
            hideAddNewTaskDialog()
        }
    }

    private fun hideAddNewTaskDialog(){
        isAddNewTaskDialogOpen = false
        addNewTaskDialog?.hide()
    }

    override fun activateActionToolbar(option: String) {
        val c = Constants

        // Standard set of UI changes
        isToolbarActivated = true

        when (option){
            c.TASKS_ON_LONG_CLICKED -> {
                toolbarMidText?.text = "Delete Task?"
                toolbarBackButton?.visibility = View.GONE
                toolbarDeleteButton?.visibility = View.VISIBLE
                toolbarDismissButton?.visibility = View.VISIBLE
            }
        }

        // Confirm button onclick here!
        toolbarDeleteButton?.setOnClickListener {
            DialogCreator.showDialog(DialogCreator.DialogType.TASK_DELETE_WARNING, this, this)
            isTaskSelected = false
            itemTemp = null
        }

        toolbarDismissButton?.setOnClickListener {
            disableActionToolbar()
            setTaskColorDefault(selectedTaskItemView)
        }

    }

    override fun onAccept(option : DialogCreator.DialogControllerCallback.CallbackOption?) {
        FirebaseTasksManager.deleteTask(selectedTaskId, pathToTasksCollection as CollectionReference, this)
        logsManager.createActivityLog(userInstance, artistPageInstance?.artistPageId.toString(), selectedTaskId, FirebaseActivityLogsManager.ActivityLogCategory.TASK_REMOVED)
        showProgressBar()
        disableActionToolbar()
    }

    override fun onDismiss() { disableActionToolbar() }

    override fun onShown() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCallInvalid() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setTaskColorWarning(itemView: View?){
        itemView?.task_item_background?.setBackgroundColor(Color.parseColor("#94191f"))
    }

    fun setTaskColorDefault(itemView: View?){
        itemView?.task_item_background?.setBackgroundColor(Color.parseColor("#2e2c36"))
    }

    override fun onTaskDeleted() {
        triggerUpdate()
        hideProgressBar()
        setTaskColorDefault(selectedTaskItemView)
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show()
    }

    fun disableActionToolbar(){
        isToolbarActivated = false
        toolbarBackButton?.visibility = View.VISIBLE
        toolbarDeleteButton?.visibility = View.GONE
        toolbarDismissButton?.visibility = View.GONE
        toolbarMidText?.text = "Tasks Manager"
    }

    override fun onTasksListEmpty() {
        taskListRecyclerView?.visibility = View.GONE
        completedTaskListRecyclerView?.visibility = View.GONE
        showCompletedTasks?.visibility = View.GONE
        tasksListEmptyTv?.visibility = View.VISIBLE
        addFirstTaskText?.visibility = View.VISIBLE
    }

    fun onTasksListNotEmpty(){
        taskListRecyclerView?.visibility = View.VISIBLE
        completedTaskListRecyclerView?.visibility = View.VISIBLE
        showCompletedTasks?.visibility = View.VISIBLE
        tasksListEmptyTv?.visibility = View.GONE
        addFirstTaskText?.visibility = View.GONE
    }

    override fun onDismissWithOption(option: DialogCreator.DialogControllerCallback.DismissCalbackOption) {

    }

    override fun onCodeRedeemed(pageId: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
