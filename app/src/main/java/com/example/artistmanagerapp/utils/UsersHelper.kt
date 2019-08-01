package com.example.artistmanagerapp.utils

import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.interfaces.UsersListListener
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.google.firebase.firestore.CollectionReference

class UsersHelper : BaseActivity() {

    val c = FirebaseConstants

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

    fun updateCurrentArtistPage(userId: String, pageId : String){
        usersCollectionPath.document(userId).update(mapOf("currentArtistPageId" to pageId))
    }

}