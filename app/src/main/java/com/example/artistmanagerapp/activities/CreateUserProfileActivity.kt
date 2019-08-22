package com.example.artistmanagerapp.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.util.Log
import android.view.View
import android.widget.*
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.firebase.FirebaseDataWriter
import com.example.artistmanagerapp.firebase.StorageFileUploader
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.FirebaseConstants
import com.example.artistmanagerapp.utils.Utils
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import kotlin.collections.HashMap

class CreateUserProfileActivity : BaseActivity(), UserInterfaceUpdater {

    var userInstance : User? = null

    // Views
    var submitButton : Button? = null
    var firstNameInput : EditText? = null
    var lastNameInput : EditText? = null
    var addPhotoFab : FloatingActionButton? = null
    var avatarImageView : CircleImageView? = null
    var radioButtonArtist : RadioButton? = null
    var radioButtonManager : RadioButton? = null
    var pageRoleInput : EditText? = null
    var userProfileBackButton : ImageView? = null

    // Variables
    var bitmap : Bitmap? = null

    // Bundle data set
    var isDbRecordCreated : String? = null
    var mode : String? = null

    // Collections
    var userInitData : HashMap <String,Any> = HashMap()
    var userProfileData : HashMap <String, Any> = HashMap()

    // Boolean controllers
    var isAvatarUploaded : Boolean? = false
    var areInputsValid : Boolean? = false
    var isArtistRadioChecked : Boolean? = false
    var isManagerRadioChecked : Boolean? = false
    var ifInputsCorrect : Boolean? = null

    // Others
    val c = FirebaseConstants

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user_profile)
        Log.d(ACTIVITY_WELCOME_TAG, "Welcome to CreateUserProfileActivity")

        // Views
        firstNameInput = findViewById(R.id.artist_name_epk)
        lastNameInput = findViewById(R.id.artist_genre_epk)
        submitButton = findViewById(R.id.generate_button)
        addPhotoFab = findViewById(R.id.fab_add_photo)
        avatarImageView = findViewById(R.id.edit_profile_photo)
        radioButtonArtist = findViewById(R.id.radio_button_artist)
        radioButtonManager = findViewById(R.id.radio_button_manager)
        pageRoleInput = findViewById(R.id.edit_profile_position)
        userProfileBackButton = findViewById(R.id.edit_profile_back_button)

        // Getting data from previous activity
        isDbRecordCreated = intent.getStringExtra("isDbRecordCreated")
        mode = intent.getStringExtra(Constants.MODE_KEY)
        if (mode == Constants.USER_PROFILE_EDIT_MODE){
            userInstance = intent.getSerializableExtra(Constants.BUNDLE_USER_INSTANCE) as User?
        }

        if (isDbRecordCreated == "false"){
            initUserDatabaseRecord(user)
        }

        Toast.makeText(this, mode.toString(), Toast.LENGTH_SHORT).show()

        initUI(mode)

        // OnClicks implementation
        submitButton?.setOnClickListener {
            // We check if all the inputs have correct format

            if (true){
                mapDataFromTextInputs(firstNameInput, lastNameInput, pageRoleInput, isArtistRadioChecked, isManagerRadioChecked)
                // We mark completion status as completed
                userProfileData.put(c.PROFILE_COMPLETION_STATUS, c.V_PROFILE_STATUS_COMPLETED)
                StorageFileUploader()?.saveImage(bitmap, storageRef.child("avatars/$userId/avatar.jpg"), this)
                FirebaseDataWriter().addUserDataToDbAndUpdateUi(usersCollectionPath, userProfileData, user?.uid.toString(), this, bitmap)
            } else {

            }
        }

        addPhotoFab?.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 1)
        }

    }

    fun initUI (mode : String?){
        when (mode){
            Constants.USER_PROFILE_CREATE_MODE -> {
                userProfileBackButton?.visibility = View.GONE
                submitButton?.text = "CREATE PROFILE"
            }
            Constants.USER_PROFILE_EDIT_MODE -> {
                loadCurrentData()
                userProfileBackButton?.visibility = View.VISIBLE
                userProfileBackButton?.setOnClickListener { onBackPressed() }
                submitButton?.text = "SAVE CHANGES"
            }
        }
    }

    fun loadCurrentData(){
        if (userInstance?.firstName != "null") firstNameInput?.setText(userInstance?.firstName)
        if (userInstance?.lastName != "null") lastNameInput?.setText(userInstance?.lastName)
        if (userInstance?.pageRole != "null") pageRoleInput?.setText(userInstance?.pageRole)
        when (userInstance?.roleCategory){
            "artist" -> radioButtonArtist?.isChecked = true
            "manager" -> radioButtonManager?.isChecked = true
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_button_manager ->
                    if (checked) {
                        isManagerRadioChecked = true
                        isArtistRadioChecked = false
                        radioButtonArtist?.isChecked = false
                    } else {

                    }
                R.id.radio_button_artist ->
                    if (checked) {
                        isArtistRadioChecked = true
                        isManagerRadioChecked = false
                        radioButtonManager?.isChecked = false
                    }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    avatarImageView?.setImageBitmap(bitmap)
                    isAvatarUploaded = true
                }
                catch (e: IOException) { e.printStackTrace() }
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
    }

    fun mapDataFromTextInputs(firstName : EditText?, lastName : EditText?, position : EditText?, isArtistChecked : Boolean?, isManagerChecked : Boolean?){
        userProfileData.put(c.FIRST_NAME, firstName?.text.toString())
        userProfileData.put(c.LAST_NAME, lastName?.text.toString())
        userProfileData.put(c.PAGE_ROLE, position?.text.toString())
        if (isArtistChecked == true) userProfileData.put(c.ROLE_CATEGORY, "artist")
        else if (isManagerChecked == true) userProfileData.put (c.ROLE_CATEGORY, "manager")
    }

    override fun updateUI(option : String, data : Any?){
        when (mode){
            Constants.USER_PROFILE_EDIT_MODE -> {
                Toast.makeText(this, "Data successfully saved", Toast.LENGTH_SHORT).show()
            }
            Constants.USER_PROFILE_CREATE_MODE -> {
                var intent = Intent(applicationContext, SelectArtistPageActivity::class.java).apply{
                    //putExtra("isDbRecordCreated", "true")
                }
                startActivity(intent)
            }
        }
    }

    override fun initializeUI() {

    }

    override fun hideProgress() {
    }

    override fun showProgress() {
    }

    // ************************************ FUNCTIONS SECTION ENDS ************************************
}
