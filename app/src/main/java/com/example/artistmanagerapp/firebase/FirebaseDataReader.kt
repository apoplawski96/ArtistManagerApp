package com.example.artistmanagerapp.firebase

import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.UserDataPresenter
import com.example.artistmanagerapp.models.Person

class FirebaseDataReader : BaseActivity () {

    fun getUserData(userID : String, userDataPresenter: UserDataPresenter){
        lateinit var person : Person
        Log.d("chuj", "tujeste")
        db.collection("users").document("perfectUser").get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()){
                    var firstName = documentSnapshot.getString("first_name").toString()
                    var lastName = documentSnapshot.getString("last_name").toString()
                    var artistRole = documentSnapshot.getString("artist_role").toString()
                    var pageRole = documentSnapshot.getString("page_role").toString()
                    person = Person(firstName, lastName, artistRole, pageRole)
                }
                Log.d("chuj", documentSnapshot.data.toString())
                userDataPresenter.showUserData(person)
            }.addOnFailureListener { exception ->
                Log.d("ERROR", "get failed with ", exception)
            }
    }

}