package com.example.artistmanagerapp.firebase

import android.util.Log
import com.example.artistmanagerapp.activities.ActivityLogsActivity
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.models.*
import com.example.artistmanagerapp.utils.Utils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

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

    fun parseActivityLogs(pageId : String, presenter : ActivityLogsPresenter){
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
                presenter.receiveLogs(activityLogsList)
            } else {
                Log.d("error", "activity logs list empty")
            }
        }
    }

    fun filterActivityLogs(inputList : ArrayList<ActivityLog>, parameter : ActivityLogsActivity.FilterParameter, userId : String?) : ArrayList<ActivityLog>{
        var filteredList = ArrayList<ActivityLog>()

        when (parameter){
            ActivityLogsActivity.FilterParameter.ONLY_ME -> {
                for (log in inputList){
                    if (log.createdById == userId){
                        filteredList.add(log)
                    }
                }
            }
            ActivityLogsActivity.FilterParameter.TEAM -> {
                filteredList = inputList
            }
        }

        return filteredList
    }

    fun sortActivityLogsByDate(inputList: ArrayList<ActivityLog>, parameter : ActivityLogsActivity.SortParameter) : ArrayList<ActivityLog>{
        var datesList : ArrayList<String> = ArrayList()
        var sortedDatesList : List<String>
        var sortedLogsList : ArrayList<ActivityLog> = ArrayList()

        // Setting up comparator
        val cmp = compareBy<String> { LocalDateTime.parse(it, DateTimeFormatter.ofPattern("dd.MM.yy | HH:mm")) }

        // Creating dates list out of input ActivityLogs list
        for (log in inputList){
            datesList.add("${log.dateCreated} | ${log.timeCreated}")
        }

        // Sorting dates list
        sortedDatesList = datesList.sortedWith(cmp)

        // Loop over dates list
        for (date in sortedDatesList){
            Log.d("SORTED-DATES", "$date + ${sortedDatesList.indexOf(date)}")

            // Loop over input logs list
            for (log in inputList){
                val dateFullFormat = "${log.dateCreated} | ${log.timeCreated}"
                if (dateFullFormat == date){
                    sortedLogsList.add(log)
                    inputList.remove(log)
                    break
                }
            }

        }

        if (parameter == ActivityLogsActivity.SortParameter.DESCENDING) sortedLogsList.reverse()
        return sortedLogsList
    }

    interface ActivityLogsPresenter{
        fun receiveLogs(activityLogs : ArrayList<ActivityLog>)
    }
}