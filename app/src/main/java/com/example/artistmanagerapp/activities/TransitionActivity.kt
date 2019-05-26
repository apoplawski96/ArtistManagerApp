package com.example.artistmanagerapp.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.concurrent.schedule

class TransitionActivity : BaseActivity() {

    val context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)
        Log.d(ACTIVITY_WELCOME_TAG, "Welcome to TransitionActivity")
        Log.d("USER_ID", user?.uid.toString())

        // Checking if user is logged in and saving the function response in the variable
        checkIfUserIsNewAndGuide(db, user)
    }

    // ************************************ FUNCTIONS SECTION START ************************************

    fun checkIfUserIsNewAndGuide(db : FirebaseFirestore, user : FirebaseUser?) {
        // Creating a reference to the "users -> userId" path
        val docRef = db.collection("users").document(user?.uid.toString())
        Log.d(FIREBASE_TAG, "Method entered")

        // ** NOTE **
        // We have 3 cases here
        // 1 - Database record of an user is not yet initialized
        //     -> CreateUserProfileActivity, isDbRecordCreated == false
        // 2 - Database record of an user is initialized but profile completion status is not yet completed
        //     -> CreateUserProfileActivity, isDbRecordCreated == true
        // 3 - User profile is completed
        //     -> CheckArtistPageLinkActivity

        // Getting a DocSnaphot from a path reference
        docRef.get().addOnSuccessListener { docSnapshot ->
            Log.d(FIREBASE_ERROR, "Success")
            // If docSnahshot doesn't exist - database record is not yet created, so we pass a "false" value and create it in the next activity
            if (!docSnapshot.exists()){
                Log.d(FIREBASE_TAG, "Db record not created - go to CreateUserProfileActivity and initialize record")
                val intent = Intent(applicationContext, CreateUserProfileActivity::class.java).apply{
                    putExtra("isDbRecordCreated", "false")
                }
                startActivity(intent)
            } else {
                // Getting and checking the "profileCompletionStatus" value
                var completionStatus = docSnapshot.getString("profile_completion_status")

                // If completionStatus is completed - we search for artist page link
                if (completionStatus.equals("completed")){
                    Log.d(FIREBASE_TAG, "User profile completed - go to CheckArtistPageLinkActivity")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else if (completionStatus.equals("started")){
                    Log.d(FIREBASE_TAG, "Db record initialized - go to CreateUserProfileActivity and complete missing profile info")
                    val intent = Intent(applicationContext, CreateUserProfileActivity::class.java).apply{
                        putExtra("isDbRecordCreated", "true")
                    }
                    startActivity(intent)
                }

            }

        }.addOnFailureListener{
            Log.d(FIREBASE_ERROR, "Failure")
        }
    }

    // ************************************ FUNCTIONS SECTION END **************************************

}
