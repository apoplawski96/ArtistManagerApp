package com.example.artistmanagerapp.models

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class Task : Serializable{

    var title : String? = null
    var taskId : String? = null
    var isCompleted : Boolean = false
    var createdById : String? = null
    var description : String? = null
    var assigneesList : ArrayList<String>? = null
    var dueDate : String? = null
    var dateCreated : String? = null
    var isTaskNew : Boolean? = null

    constructor(mTitle : String?, mIsCompleted : Boolean) {
        this.title = mTitle
        this.isCompleted = mIsCompleted
    }

    constructor(mTitle : String?, mIsCompleted : Boolean, mIsTaskNew : Boolean?) {
        this.title = mTitle
        this.isCompleted = mIsCompleted
        this.isTaskNew = mIsTaskNew
    }

    constructor(mTitle : String?, mTaskId: String?, mIsCompleted : Boolean, mCreatedById : String?, description : String?, mDueDate : String?, mDateCreated : String, mAssigneesList : ArrayList<String>?) {
        this.title = mTitle
        this.taskId = mTaskId
        this.isCompleted = mIsCompleted
        this.createdById = mCreatedById
        this.assigneesList = mAssigneesList
        this.dueDate = mDueDate
        this.dateCreated = mDateCreated
        this.description = description
    }



    override fun toString(): String {
        return "Title: $title, id: $taskId, isCompleted: $isCompleted, createdBy: $createdById, dueDate: $dueDate, dateCreated: $dateCreated"
    }
}
