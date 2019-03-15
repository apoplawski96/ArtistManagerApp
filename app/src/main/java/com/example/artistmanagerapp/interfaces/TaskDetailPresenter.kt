package com.example.artistmanagerapp.interfaces

import com.example.artistmanagerapp.models.Task

interface TaskDetailPresenter {
    fun showTask (taskOutput : Task)
}