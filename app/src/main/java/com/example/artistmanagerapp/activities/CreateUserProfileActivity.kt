package com.example.artistmanagerapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

class CreateUserProfileActivity : AppCompatActivity() {

    var userData : HashMap <String,Any> = HashMap()
    val ta = TransitionActivity()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user_profile)

        // Firebase stuff
        val auth = FirebaseAuth.getInstance()
        var user = auth.currentUser

        // Variables
        var loggedIn = ta.checkIfUserLoggedIn(user)

       if (loggedIn){
           initUserDatabaseRecord(user)
       } else {
           //zabij sb
       }

    }

    // ************************************ FUNCTIONS SECTION START ************************************

    fun mapTextFromField(map : Map<String, Any>, key : String, value : String){

    }

    fun initUserDatabaseRecord(user : FirebaseUser?) {
        userData.put("email_address", user?.email.toString())
        db.collection("users").document(user?.uid.toString()).set(userData).addOnSuccessListener {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Chuj", Toast.LENGTH_SHORT).show()
        }
    }

    // ************************************ FUNCTIONS SECTION ENDS ************************************

}
