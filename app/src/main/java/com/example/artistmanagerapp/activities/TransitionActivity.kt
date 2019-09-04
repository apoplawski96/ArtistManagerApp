package com.example.artistmanagerapp.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.interfaces.DataReceiver
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.FirebaseConstants
import com.example.artistmanagerapp.utils.UsersHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class TransitionActivity : BaseActivity(), DataReceiver {

    val context : Context = this
    val c = FirebaseConstants
    val tag = "TransitionActivity"

    // Bundle data objects
    var userInstance : User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)
        Log.d(ACTIVITY_WELCOME_TAG, "Welcome to TransitionActivity")
        Log.d("USER_ID", user?.uid.toString())


        UsersHelper.getUserData(userIdGlobal, this)
    }

    // ************************************ FUNCTIONS SECTION START ************************************

    override fun receiveData(data: Any?, mInterface: Any?) {
        if (data == null){
            checkUserDataAndGuide(null)
        } else {
            var user : User = data as User
            checkUserDataAndGuide(user)
        }

    }

    fun checkUserDataAndGuide(user : User?){
        var intent : Intent? = null

        if (user == null){
            Log.d(FIREBASE_TAG, "Db record not created - go to CreateUserProfileActivity and initialize record")
            intent = Intent(applicationContext, CreateUserProfileActivity::class.java).apply{
                putExtra("isDbRecordCreated", "false")
                putExtra(Constants.MODE_KEY, Constants.USER_PROFILE_CREATE_MODE)
            }
        } else {
            when (user.profileCompletionStatus.toString()){
                c.V_PROFILE_STATUS_COMPLETED -> {
                    Log.d(tag, "User profile completed, routing between MainActivity and SelectArtistPageActivity")
                    if ((user.currentArtistPageId==null) or (user.currentArtistPageId=="null")){
                        Log.d(tag, "CurrentPageId is a null, we go to SelectArtistPageActivity")
                        intent = Intent(this, SelectArtistPageActivity::class.java).apply {
                            putExtra(Constants.BUNDLE_USER_INSTANCE, user)
                        }
                    } else {
                        Log.d(tag, "We have CurrentPageId - go to MainActivity")
                        intent = Intent(this, MainActivity::class.java).apply {
                            putExtra(Constants.BUNDLE_USER_INSTANCE, user)
                        }
                    }

                }
                c.V_PROFILE_STATUS_STARTED -> {
                    Log.d(FIREBASE_TAG, "Db record initialized - go to CreateUserProfileActivity and complete missing profile info")
                    intent = Intent(applicationContext, CreateUserProfileActivity::class.java).apply{
                        putExtra("isDbRecordCreated", "true")
                        putExtra(Constants.MODE_KEY, Constants.USER_PROFILE_CREATE_MODE)
                        putExtra(Constants.BUNDLE_USER_INSTANCE, user)
                    }
                }
            }
        }

        this.finish()
        startActivity(intent)
    }



    // ************************************ FUNCTIONS SECTION END **************************************

}
