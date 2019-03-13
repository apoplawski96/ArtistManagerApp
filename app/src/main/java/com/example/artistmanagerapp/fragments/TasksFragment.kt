package com.example.artistmanagerapp.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.activities.MainActivity
import com.example.artistmanagerapp.adapters.SelectArtistPageAdapter
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.Task
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.horizontal_select_artist_recycler_view.*

class TasksFragment : Fragment() {

    private var tasksList : ArrayList <Task> = ArrayList()


    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_tasks, container, false)

        populateTasksListWithFakeStuff()

        var taskListRecyclerView = rootView.findViewById(R.id.task_list_recyclerview) as RecyclerView

        taskListRecyclerView.layoutManager = LinearLayoutManager(MainActivity(), OrientationHelper.VERTICAL, false)
        taskListRecyclerView.adapter = TaskListAdapter(tasksList)

        return rootView
    }

    fun populateTasksListWithFakeStuff(){
        tasksList.add(Task("task1"))
        tasksList.add(Task("task1"))
        tasksList.add(Task("task1"))
    }

}