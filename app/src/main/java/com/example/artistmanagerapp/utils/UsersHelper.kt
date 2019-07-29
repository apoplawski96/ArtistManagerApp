package com.example.artistmanagerapp.utils

import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.interfaces.UsersListListener
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.google.firebase.firestore.CollectionReference

class UsersHelper {

    fun parseUsers (pathToUsersCollection : CollectionReference?, listener : UsersListListener){
        var usersOutput : ArrayList<User> = ArrayList()

        pathToUsersCollection?.get()
            ?.addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents){
                        // Need to work on assignees list later
                        usersOutput!!.add(
                            User(
                                document.get("id").toString(),
                                document.get("firstName").toString(),
                                document.get("lastName").toString()
                            )
                        )
                    }
                    listener.updateList(usersOutput)
                } else {

                }
            }
    }

}