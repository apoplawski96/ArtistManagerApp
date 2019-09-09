package com.example.artistmanagerapp.firebase

import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Transaction

object FirebaseStatisticsHelper : BaseActivity(){

    fun incrementValue (docRef : DocumentReference, fieldName : String){
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val newCounter = snapshot.getDouble(fieldName)!! + 1
            transaction.update(docRef, fieldName, newCounter)
        }.addOnSuccessListener {
            Log.d("TRANSACTION", "Sccessfully incremented")
        }.addOnFailureListener {
            Log.d("TRANSACTION", it.toString())
        }
    }

    fun decrementValue (docRef : DocumentReference, fieldName : String){
        db.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            var newCounter = snapshot.getDouble(fieldName)!! - 1
            if (newCounter<0){ newCounter = 0.0 }
            transaction.update(docRef, fieldName, newCounter)
        }.addOnSuccessListener {
            Log.d("TRANSACTION", "Successfully decremented")
        }.addOnFailureListener {
            Log.d("TRANSACTION", it.toString())
        }
    }

    fun readStats(userId : String?, pageId : String?, fieldValue : String, statsReceiver : StatisticsReceiver){
        when (fieldValue){
            "tasksAssigned" -> {
                usersCollectionPath.document(userId!!).get().addOnSuccessListener { doc ->
                    val tasksAssignedList = doc.get(fieldValue) as ArrayList<String>
                    statsReceiver.onStatsReceived(tasksAssignedList.size, fieldValue)
                }
            }
        }
    }

    interface StatisticsReceiver{
        fun onStatsReceived(data : Any?, option : String?)
    }

}