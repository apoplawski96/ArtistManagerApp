package com.example.artistmanagerapp.models

import java.util.*

data class Task(var title : String, var taskId : String){
    constructor(title : String, urgency : String, assignee : String, dueDate : Date, id : String) : this(title, id)
}
