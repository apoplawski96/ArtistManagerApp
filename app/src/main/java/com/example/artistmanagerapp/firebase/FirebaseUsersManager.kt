package com.example.artistmanagerapp.firebase

import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.constants.Constants
import com.example.artistmanagerapp.interfaces.*
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.constants.FirebaseConstants
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions

object FirebaseUsersManager : BaseActivity() {

    val TAG = "Firebase - FirebaseUsersManager"
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
                                document.get(FirebaseConstants.FIRST_NAME).toString(),
                                document.get(FirebaseConstants.LAST_NAME).toString(),
                                document.get(FirebaseConstants.ARTIST_ROLE).toString(),
                                document.get(FirebaseConstants.ROLE_CATEGORY).toString()
                            )
                        )
                    }
                    listener.updateList(usersOutput)
                } else {

                }
            }
    }

    fun parsePageMembers (pathToUsersCollection : CollectionReference?, listener : UsersListListener){
        var usersOutput : ArrayList<User> = ArrayList()

        pathToUsersCollection?.get()
            ?.addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents){
                        // Need to work on assignees list later
                        usersOutput!!.add(
                            User(
                                document.get(FirebaseConstants.FIRST_NAME).toString(),
                                document.get(FirebaseConstants.LAST_NAME).toString(),
                                document.get(FirebaseConstants.ARTIST_ROLE).toString(),
                                document.get(FirebaseConstants.CURRENT_ARTIST_PAGE).toString(),
                                document.get(FirebaseConstants.ROLE_CATEGORY).toString(),
                                document.get(FirebaseConstants.ID).toString()
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
                val currentArtistPageId : String? = document.get(FirebaseConstants.CURRENT_ARTIST_PAGE).toString()
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
                val firstName : String? = document.get(FirebaseConstants.FIRST_NAME).toString()
                val lastName : String? = document.get(FirebaseConstants.LAST_NAME).toString()
                val pageRole : String? = document.get(FirebaseConstants.PAGE_ROLE).toString()
                val artistRole : String? = document.get(FirebaseConstants.ARTIST_ROLE).toString()
                val roleCategory : String? = document.get(FirebaseConstants.ROLE_CATEGORY).toString()
                val email : String? = document.get(FirebaseConstants.EMAIL).toString()
                val currentArtistPageId : String? = document.get(FirebaseConstants.CURRENT_ARTIST_PAGE).toString()
                val profileCompletionStatus : String? = document.get(FirebaseConstants.PROFILE_COMPLETION_STATUS).toString()
                val tasksCompleted : Int? = document.get(FirebaseConstants.TASKS_COMPLETED) as Int?
                val eventsAttended : Int? = document.get(FirebaseConstants.EVENTS_ATTENDED) as Int?
                val eventsCreated : Int? = document.get(FirebaseConstants.EVENTS_CREATED) as Int?
                val assignmentsPending : Int? = document.get(FirebaseConstants.ASSIGNMENTS_PENDING) as Int?

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

    fun changeUserAccessRights(){

    }

}