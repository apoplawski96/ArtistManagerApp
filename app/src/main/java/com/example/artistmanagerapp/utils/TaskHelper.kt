package com.example.artistmanagerapp.utils

import android.util.Log
import com.example.artistmanagerapp.interfaces.TaskDetailPresenter
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

object TaskHelper {

    fun addTask (taskData : HashMap<String, Any>, pathToTasksCollection : CollectionReference){
        pathToTasksCollection.document().set(taskData)
    }

    fun returnTasksWhereEqualTo (pathToTasksCollection: CollectionReference, key : String, value : String) : Query {
        return pathToTasksCollection.whereEqualTo(key, value)
    }

    fun parseTasks (pathToTasksCollection : CollectionReference, taskUpdater : TaskUpdater){
        var tasksOutput : ArrayList<Task> = ArrayList()

        pathToTasksCollection.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result){
                        tasksOutput.add(Task(document.get("title").toString(), document.id))
                    }
                    taskUpdater.updateTasks(tasksOutput)
                } else {

                }
            }
    }

    fun getTaskData (pathToTasksCollection: CollectionReference, taskId : String, taskDetailPresenter: TaskDetailPresenter){
        lateinit var outputTask : Task

        pathToTasksCollection.document(taskId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()){
                    var taskTitle = documentSnapshot.getString("title").toString()
                    var taskId = documentSnapshot.getString("id").toString()
                    outputTask = Task (taskTitle, taskId)
                }
                taskDetailPresenter.showTask(outputTask)
        }


    }

}