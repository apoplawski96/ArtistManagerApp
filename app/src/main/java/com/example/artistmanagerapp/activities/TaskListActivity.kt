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
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.fragments.TaskBackdropFragment
import com.example.artistmanagerapp.interfaces.TaskDetailPresenter
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.utils.TaskHelper

class TaskListActivity : BaseActivity(), TaskUpdater{

    // Views
    var checkBox : CheckBox? = null

    // Collections
    private var tasksList : ArrayList <Task> = ArrayList()

    // Adapters
    private var adapter: TaskListAdapter? = null

    // BottomSheet stuff
    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null
    var bottomSheetFragment : Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        val context : Context = this

        // Views
        var taskListRecyclerView = findViewById(R.id.task_list_recyclerview) as RecyclerView
        checkBox = findViewById(R.id.check_box)

        // Generate path to tasks list in Firestore
        val pathToTasksCollection = perfectArtistPagePath.collection("tasks")

        // BottomSheet Fragment
        //bottomSheetFragment = TaskBackdropFragment()
        //bottomSheetFragment?.fragmentManager?.findFragmentById(R.id.filter_fragment)
        bottomSheetFragment = supportFragmentManager.findFragmentById(R.id.filter_fragment)

        configureBackdrop()

        // Parsing tasks and setting up the task list
        TaskHelper.parseTasks(perfectArtistPagePath.collection("tasks"), this)
        taskListRecyclerView.layoutManager = LinearLayoutManager(MainActivity(), OrientationHelper.VERTICAL, false)
        adapter = TaskListAdapter(context, tasksList, pathToTasksCollection) { taskItem : Task -> taskItemClicked(taskItem)}
        taskListRecyclerView.adapter = adapter

        TaskHelper.addTask(Task("asda", "low", false), pathToTasksCollection)

    }

    // Setting up system back button custom behaviour
    override fun onBackPressed() {
        if (mBottomSheetBehavior?.state == (BottomSheetBehavior.STATE_EXPANDED)){
            mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }
    }


    override fun updateTasks(tasksOutput: ArrayList<Task>) {
        adapter?.updateItems(tasksOutput)
    }

    fun taskItemClicked (taskItem : Task){
        // Populate fragment with task info, non Kotlin way sadly
        /*var mArgs = Bundle()
        mArgs.putString("task_title", taskItem.title)
        bottomSheetFragment?.setArguments(mArgs)*/
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun configureBackdrop(){
        bottomSheetFragment?.let {
            BottomSheetBehavior.from(it.view)?.let { bsb ->
                bsb.state = BottomSheetBehavior.STATE_HIDDEN
                mBottomSheetBehavior = bsb
            }
        }
    }

}
