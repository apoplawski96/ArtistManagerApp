package com.example.artistmanagerapp.firebase

import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.constants.RealTimeUpdateType
import com.example.artistmanagerapp.interfaces.FirebaseRealTimeUpdatesPresenter
import kotlin.math.roundToInt

object FirebaseRealTimeUpdatesManager : BaseActivity(){

    val TAG = "FIREBASE_REALTIME_UPDATES_MANAGER"

    fun attachNewTasksCounterListener(pageId : String, updatesPresenter: FirebaseRealTimeUpdatesPresenter){
        artistPagesCollectionPath.document(pageId).addSnapshotListener { docSnapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (docSnapshot != null && docSnapshot.exists()) {
                Log.d(TAG, "Listen succeeded, current data: ${docSnapshot.data}")
                val newTasksCounter = docSnapshot.getDouble("newTasks")!!
                if (newTasksCounter != null){
                    updatesPresenter.presentNewData(newTasksCounter.roundToInt(), RealTimeUpdateType.NEW_TASKS)
                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    fun attachUserPendingAssignmentsListener(userId : String?, updatesPresenter: FirebaseRealTimeUpdatesPresenter){
        usersCollectionPath.document(userId!!).addSnapshotListener { docSnapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (docSnapshot != null && docSnapshot.exists()){
                Log.d(TAG, "Listen succeeded, current data: ${docSnapshot.data}")

                if (docSnapshot.get("tasksAssigned") != null){
                    val tasksAssignedList = docSnapshot.get("tasksAssigned") as ArrayList<String>
                    updatesPresenter.presentNewData(tasksAssignedList.size, RealTimeUpdateType.PENDING_ASSIGNMENTS)
                } else {
                    Log.d(TAG, "Field tasksAssigned does not exist")
                    updatesPresenter.presentNewData(0, RealTimeUpdateType.PENDING_ASSIGNMENTS)
                }
            } else {
                Log.d(TAG, "Document does not exist")
            }
        }
    }
}