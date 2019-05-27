package com.example.artistmanagerapp.firebase

import android.util.Log
import android.widget.Toast
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions

class FirebaseDataWriter : BaseActivity(){

    fun addUserDataToDbAndUpdateUi(collectionPath : CollectionReference, dataMap : HashMap<String, Any>, userId : String, uiUpdater: UserInterfaceUpdater){
        collectionPath.document(userId).set(dataMap, SetOptions.merge()).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Data successfully added: $dataMap")
            uiUpdater.updateUI()
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }
    }

}