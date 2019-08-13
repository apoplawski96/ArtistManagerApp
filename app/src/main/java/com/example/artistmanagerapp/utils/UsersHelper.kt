package com.example.artistmanagerapp.utils

import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.*
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions

object UsersHelper : BaseActivity() {

    val c = FirebaseConstants
    val const = Constants

    // ************************************************ \READ FUNCTIONS ************************************************

    fun parseUsers (pathToUsersCollection : CollectionReference?, listener : UsersListListener){
        var usersOutput : ArrayList<User> = ArrayList()

        pathToUsersCollection?.get()
            ?.addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents){
                        // Need to work on assignees list later
                        usersOutput!!.add(
                            User(
                                document.get(c.ID).toString(),
                                document.get(c.FIRST_NAME).toString(),
                                document.get(c.LAST_NAME).toString()
                            )
                        )
                    }
                    listener.updateList(usersOutput)
                } else {

                }
            }
    }

    fun getCurrentArtistPage (userID : String, dataReceiver: DataReceiver){
        usersCollectionPath.document(userId).get().addOnSuccessListener { document ->
            if (document.exists()){
                val currentArtistPageId : String? = document.get(c.CURRENT_ARTIST_PAGE).toString()
                dataReceiver.receiveData(currentArtistPageId.toString(), null)
            } else {
                Log.d(FIREBASE_ERROR, "Artist page with provided pageId doesn't exist in user data")
            }
        }
    }

    fun getUserData (userId : String, dataReceiver: DataReceiver){
        usersCollectionPath.document(UsersHelper.userId).get().addOnSuccessListener { document ->
            if (document.exists()){
                var user : User? = null
                val firstName : String? = document.get(c.FIRST_NAME).toString()
                val lastName : String? = document.get(c.LAST_NAME).toString()
                val pageRole : String? = document.get(c.PAGE_ROLE).toString()
                val artistRole : String? = document.get(c.ARTIST_ROLE).toString()
                val email : String? = document.get(c.EMAIL).toString()
                val currentArtistPageId : String? = document.get(c.CURRENT_ARTIST_PAGE).toString()

                // Sending user data
                user = User(userId, firstName, lastName, pageRole, artistRole, null, currentArtistPageId, email)
                dataReceiver.receiveData(user, null)
            } else {
                Log.d(FIREBASE_ERROR, "Artist page with provided pageId doesn't exist in user data")
            }
        }
    }

    // ************************************************ READ FUNCTIONS/ ************************************************

    // ************************************************ \WRITE FUNCTIONS ************************************************

    fun setCurrentArtistPage(userId: String, pageId : String, presenter : UserInterfaceUpdater){
        usersCollectionPath.document(userId).set(mapOf("currentArtistPageId" to pageId), SetOptions.merge()).addOnSuccessListener {
            presenter.updateUI(const.ARTIST_PAGE_SELECTED)
        }.addOnFailureListener {  }
    }

    fun removeCurrentArtistPage(userId : String, presenter : UserInterfaceUpdater){
        usersCollectionPath.document(userId).set(mapOf("currentArtistPageId" to null), SetOptions.merge()).addOnSuccessListener {
            presenter.updateUI(const.CURRENT_ARTIST_PAGE_REMOVED)
        }.addOnFailureListener {  }
    }

    // ************************************************ WRITE FUNCTIONS/ ************************************************

}