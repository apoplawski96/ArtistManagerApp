package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.utils.TaskHelper

class TaskListActivity : BaseActivity(), TaskUpdater{

    // Collections
    private var tasksList : ArrayList <Task> = ArrayList()
    // Adapters
    private var adapter: TaskListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        // Views
        var taskListRecyclerView = findViewById(R.id.task_list_recyclerview) as RecyclerView

        // Parse tasks
        TaskHelper.parseTasks(perfectArtistPagePath.collection("tasks"), this)
        taskListRecyclerView.layoutManager = LinearLayoutManager(MainActivity(), OrientationHelper.VERTICAL, false)
        adapter = TaskListAdapter(tasksList) { taskItem : Task -> taskItemClicked(taskItem)}
        taskListRecyclerView.adapter = adapter
    }

    override fun updateTasks(tasksOutput: ArrayList<Task>) {
        adapter?.updateItems(tasksOutput)
    }


    fun taskItemClicked (taskItem : Task){
        Toast.makeText(this, "Clicked: ${taskItem.title}", Toast.LENGTH_SHORT).show()
        val intent = Intent(applicationContext, TaskDetailsActivity::class.java).apply{
            putExtra("task_id", taskItem.taskId)
        }
        startActivity(intent)
    }

}
