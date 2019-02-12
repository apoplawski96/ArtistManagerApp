package com.example.artistmanagerapp.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*
import kotlin.concurrent.schedule

class TransitionActivity : AppCompatActivity() {

    val context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)

        val auth = FirebaseAuth.getInstance()
        var user = auth.currentUser
        val intent : Intent = Intent()

        // Checking if user is logged in and saving the function response in the variable
        var isLoggedIn = checkIfUserLoggedIn(user)
        var isUserNew : Boolean = true
        guideUser(isLoggedIn, isUserNew)
    }

    // ************************************ FUNCTIONS SECTION START ************************************

    fun guideUser(loggedIn : Boolean, userNew : Boolean) {
        if (loggedIn){
            // If the user haven't finished filling his profile info - we send him there
            if (userNew){
                val intent = Intent(context, CreateUserProfileActivity::class.java).apply {
                    //putExtra("asdasd", "asda")
                }
                startActivity(intent)
            // If the user already finished filling his profile - he goes to Main Activity
            } else {
                val intent = Intent(context, MainActivity::class.java).apply {
                    //putExtra("asdasd", "asda")
                }
                startActivity(intent)
            }

        } // If user is not logged in - we go to LoginActivity
        else {
            val intent = Intent(context, LoginActivity::class.java).apply {
                //putExtra("asdasd", "asda")
            }
            startActivity(intent)
        }
    }

    fun checkIfUserIsNew() {

    }

    fun checkIfUserLoggedIn(user : FirebaseUser?) : Boolean{
        if (user != null){
            return true
        } else {
            return false
        }
    }

    // ************************************ FUNCTIONS SECTION END **************************************

}
