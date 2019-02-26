package com.example.artistmanagerapp.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.concurrent.schedule

class TransitionActivity : AppCompatActivity() {

    val context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)

        // Firebase stuff asdasd
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        var user = auth.currentUser

        // Checking if user is logged in and saving the function response in the variable
        checkIfUserIsNewAndGuide(db, user)

    }

    // ************************************ FUNCTIONS SECTION START ************************************

    fun checkIfUserIsNewAndGuide(db : FirebaseFirestore, user : FirebaseUser?) {
        // Creating a reference to the "users -> userId" path
        val docRef = db.collection("users").document(user?.uid.toString())

        // Getting a DocSnaphot from a path reference
        docRef.get().addOnSuccessListener { docSnapshot ->
            // If docSnahshot doesn't exist - database record is not yet created, so we pass a "false" value and create it in the next activity
            if (!docSnapshot.exists()){
                val intent = Intent(applicationContext, CreateUserProfileActivity::class.java).apply{
                    putExtra("isDbRecordCreated", "false")
                }
                startActivity(intent)
            } else {
                // Getting and checking the "profileCompletionStatus" value
                var completionStatus = docSnapshot.getString("profileCompletionStatus")

                // If completionStatus is completed - we can go to MainActivity
                if (completionStatus.equals("completed")){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else if (completionStatus.equals("started")){
                    val intent = Intent(applicationContext, CreateUserProfileActivity::class.java).apply{
                        putExtra("isDbRecordCreated", "true")
                    }
                    startActivity(intent)
                }

            }

        }.addOnFailureListener{

        }
    }

    // ************************************ FUNCTIONS SECTION END **************************************

}
