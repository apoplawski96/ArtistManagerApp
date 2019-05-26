package com.example.artistmanagerapp.models

import java.util.*

data class Task(var title : String, var taskId : String, var isCompleted : Boolean){
    constructor(title : String, urgency : String, assignee : String, dueDate : Date, id : String, assignedBy : String, isCompleted : Boolean) : this(title, id, isCompleted)
}
