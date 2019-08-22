package com.example.artistmanagerapp.utils

import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.firebase.CommentsHelper
import com.example.artistmanagerapp.interfaces.TaskDetailPresenter
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.Comment
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.google.android.gms.common.api.Batch
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object TaskHelper : BaseActivity() {

    // Objects
    val c = Constants

    // ************************************************ \WRITE FUNCTIONS ************************************************

    // Adding Task object to a specified collection path
    fun addTask (task : Task, artistPage: ArtistPage?, user: User?, taskUpdater: TaskUpdater){
        // Capturing new task id
        val pathToTasksCollection = artistPagesCollectionPath.document(artistPage?.artistPageId.toString()).collection("tasks")
        val taskId = pathToTasksCollection.document().id
        val pathToCommentsCollection = pathToTasksCollection.document(taskId).collection("comments")

        // Adding task to task collection
        pathToTasksCollection.document(taskId).set(task).addOnSuccessListener {
            //taskUpdater.triggerUpdate()
        }.addOnFailureListener {
            taskUpdater.hideProgressBar()
        }

        // Adding init comment to comments collection
        val commentContent = "Task created by ${user?.getDisplayName()}"
        pathToCommentsCollection.document().set(Comment (commentContent, userId, Utils.getCurrentDate(), user?.getDisplayName())).addOnSuccessListener {
            taskUpdater.triggerUpdate()
        }.addOnFailureListener {

        }
    }

    // Changing "isCompleted" field of a Task in database with a specified value
    fun changeTaskCompletionStatus (taskUpdater: TaskUpdater, taskId: String?, completionStatus : Boolean?, pathToTasksCollection: CollectionReference){
        val taskPath = pathToTasksCollection.document(taskId.toString())
        taskPath.update(c.FB_TASK_ISCOMPLETED, completionStatus).addOnSuccessListener {
            taskUpdater.triggerUpdate()
        }.addOnFailureListener {

        }
    }

    // Setting tasks due date
    fun setTaskDueDate(taskId: String?, dueDate: String?, currentArtistPageId: String?, tasksUpdater: TaskUpdater){
        var updateData = HashMap <String, Any>()
        updateData.put("dueDate", dueDate.toString())

        artistPagesCollectionPath.document(currentArtistPageId.toString()).collection("tasks").document(taskId.toString()).set(updateData, SetOptions.merge()).addOnSuccessListener {
            tasksUpdater.onTaskDetailChanged(Constants.TASK_DUE_DATE_SET, null)
        }.addOnFailureListener {
            Log.d("FirebaseError", it.toString())
            tasksUpdater.onError(it.toString())
        }
    }

    fun deleteTask(taskId : String?, pathToTasksCollection: CollectionReference, tasksUpdater: TaskUpdater){
        pathToTasksCollection.document(taskId.toString()).delete().addOnSuccessListener {
            tasksUpdater.onTaskDeleted()
        }.addOnFailureListener {  }
    }

    fun setTaskDescription(taskId : String, pathToTasksCollection: CollectionReference?, descriptionContent : String){
        var updateData = HashMap <String, Any>()
        updateData.put("description", descriptionContent)

        pathToTasksCollection?.document(taskId)?.update(updateData)?.addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Data successfully updated: $updateData")
        }
    }

    fun assignMembers(taskId : String?, assigneesList : ArrayList<User?>, currentArtistPageId: String?, tasksUpdater: TaskUpdater){
        var batch = db.batch()
        val assigneesCollectionReference = artistPagesCollectionPath.document(currentArtistPageId.toString()).collection("tasks").document(taskId.toString()).collection("assignees")

        // Loading batch
        for (user in assigneesList){
            val docRef = assigneesCollectionReference.document(user?.id.toString())
            batch.set(docRef, user as User, SetOptions.merge())
        }

        batch.commit().addOnSuccessListener {
            tasksUpdater.onTaskDetailChanged(Constants.MEMBERS_ASSIGNED, null)
            Log.d("Batch", "Batch went ok")
        }.addOnFailureListener{
            Log.d("BATCH", "Batch failed: $it")
        }
    }


    // ************************************************ WRITE FUNCTIONS/ ***********************************************

    // ************************************************ \READ FUNCTIONS ************************************************

    fun returnTasksWhereEqualTo (pathToTasksCollection: CollectionReference, key : String, value : String) : Query {
        return pathToTasksCollection.whereEqualTo(key, value)
    }

    // Getting list of all tasks and returning it via interface
    fun parseTasks (pathToTasksCollection : CollectionReference?, taskUpdater : TaskUpdater){
        var tasksOutput : ArrayList<Task> = ArrayList()

        pathToTasksCollection?.get()
            ?.addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents){
                        // Need to work on assignees list later
                        tasksOutput!!.add(Task(
                            document.get("title").toString(),
                            document.id,
                            document.get("completed").toString().toBoolean(),
                            document.get("createdById").toString(),
                            document.get("description").toString(),
                            document.get("dueDate").toString(),
                            document.get("urgency").toString(),
                            null))
                    }
                    taskUpdater.updateTasks(tasksOutput)
                } else {
                    taskUpdater.onTasksListEmpty()
                }
            }
    }

    // Getting all data of a task specified with its ID
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

    // ************************************************ READ FUNCTIONS/ ************************************************

}