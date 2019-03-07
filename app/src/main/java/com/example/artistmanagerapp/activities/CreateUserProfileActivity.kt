package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

class CreateUserProfileActivity : AppCompatActivity() {

    var userData : HashMap <String,Any> = HashMap()
    var artistPages : HashMap <String, Any> = HashMap()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user_profile)

        // Firebase stuff
        val auth = FirebaseAuth.getInstance()
        var user = auth.currentUser

        //
        var intent : Intent = intent
        var isDbRecordCreated = intent.getStringExtra("isDbRecordCreated")

        Toast.makeText(this, isDbRecordCreated, Toast.LENGTH_SHORT).show()

        initUserDatabaseRecord(user)

        if (isDbRecordCreated!=null){

            if (isDbRecordCreated.equals("false")){
                initUserDatabaseRecord(user)
            } else {

            }

        }

    }

    // ************************************ FUNCTIONS SECTION START ************************************

    fun mapTextFromField(map : Map<String, Any>, key : String, value : String){

    }

    fun initUserDatabaseRecord(user : FirebaseUser?){

        var userId = user?.uid.toString()
        var userEmail = user?.email.toString()

        // Paths
        //var userPath = db.collection(R.string.firestore_users_collection.toString()).document(userId)
        var userPathId = db.collection(R.string.firestore_users_collection.toString()).document(userId).id
        var artistsPath = db.collection(R.string.firestore_users_collection.toString()).document(userId).collection(R.string.firestore_artistpages_collection.toString()).document()
        var hartistsPath = db.collection(R.string.firestore_users_collection.toString()).document(userId).collection("asdasdasd").document().id

        // User info stuff
        userData.put(R.string.firestore_email.toString(), userEmail)
        userData.put(R.string.firestore_profilecompletionstatus.toString(), "started")

        db.collection(R.string.firestore_users_collection.toString()).document(userId).set(userData).addOnSuccessListener {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Fallus", Toast.LENGTH_SHORT).show()
        }

        // Artist page stuff
        artistPages.put(R.string.firestore_islinkedwithartistpage.toString(), "false")

        //artistsPath.set(artistPages).ad


    }

    // ************************************ FUNCTIONS SECTION ENDS ************************************

}
