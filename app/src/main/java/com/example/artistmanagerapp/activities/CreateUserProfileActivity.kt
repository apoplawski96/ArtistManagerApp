package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.firebase.FirebaseDataWriter
import com.example.artistmanagerapp.interfaces.UserDataPresenter
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

class CreateUserProfileActivity : BaseActivity(), UserInterfaceUpdater {

    // Views
    var submitButton : Button? = null
    var firstNameInput : EditText? = null
    var lastNameInput : EditText? = null

    // Collections
    var userInitData : HashMap <String,Any> = HashMap()
    var userProfileData : HashMap <String, Any> = HashMap()
    var artistPages : HashMap <String, Any> = HashMap()

    // Others
    var ifInputsCorrect : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user_profile)
        Log.d(ACTIVITY_WELCOME_TAG, "Welcome to CreateUserProfileActivity")

        // Views
        firstNameInput = findViewById(R.id.first_name)
        lastNameInput = findViewById(R.id.last_name)
        submitButton = findViewById(R.id.submit_button)

        // Getting data from previous activity
        var intent : Intent = intent
        var isDbRecordCreated = intent.getStringExtra("isDbRecordCreated")

        Toast.makeText(this, user?.uid.toString(), Toast.LENGTH_SHORT).show()

        if (isDbRecordCreated == "false"){
            initUserDatabaseRecord(user)
        }

        submitButton?.setOnClickListener {
            // We check if all the inputs have correct format
            ifInputsCorrect = validateInputs(firstNameInput, lastNameInput)

            if (ifInputsCorrect == true){
                mapDataFromTextInputs(firstNameInput, lastNameInput)
                // We mark completion status as completed
                userProfileData.put("profile_completion_status", "completed")
                FirebaseDataWriter().addUserDataToDbAndUpdateUi(usersCollectionPath, userProfileData, user?.uid.toString(), this)
            } else {

            }
        }
    }

    // ************************************ FUNCTIONS SECTION START ************************************

    fun initUserDatabaseRecord(user : FirebaseUser?){
        var userId = user?.uid.toString()
        var userEmail = user?.email.toString()

        // Paths
        var userPath = db.collection(R.string.firestore_users_collection.toString()).document(userId)
        var userPathId = db.collection(R.string.firestore_users_collection.toString()).document(userId).id
        var artistsPath = db.collection(R.string.firestore_users_collection.toString()).document(userId).collection(R.string.firestore_artistpages_collection.toString()).document()
        var hartistsPath = db.collection(R.string.firestore_users_collection.toString()).document(userId).collection("asdasdasd").document().id

        // Setting up init user data to collection
        userInitData.put("email_address", userEmail)
        userInitData.put("profile_completion_status", "started")

        // Writing init user data to database
        db.collection("users").document(userId).set(userInitData).addOnSuccessListener {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Fallus", Toast.LENGTH_SHORT).show()
        }

        // Artist page stuff
        //artistPages.put(R.string.firestore_islinkedwithartistpage.toString(), "false")
        //artistsPath.set(artistPages).ad
    }

    fun mapDataFromTextInputs(firstName : EditText?, lastName : EditText?){
        userProfileData.put("first_name", firstName?.text.toString())
        userProfileData.put("last_name", lastName?.text.toString())
    }

    fun validateInputs(firstName : EditText?, lastName : EditText?) : Boolean{
        Utils.validateFirstName(firstName?.text.toString())
        Utils.validateLastName(lastName?.text.toString())
        return true
    }

    override fun updateUI(){
        Toast.makeText(this, "Henlo", Toast.LENGTH_SHORT).show()
        val intent = Intent(applicationContext, SelectArtistPageActivity::class.java).apply{
            //putExtra("isDbRecordCreated", "true")
        }
        startActivity(intent)
    }

    // ************************************ FUNCTIONS SECTION ENDS ************************************
}
