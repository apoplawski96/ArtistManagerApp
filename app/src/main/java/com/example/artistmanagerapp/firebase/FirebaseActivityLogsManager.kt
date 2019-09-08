package com.example.artistmanagerapp.firebase

import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.models.*
import com.example.artistmanagerapp.utils.Utils

object FirebaseActivityLogsManager : BaseActivity(){

    enum class ActivityLogCategory{
        PAGE_CREATED,
        TASK_CREATED,
        EVENT_CREATED,
        MEMBER_JOINED,
        MEMBER_REMOVED,
        TASK_RESOLVED,
        TASK_REMOVED,
        TASK_DUE_DATE_SET,
        COMMENT_CREATED,
        TASK_MEMBERS_ASSIGNED
    }

    enum class SortParameters{
        ASCENDING,
        DESCENDING
    }

    fun createActivityLog(user : User?, pageId : String, data : Any?, option : ActivityLogCategory){

        val pageActivityLogCollection = artistPagesCollectionPath.document(pageId).collection("activityLogs")
        var logDescription : String? = null

        when (option){
            ActivityLogCategory.PAGE_CREATED -> {
                logDescription = "ArtistPage created by ${user!!.getDisplayName()}"
            }
            ActivityLogCategory.TASK_CREATED -> {
                val task = data as Task
                logDescription = "Task ${task.title} created by ${user?.getDisplayName()}"
            }
            ActivityLogCategory.EVENT_CREATED -> {
                val event = data as Event
                logDescription = "Event ${event.eventTitle} created b ${user?.getDisplayName()}"
            }
            ActivityLogCategory.MEMBER_JOINED -> {
                logDescription = "A new member ${user?.getDisplayName()} have joined to a team!"
            }
            ActivityLogCategory.MEMBER_REMOVED -> {
                val memberRemoved = data as User
                logDescription = "${memberRemoved.getDisplayName()} has been removed from the team by ${user?.getDisplayName()}"
            }
            ActivityLogCategory.TASK_RESOLVED -> {
                val task = data as Task
                logDescription = "Task ${task.title} has been completed by ${user?.getDisplayName()}"
            }
            ActivityLogCategory.TASK_REMOVED -> {
                val task = data as Task
                logDescription = "Task ${task.title} has been removed by ${user?.getDisplayName()}"
            }
            ActivityLogCategory.TASK_DUE_DATE_SET -> {
                val task = data as Task
                logDescription = "Due date of Task: ${task.title} has been set to ${task.dueDate} by ${user!!.getDisplayName()}"
            }
            ActivityLogCategory.TASK_MEMBERS_ASSIGNED -> {
                val assigneesList = data as ArrayList<User>
                logDescription = "${assigneesList.size} members has been assigned to a task"
            }
            ActivityLogCategory.COMMENT_CREATED -> {
                logDescription = "A new comment has been added by ${user!!.getDisplayName()}"
            }
        }

        val logItem = ActivityLog(
            user?.getDisplayName(),
            Utils.getCurrentDateShort(),
            Utils.getCurrentTimeShort(),
            null,
            logDescription,
            user?.getAcronym(),
            pageId,
            user?.id)

        pageActivityLogCollection.add(logItem).addOnSuccessListener {
            Log.d("ACTIVITY_LOGS", "Log added successfullu")
        }.addOnFailureListener {
            Log.d("ACTIVITY_LOGS", it.toString())
        }
    }

    fun parseActivityLogs(pageId : String, presenter : ActivityLogsPresenter ){
        var activityLogsList : ArrayList<ActivityLog> = ArrayList()
        val activityLogsCollectionPath = artistPagesCollectionPath.document(pageId).collection("activityLogs")

        activityLogsCollectionPath.get().addOnSuccessListener { documents ->

            if (!documents.isEmpty){
                for (doc in documents){
                    val displayName = doc.get("userDisplayName").toString()
                    val dateCreated = doc.get("dateCreated").toString()
                    val timeCreated = doc.get("timeCreated").toString()
                    val eventCategory = doc.get("eventCategory").toString()
                    val logDescription = doc.get("logDescription").toString()
                    val userAcronym = doc.get("userAcronym").toString()
                    val connectedPageId = doc.get("connectedPageId").toString()
                    val createdById = doc.get("createdById").toString()
                    activityLogsList.add(ActivityLog(displayName, dateCreated, timeCreated, eventCategory, logDescription, userAcronym, connectedPageId, createdById))
                }
                presenter.showLogs(activityLogsList)
            } else {
                Log.d("error", "activity logs list empty")
            }
        }
    }

    fun filterActivityLogs(inputList : ArrayList<ActivityLog>, parameter : Any?) : ArrayList<ActivityLog>{
        var filteredList = ArrayList<ActivityLog>()

        when (parameter){
            is User -> {
                for (log in inputList){
                    if (log.createdById == parameter.id){
                        filteredList.add(log)
                    }
                }
            }
        }

        return filteredList
    }

    fun sortActivityLogsByDate(inputList: ArrayList<ActivityLog>, parameter : SortParameters){

    }

    interface ActivityLogsPresenter{
        fun showLogs(activityLogs : ArrayList<ActivityLog>)
    }
}