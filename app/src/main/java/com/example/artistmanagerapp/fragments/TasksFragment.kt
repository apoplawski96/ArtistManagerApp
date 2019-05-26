package com.example.artistmanagerapp.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.activities.MainActivity
import com.example.artistmanagerapp.adapters.SelectArtistPageAdapter
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.utils.TaskHelper
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.horizontal_select_artist_recycler_view.*

class TasksFragment : BaseFragment(), TaskUpdater{

    // Collections
    private var tasksList : ArrayList <Task> = ArrayList()
    // Adapters
    private var adapter: TaskListAdapter? = null

    override fun updateTasks(tasksOutput: ArrayList<Task>) {
        adapter?.updateItems(tasksOutput)
    }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_tasks, container, false)

        // Views
        /*var taskListRecyclerView = rootView.findViewById(R.id.task_list_recyclerview) as RecyclerView

        // Parse tasks
        TaskHelper.parseTasks(perfectArtistPagePath.collection("tasks"), this)
        taskListRecyclerView.layoutManager = LinearLayoutManager(MainActivity(), OrientationHelper.VERTICAL, false)
        adapter = TaskListAdapter(context, tasksList) { taskItem : Task -> taskItemClicked(taskItem)}
        taskListRecyclerView.adapter = adapter*/

        return rootView
    }

    fun taskItemClicked (taskItem : Task){
        Toast.makeText(activity, "Clicked: ${taskItem.title}", Toast.LENGTH_SHORT).show()
    }



}