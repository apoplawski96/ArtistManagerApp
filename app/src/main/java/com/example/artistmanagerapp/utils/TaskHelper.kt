package com.example.artistmanagerapp.utils

import com.example.artistmanagerapp.activities.BaseActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query

object TaskHelper : BaseActivity() {

    fun addTask (taskData : HashMap<String, Any>, pathToTasksCollection : CollectionReference){
        pathToTasksCollection.document().set(taskData)
    }

    fun returnTasksWhereEqualTo (pathToTasksCollection: CollectionReference, key : String, value : String) : Query {
        return pathToTasksCollection.whereEqualTo(key, value)
    }

}