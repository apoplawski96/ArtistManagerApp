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

    // Firebase basic stuff
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser
    val storageRef = FirebaseStorage.getInstance().reference


    // Firebase paths
    val userPath = db.collection("users").document(user?.uid.toString())
    val artistsCollectionPath = userPath.collection(R.string.firestore_artistpages_collection.toString())
    val avatarPath = storageRef.child("avatars/perfectUser/avatar.jpg")

    // Perfect stuff
    val perfectUserID = "perfectUser"
    val perfectUserPath = db.collection("users").document("perfectUser")
    val perfectArtistPagePath = db.collection("artist_pages").document("perfect_artistpage_id")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        Utils.exitIfUserNotLogged()
    }

    override fun onResume() {
        super.onResume()

        Utils.exitIfUserNotLogged()
    }

}
