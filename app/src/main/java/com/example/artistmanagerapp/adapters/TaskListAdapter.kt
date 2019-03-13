package com.example.artistmanagerapp.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.models.Task

class TaskListAdapter (var taskList : ArrayList<Task>) : RecyclerView.Adapter<TaskListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.taskTitle.text = taskList[position].title
    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        val taskTitle : TextView = itemView.findViewById(R.id.task_title) as TextView
    }

}