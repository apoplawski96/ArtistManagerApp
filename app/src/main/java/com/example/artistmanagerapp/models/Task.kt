package com.example.artistmanagerapp.models

import com.google.firebase.firestore.PropertyName
import java.util.*
import kotlin.collections.ArrayList

class Task {

    @PropertyName("title")
    var title : String? = null
    var taskId : String? = null
    @PropertyName("isCompleted")
    var isCompleted : Boolean = false
    var createdById : String? = null
    var assigneesList : ArrayList<User>? = null
    var dueDate : Date? = null
    var urgency : String? = null

    // RedeemCode (redeemCodeString, false, artistPageId, userId, null)

    constructor(mTitle : String?, mIsCompleted : Boolean) {
        this.title = mTitle
        this.isCompleted = mIsCompleted
    }

    constructor(mTitle : String?, mTaskId : String?, mIsCompleted : Boolean) {
        this.title = mTitle
        this.taskId = mTaskId
        this.isCompleted = mIsCompleted
    }

    constructor(mTitle : String?, mTaskId: String?, mIsCompleted : Boolean, mCreatedById : String?, mDueDate : Date?, mUrgency : String, mAssigneesList : ArrayList<User>?) {
        this.title = mTitle
        this.taskId = mTaskId
        this.isCompleted = mIsCompleted
        this.createdById = mCreatedById
        this.assigneesList = mAssigneesList
        this.dueDate = mDueDate
        this.urgency = mUrgency
    }

}
