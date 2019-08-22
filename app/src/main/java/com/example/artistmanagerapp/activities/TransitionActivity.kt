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
import java.util.*
import kotlin.concurrent.schedule

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

        // Checking if user is logged in and saving the function response in the variable
        UsersHelper.getUserData(userIdGlobal, this)
        //checkIfUserIsNewAndGuide(db, user)
    }

    // ************************************ FUNCTIONS SECTION START ************************************

    fun checkIfUserIsNewAndGuide(db : FirebaseFirestore, user : FirebaseUser?) {
        // Creating a reference to the "users -> userId" path
        val docRef = db.collection(c.USERS_COLLECTION_NAME).document(userIdGlobal)

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
            var user : User? = null
            val firstName : String? = docSnapshot.get(UsersHelper.c.FIRST_NAME).toString()
            val lastName : String? = docSnapshot.get(UsersHelper.c.LAST_NAME).toString()
            val pageRole : String? = docSnapshot.get(UsersHelper.c.PAGE_ROLE).toString()
            val artistRole : String? = docSnapshot.get(UsersHelper.c.ARTIST_ROLE).toString()
            val email : String? = docSnapshot.get(UsersHelper.c.EMAIL).toString()
            val currentArtistPageId : String? = docSnapshot.get(UsersHelper.c.CURRENT_ARTIST_PAGE).toString()

            Log.d(FIREBASE_TAG, "Gettin user data in TransitionActivity: $firstName, $lastName, $email")

            Log.d(FIREBASE_TAG, "Success")
            // If docSnahshot doesn't exist - database record is not yet created, so we pass a "false" value and create it in the next activity
            if (!docSnapshot.exists()){
                Log.d(FIREBASE_TAG, "Db record not created - go to CreateUserProfileActivity and initialize record")
                val intent = Intent(applicationContext, CreateUserProfileActivity::class.java).apply{
                    putExtra("isDbRecordCreated", "false")
                    putExtra(Constants.MODE_KEY, Constants.USER_PROFILE_CREATE_MODE)
                }
                startActivity(intent)
            } else {
                Log.d(FIREBASE_TAG, "Doc reference found")
                // Getting and checking the "profileCompletionStatus" value
                var completionStatus = docSnapshot.getString(c.PROFILE_COMPLETION_STATUS)
                Log.d(FIREBASE_TAG, "$completionStatus")

                // If completionStatus is completed - we search for artist page link
                if (completionStatus.equals(c.V_PROFILE_STATUS_COMPLETED)){
                    Log.d(tag, "User profile completed - go to SelectArtistPageActivity")
                    val intent = Intent(this, SelectArtistPageActivity::class.java)
                    startActivity(intent)
                } else if (completionStatus.equals(c.V_PROFILE_STATUS_STARTED)){
                    Log.d(FIREBASE_TAG, "Db record initialized - go to CreateUserProfileActivity and complete missing profile info")
                    val intent = Intent(applicationContext, CreateUserProfileActivity::class.java).apply{
                        putExtra("isDbRecordCreated", "true")
                        putExtra(Constants.MODE_KEY, Constants.USER_PROFILE_CREATE_MODE)
                    }
                    this.finish()
                    startActivity(intent)
                }

            }

        }.addOnFailureListener{
            Log.d(FIREBASE_ERROR, "Failure")
        }
    }

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
