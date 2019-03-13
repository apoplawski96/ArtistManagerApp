package com.example.artistmanagerapp.models

import java.util.*

data class Task(var title : String){
    constructor(title : String, urgency : String, assignee : String, dueDate : Date) : this(title){

    }
}