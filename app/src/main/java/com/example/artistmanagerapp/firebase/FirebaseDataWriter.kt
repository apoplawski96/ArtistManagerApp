package com.example.artistmanagerapp.firebase

import android.widget.Toast
import com.example.artistmanagerapp.activities.BaseActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions

class FirebaseDataWriter : BaseActivity(){

    fun addUserDataToDb(collectionPath : CollectionReference, dataMap : HashMap<String, Any>, userId : String){
        collectionPath.document(userId).set(dataMap, SetOptions.merge()).addOnSuccessListener {

        }.addOnFailureListener {

        }
    }

}