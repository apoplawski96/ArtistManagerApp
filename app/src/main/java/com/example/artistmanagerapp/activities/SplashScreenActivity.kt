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
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : BaseActivity() {

    // Others
    val context : Context = this

    val this2 = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Log.d(ACTIVITY_WELCOME_TAG, "Welcome to SplashScreenActivity")

        val auth = FirebaseAuth.getInstance()
        var user = auth.currentUser

        // Checking if user is logged in and saving the function response in the variable
        var isLoggedIn = checkIfUserLoggedIn(user)

        guideUserDelayed(isLoggedIn)
    }

    fun guideUserDelayed(loggedIn : Boolean) {
        var mIntent : Intent? = null

        Timer().schedule(3000){
            if (loggedIn){
                // If user is logged in - we go to TransitionActivity
                Log.d(FIREBASE_TAG, "User is logged in - we go to TransitionActivity")
                mIntent = Intent(context, TransitionActivity::class.java).apply {
                    //putExtra("asdasd", "asda")
                }
            } else {
                // If user is not logged in - we go to LoginActivity
                Log.d(FIREBASE_TAG, "User is not logged in - we go to LoginActivity")
                mIntent = Intent(context, LoginActivity::class.java).apply {
                    //putExtra("asdasd", "asda")
                }
            }
            this2.finish()
            startActivity(mIntent)
        }
    }

    fun checkIfUserLoggedIn(user : FirebaseUser?) : Boolean{
        return user != null
    }

}
