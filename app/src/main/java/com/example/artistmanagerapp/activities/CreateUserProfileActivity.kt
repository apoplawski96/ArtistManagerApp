package com.example.artistmanagerapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.artistmanagerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateUserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user_profile)

        // Firebase stuff
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        var user = auth.currentUser

        // Variables
        var loggedIn : Boolean

        // Checking if user is logged in
        if (user != null) {
            loggedIn = true
        } else {
            loggedIn = false
        }



    }

    // ************************************ FUNCTIONS SECTION START ************************************


    // ************************************ FUNCTIONS SECTION ENDS ************************************

}
