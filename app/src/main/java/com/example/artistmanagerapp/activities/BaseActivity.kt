package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.artistmanagerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

abstract class BaseActivity : AppCompatActivity() {

    // Firebase basic stuff
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    var user = auth.currentUser

    // Firebase Firestore paths
    val userPath = db.collection("users").document(user?.uid.toString())
    val artistsCollectionPath = userPath.collection(R.string.firestore_artistpages_collection.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        exitIfUserNotLogged()
    }

    override fun onResume() {
        super.onResume()

        exitIfUserNotLogged()
    }

    fun exitIfUserNotLogged(){
        if (user == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

}
