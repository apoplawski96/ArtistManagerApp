package com.example.artistmanagerapp.adapters

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.activities.TaskListActivity
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.utils.TaskHelper
import com.google.firebase.firestore.CollectionReference
import kotlinx.android.synthetic.main.item_task.view.*

class TaskListAdapter (val context : Context?, var taskList : ArrayList<Task>, pathToTasksCollection : CollectionReference, val clickListener: (Task) -> Unit) : RecyclerView.Adapter<TaskListAdapter.ViewHolder>(){

    private var taskArrayList : ArrayList<Task> = this.taskList
    var path : CollectionReference = pathToTasksCollection

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ViewHolder(view, context, path)
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


    class ViewHolder (itemView : View, context: Context?, pathToTasksCollection : CollectionReference) : RecyclerView.ViewHolder(itemView) {
        var path : CollectionReference = pathToTasksCollection


        fun bind (task : Task, clickListener: (Task) -> Unit){
            itemView.setOnClickListener {clickListener(task)}
            itemView.task_title.text = task.title

            // Checkbox setup
            itemView.check_box.isChecked = task.isCompleted
            setupCheckBoxStatus()

            // Checbox onclick setup
            itemView.check_box.setOnClickListener {
                Toast.makeText(it.context, "Checkbox" + task.title + "clicked!", Toast.LENGTH_SHORT).show()

                // Setting "isCompleted" value in database
                TaskHelper.changeTaskCompletionStatus(task.taskId, itemView.check_box.isChecked, path)

                setupCheckBoxStatus()
            }

        }

        fun setupCheckBoxStatus(){
            if (itemView.check_box.isChecked){
                itemView.task_title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else if (!itemView.check_box.isChecked){
                itemView.task_title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

    }

}