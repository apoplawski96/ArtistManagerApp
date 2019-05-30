package com.example.artistmanagerapp.firebase

import android.util.Log
import android.widget.Toast
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.User
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

    fun createArtistPage(artistPage : ArtistPage, uiUpdater: UserInterfaceUpdater, userId: String){
        val pageId = artistPagesCollectionPath.document().id
        val userInfo = User(userId, "admin") // PAGE ROLE SOMEHOW DOESN'T ADD TO DATABASE
        var artistPageInfo = HashMap<String, Any>()

        // Adding record to artist_pages collection
        artistPagesCollectionPath.document(pageId).set(artistPage, SetOptions.merge()).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Artist page successfully created: $artistPage")
            uiUpdater.updateUI()
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }

        // Adding user record and admin info to artist page record
        artistPagesCollectionPath.document(pageId).collection("page_members").document(userId).set(userInfo, SetOptions.merge()).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Artist page successfully created: $artistPage")
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }

        artistPageInfo.put("artistPageName", artistPage.artistName.toString())
        artistPageInfo.put("pageRole", "admin")

        // Adding artist page link to user data
        userPath.collection("artist_pages").document(pageId).set(artistPageInfo, SetOptions.merge()).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Artist page info successfully added to user record: $artistPageInfo")
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }
    }
}