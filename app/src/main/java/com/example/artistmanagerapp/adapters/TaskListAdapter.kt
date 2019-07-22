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
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.utils.TaskHelper
import com.google.firebase.firestore.CollectionReference
import kotlinx.android.synthetic.main.item_task.view.*

class TaskListAdapter (taskUpdater : TaskUpdater, val context : Context?, var taskList : ArrayList<Task>, pathToTasksCollection : CollectionReference, val clickListener: (Task) -> Unit) : RecyclerView.Adapter<TaskListAdapter.ViewHolder>(){

    var path : CollectionReference = pathToTasksCollection
    val taskUpdater : TaskUpdater = taskUpdater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ViewHolder(taskUpdater, view, context, path)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(taskUpdater, taskList[position], clickListener)
    }


    fun updateItems(newTasks : ArrayList<Task>){
        taskList = newTasks
        notifyDataSetChanged()
    }


    class ViewHolder (taskUpdater: TaskUpdater, itemView : View, context: Context?, pathToTasksCollection : CollectionReference) : RecyclerView.ViewHolder(itemView) {
        var path : CollectionReference = pathToTasksCollection
        val mTaskUpdater : TaskUpdater = taskUpdater

        fun bind (taskUpdater : TaskUpdater, task : Task, clickListener: (Task) -> Unit){
            itemView.setOnClickListener {clickListener(task)}
            itemView.task_title.text = task.title
            val taskUpdater = mTaskUpdater

            // Checkbox setup
            itemView.check_box.isChecked = task.isCompleted
            setupCheckBoxStatus()

            // Checbox onclick setup
            itemView.check_box.setOnClickListener {
                val taskListActivity : TaskListActivity = TaskListActivity()
                taskListActivity.showProgressBar()
                Toast.makeText(taskListActivity, "tu progres bar kurwa", Toast.LENGTH_SHORT).show()
                // Setting "isCompleted" value in database
                TaskHelper.changeTaskCompletionStatus(taskUpdater, task.taskId, itemView.check_box.isChecked, path)

                //setupCheckBoxStatus()
            }

        }

        fun setupCheckBoxStatus(){
            if (itemView.check_box.isChecked){
                //itemView.task_title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else if (!itemView.check_box.isChecked){
                //itemView.task_title.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

    }

}