package com.example.artistmanagerapp.interfaces

import android.view.View
import com.example.artistmanagerapp.models.Task

interface TaskUpdater {
    fun updateTasks(tasksOutput : ArrayList<Task>)
    fun triggerUpdate()
    fun showProgressBar()
    fun hideProgressBar()
    fun hideAddTaskDialog()
    fun onTaskDetailChanged()
    fun onTaskLongClicked(itemView : View)
    fun activateActionToolbar(option : String)
    fun onError(errorLog : String?)
}