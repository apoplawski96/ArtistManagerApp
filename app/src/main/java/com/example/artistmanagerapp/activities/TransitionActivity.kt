package com.example.artistmanagerapp.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
        var loggedIn : Boolean

        // Checking if user is logged in
        if (user != null) {
            loggedIn = true
        } else {
            loggedIn = false
        }

        guideUser(loggedIn)

    }

    // ************************************ FUNCTIONS SECTION START ************************************

    fun guideUser(loggedIn : Boolean) {
        Timer().schedule(3000){
            if (loggedIn){
                // If user is logged in - we go to MainActivity
                val intent = Intent(context, MainActivity::class.java).apply {
                    //putExtra("asdasd", "asda")
                }
                startActivity(intent)
            } else {
                // If user is not logged in - we go to LoginActivity
                val intent = Intent(context, LoginActivity::class.java).apply {
                    //putExtra("asdasd", "asda")
                }
                startActivity(intent)
            }
        }
    }

    // ************************************ FUNCTIONS SECTION END **************************************

}
