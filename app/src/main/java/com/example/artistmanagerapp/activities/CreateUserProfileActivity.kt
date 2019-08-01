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
import com.example.artistmanagerapp.utils.FirebaseConstants
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
    val c = FirebaseConstants

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
                userProfileData.put(c.PROFILE_COMPLETION_STATUS, c.V_PROFILE_STATUS_COMPLETED)
                FirebaseDataWriter().addUserDataToDbAndUpdateUi(usersCollectionPath, userProfileData, user?.uid.toString(), this)
            } else {

            }
        }
    }

    // ************************************ FUNCTIONS SECTION START ************************************

    fun initUserDatabaseRecord(user : FirebaseUser?){
        var userId = user?.uid.toString()
        var userEmail = user?.email.toString()

        // Setting up init user data to collection
        userInitData.put(c.EMAIL, userEmail)
        userInitData.put(c.PROFILE_COMPLETION_STATUS, c.V_PROFILE_STATUS_STARTED)

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
        userProfileData.put(c.FIRST_NAME, firstName?.text.toString())
        userProfileData.put(c.LAST_NAME, lastName?.text.toString())
    }

    fun validateInputs(firstName : EditText?, lastName : EditText?) : Boolean{
        Utils.validateFirstName(firstName?.text.toString())
        Utils.validateLastName(lastName?.text.toString())
        return true
    }

    override fun updateUI(option : String){
        Toast.makeText(this, "Henlo", Toast.LENGTH_SHORT).show()
        val intent = Intent(applicationContext, SelectArtistPageActivity::class.java).apply{
            //putExtra("isDbRecordCreated", "true")
        }
        startActivity(intent)
    }

    override fun initializeUI() {

    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // ************************************ FUNCTIONS SECTION ENDS ************************************
}
