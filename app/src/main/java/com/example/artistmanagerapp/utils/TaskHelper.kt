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

    fun addTask (task : Task, pathToTasksCollection : CollectionReference){
        pathToTasksCollection.document().set(task)
    }

    fun changeTaskCompletionStatus (taskId: String, completionStatus : Boolean, pathToTasksCollection: CollectionReference){
        val taskPath = pathToTasksCollection.document(taskId)

        taskPath.update("isCompleted", completionStatus)
    }

    fun returnTasksWhereEqualTo (pathToTasksCollection: CollectionReference, key : String, value : String) : Query {
        return pathToTasksCollection.whereEqualTo(key, value)
    }

    fun parseTasks (pathToTasksCollection : CollectionReference, taskUpdater : TaskUpdater){
        var tasksOutput : ArrayList<Task> = ArrayList()

        pathToTasksCollection.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents){
                        // Need to change isCompleted later
                        tasksOutput!!.add(Task(
                            document.get("title").toString(),
                            document.id,
                            document.get("isCompleted").toString().toBoolean()))
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
                    // Need to change isCompleted later
                    outputTask = Task (taskTitle, taskId, true)
                }
                taskDetailPresenter.showTask(outputTask)
        }


    }

}