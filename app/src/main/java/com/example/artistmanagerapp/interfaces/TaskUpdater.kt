package com.example.artistmanagerapp.interfaces

import android.view.View
import com.example.artistmanagerapp.models.Task
import java.io.Serializable

interface TaskUpdater : Serializable {
    fun updateTasks(tasksOutput : ArrayList<Task>)
    fun triggerUpdate()
    fun showProgressBar()
    fun hideProgressBar()
    fun hideAddTaskDialog()
    fun onTaskDetailChanged(option : String?, data : Any?)
    fun onTaskLongClicked(itemView : View, task : Task)
    fun activateActionToolbar(option : String)
    fun onTaskDeleted()
    fun onError(errorLog : String?)
    fun onTasksListEmpty()
}