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

class SplashScreenActivity : AppCompatActivity() {

    val context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val auth = FirebaseAuth.getInstance()
        var user = auth.currentUser
        val intent : Intent = Intent()

        // Checking if user is logged in and saving the function response in the variable
        var isLoggedIn = checkIfUserLoggedIn(user)
        guideUserDelayed(isLoggedIn)

    }

    fun guideUserDelayed(loggedIn : Boolean) {
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

    fun checkIfUserLoggedIn(user : FirebaseUser?) : Boolean{
        if (user != null){
            return true
        } else {
            return false
        }
    }

}
