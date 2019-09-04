package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.FirebaseConstants
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

    // Firebase basic stuff
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser
    val storageRef = FirebaseStorage.getInstance().reference
    val userId = user?.uid.toString()
    val userIdGlobal = user?.uid.toString()

    // Firebase paths
    val usersCollectionPath = db.collection(FirebaseConstants.USERS_COLLECTION_NAME)
    val artistPagesCollectionPath = db.collection(FirebaseConstants.ARTIST_PAGES_COLLECTION_NAME)
    val redeemCodesCollectionPath = db.collection("redeemCodes")
    val epkShareCodesCollectionPath = db.collection("shareCodes")
    val userPath = usersCollectionPath.document(userId)
    val userArtistPagesCollectionPath = userPath.collection("artistPages")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //exitIfUserNotLogged()
    }

    fun exitIfUserNotLogged(){
        if (user == null){
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

}
