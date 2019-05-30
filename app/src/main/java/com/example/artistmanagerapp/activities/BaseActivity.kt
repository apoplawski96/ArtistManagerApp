package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

abstract class BaseActivity : AppCompatActivity() {

    // Tags
    val FIREBASE_TAG : String = "FIREBASE"
    val FIREBASE_ERROR : String = "FIREBASE_ERROR"
    val ACTIVITY_WELCOME_TAG : String = "ACTIVITY_START"
    val FIREBASE_STORAGE_TAG : String = "FIREBASE_STORAGE"

    // Numeric values
    val USER_PROFILE_INPUTS_COUNTER : Int = 2

    // Firebase basic stuff
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser
    val storageRef = FirebaseStorage.getInstance().reference
    val userId = user?.uid.toString()

    // Firebase paths
    val usersPath = db.collection("users")
    val userPath = db.collection("users").document(user?.uid.toString())
    val artistsCollectionPath = userPath.collection("artist_pages")
    val avatarPath = storageRef.child("avatars/perfectUser/avatar.jpg")

    // Perfect stuff
    val perfectUserID = "perfectUser"
    val perfectUserPath = db.collection("users").document("perfectUser")
    val perfectArtistPagePath = db.collection("artist_pages").document("perfect_artistpage_id")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_base)

        exitIfUserNotLogged()
    }

    override fun onResume() {
        super.onResume()

        //exitIfUserNotLogged()
    }

    fun exitIfUserNotLogged(){
        /*if (user == null){
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }*/
    }

}
