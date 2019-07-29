package com.example.artistmanagerapp.utils

import android.util.Log
import com.example.artistmanagerapp.interfaces.TaskDetailPresenter
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import java.util.*
import kotlin.collections.ArrayList

object TaskHelper {

    // Objects
    val c = Constants

    // ********** Adding Task object to a specified collection path **********
    fun addTask (task : Task, pathToTasksCollection : CollectionReference, taskUpdater: TaskUpdater){
        pathToTasksCollection.document().set(task).addOnSuccessListener {
            taskUpdater.triggerUpdate()
        }.addOnFailureListener {
            taskUpdater.hideProgressBar()
        }
    }

    // ********** Changing "isCompleted" field of a Task in database with a specified value **********
    fun changeTaskCompletionStatus (taskUpdater: TaskUpdater, taskId: String?, completionStatus : Boolean?, pathToTasksCollection: CollectionReference){
        val taskPath = pathToTasksCollection.document(taskId.toString())
        taskPath.update(c.FB_TASK_ISCOMPLETED, completionStatus).addOnSuccessListener {
            taskUpdater.triggerUpdate()
        }.addOnFailureListener {

        }
    }

    fun returnTasksWhereEqualTo (pathToTasksCollection: CollectionReference, key : String, value : String) : Query {
        return pathToTasksCollection.whereEqualTo(key, value)
    }

    // **********  Getting list of all tasks and returning it via interface **********
    fun parseTasks (pathToTasksCollection : CollectionReference, taskUpdater : TaskUpdater){
        var tasksOutput : ArrayList<Task> = ArrayList()

        pathToTasksCollection.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents){
                        // Need to work on assignees list later
                        tasksOutput!!.add(Task(
                            document.get("title").toString(),
                            document.id,
                            document.get("completed").toString().toBoolean(),
                            document.get("createdById").toString(),
                            null,
                            document.get("urgency").toString(),
                            null))
                    }
                    taskUpdater.updateTasks(tasksOutput)
                } else {

                }
            }
    }

    // ********** Getting all data of a task specified with its ID **********
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

    fun sortTasks (isCompleted: Boolean, inputList : ArrayList<Task>) : ArrayList<Task>{
        var outputList : ArrayList<Task> = ArrayList()

        for (item in inputList){
            if (item.isCompleted == isCompleted){
                outputList?.add(item)
            }
        }

        return outputList
    }

}