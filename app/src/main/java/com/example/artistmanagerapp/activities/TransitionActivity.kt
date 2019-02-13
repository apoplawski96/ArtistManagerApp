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

        // Firebase stuff
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        var user = auth.currentUser

        // Checking if user is logged in and saving the function response in the variable
        checkIfUserIsNewAndGuide(db, user)

    }

    // ************************************ FUNCTIONS SECTION START ************************************

    fun checkIfUserIsNewAndGuide(db : FirebaseFirestore, user : FirebaseUser?) {
        val docRef = db.collection("users").document(user?.uid.toString())

        docRef.get().addOnSuccessListener {
            var completionStatus = it.getString("profileCompletionStatus")
            if (completionStatus.equals("completed")){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, CreateUserProfileActivity::class.java).apply{
                    putExtra("isDbRecordCreated", "true")
                }
                startActivity(intent)
            }
        }.addOnFailureListener{
            val intent = Intent(this, TransitionActivity::class.java).apply{
                putExtra("isDbRecordCreated", "false")
            }
            startActivity(intent)
        }
    }

    // ************************************ FUNCTIONS SECTION END **************************************

}
