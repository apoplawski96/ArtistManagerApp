package com.example.artistmanagerapp.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.models.Task
import kotlinx.android.synthetic.main.task_item.view.*

class TaskListAdapter (var taskList : ArrayList<Task>, val clickListener: (Task) -> Unit) : RecyclerView.Adapter<TaskListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(taskList[position], clickListener)
    }


    fun updateItems(newTasks : ArrayList<Task>){
        taskList = newTasks
        notifyDataSetChanged()
    }

    interface OnTaskListener {
        fun onTaskClick (position: Int)
    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind (task : Task, clickListener: (Task) -> Unit){
            itemView.setOnClickListener {clickListener(task)}
            itemView.task_title.text = task.title
        }


        // tutaj niby mial byc zainicjalizowany on note listener
        val taskTitle : TextView = itemView.findViewById(R.id.task_title) as TextView
    }

}