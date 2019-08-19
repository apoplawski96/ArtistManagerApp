package com.example.artistmanagerapp.utils

import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.*
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions

object UsersHelper : BaseActivity() {

    val TAG = "Firebase - UsersHelper"
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
        usersCollectionPath.document(userId).get().addOnSuccessListener { document ->
            if (document.exists()){
                var user : User?
                val firstName : String? = document.get(c.FIRST_NAME).toString()
                val lastName : String? = document.get(c.LAST_NAME).toString()
                val pageRole : String? = document.get(c.PAGE_ROLE).toString()
                val artistRole : String? = document.get(c.ARTIST_ROLE).toString()
                val roleCategory : String? = document.get(c.ROLE_CATEGORY).toString()
                val email : String? = document.get(c.EMAIL).toString()
                val currentArtistPageId : String? = document.get(c.CURRENT_ARTIST_PAGE).toString()
                val profileCompletionStatus : String? = document.get(c.PROFILE_COMPLETION_STATUS).toString()
                val tasksCompleted : Int? = document.get(c.TASKS_COMPLETED) as Int?
                val eventsAttended : Int? = document.get(c.EVENTS_ATTENDED) as Int?
                val eventsCreated : Int? = document.get(c.EVENTS_CREATED) as Int?
                val assignmentsPending : Int? = document.get(c.ASSIGNMENTS_PENDING) as Int?

                Log.d(FIREBASE_TAG, "User data received")
                Log.d(FIREBASE_TAG, "UserData: $firstName, $lastName, $email")

                // Sending user data
                user = User(userId, firstName, lastName, pageRole, artistRole, roleCategory, null, currentArtistPageId, email, profileCompletionStatus, tasksCompleted, eventsAttended, eventsCreated, assignmentsPending)
                dataReceiver.receiveData(user, null)
            } else {
                dataReceiver.receiveData(null, null)
            }
        }
    }

    // ************************************************ READ FUNCTIONS/ ************************************************

    // ************************************************ \WRITE FUNCTIONS ************************************************

    fun setCurrentArtistPage(userId: String, artistPage : ArtistPage, presenter : UserInterfaceUpdater){
        usersCollectionPath.document(userId).set(mapOf("currentArtistPageId" to artistPage.artistPageId.toString()), SetOptions.merge()).addOnSuccessListener {
            Log.d("setCurrentArtistPage()", "Current ArtistPage set to ${artistPage.artistName} id: ${artistPage.artistPageId}")
            presenter.updateUI(const.ARTIST_PAGE_SELECTED, artistPage)
        }.addOnFailureListener {  }
    }

    fun removeCurrentArtistPage(userId : String, presenter : UserInterfaceUpdater){
        usersCollectionPath.document(userId).set(mapOf("currentArtistPageId" to null), SetOptions.merge()).addOnSuccessListener {
            presenter.updateUI(const.CURRENT_ARTIST_PAGE_REMOVED, null)
        }.addOnFailureListener {  }
    }

    // ************************************************ WRITE FUNCTIONS/ ************************************************

}