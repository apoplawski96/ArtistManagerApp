package com.example.artistmanagerapp.interfaces

import com.example.artistmanagerapp.models.Task

interface TaskUpdater {
    fun updateTasks(tasksOutput : ArrayList<Task>)
}