package com.example.artistmanagerapp.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.firebase.FirebaseDataReader
import com.example.artistmanagerapp.firebase.StorageFileUploader
import com.example.artistmanagerapp.interfaces.ArtistPagesPresenter
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.constants.Constants
import com.example.artistmanagerapp.firebase.FirebaseElectronicPressKitHelper
import com.example.artistmanagerapp.constants.FirebaseConstants
import com.example.artistmanagerapp.utils.Utils
import java.io.IOException

class EpkEditInfoActivity : BaseActivity(), UserInterfaceUpdater, View.OnClickListener, View.OnFocusChangeListener, ArtistPagesPresenter {

    // Others
    val c = FirebaseConstants

    // ArtistPage info
    val const = Constants
    var pageId : String? = null
    var pageName : String? = null

    // Views
    var artistNameInput : EditText? = null
    var genreInput : EditText? = null
    var instagramInput : EditText? = null
    var facebookInput : EditText? = null
    var bioInput : EditText? = null
    var contactInput : EditText? = null
    var addImageButton : ImageView? = null
    var artistImage : ImageView? = null
    var saveDataButton : Button? = null
    var progressBar : ProgressBar? = null
    var uploadProgressBar : ProgressBar? = null
    var backButton : ImageView? = null
    var saveDataUpperButton : ImageView? = null
    var dismissButton : ImageView? = null

    // Image upload stuff
    var bitmap : Bitmap? = null

    // Boolean UI controllers
    var hasEditingStarted : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epk_edit_info)

        // Getting bundled data
        pageId = intent.getStringExtra(Constants.PAGE_ID_BUNDLE)
        pageName = intent.getStringExtra(Constants.ARTIST_NAME_BUNDLE)

        // Views
        artistNameInput = findViewById(R.id.artist_name_epk)
        genreInput = findViewById(R.id.artist_genre_epk)
        bioInput = findViewById(R.id.artist_bio_epk)
        instagramInput = findViewById(R.id.artist_insta_link)
        facebookInput = findViewById(R.id.artist_fb_link)
        contactInput = findViewById(R.id.artist_contact)
        saveDataButton = findViewById(R.id.generate_button)
        artistImage = findViewById(R.id.artist_cover_photo)
        addImageButton = findViewById(R.id.add_image_button)
        progressBar = findViewById(R.id.progress_bar_epk_edit)
        uploadProgressBar = findViewById(R.id.upload_progress_bar)
        backButton = findViewById(R.id.epk_edit_activity_back_button)
        saveDataUpperButton = findViewById(R.id.epk_edit_activity_upper_save_button)
        dismissButton = findViewById(R.id.epk_edit_activity_cancel_button)

        // Setting Views OnClicks and OnFocusChange's
        artistNameInput?.setOnFocusChangeListener(this)
        genreInput?.setOnFocusChangeListener(this)
        bioInput?.setOnFocusChangeListener(this)
        instagramInput?.setOnFocusChangeListener(this)
        facebookInput?.setOnFocusChangeListener(this)
        contactInput?.setOnFocusChangeListener(this)
        saveDataButton?.setOnClickListener(this)
        addImageButton?.setOnClickListener(this)
        backButton?.setOnClickListener(this)
        saveDataUpperButton?.setOnClickListener(this)
        dismissButton?.setOnClickListener(this)

        // UI initialization
        this.initializeUI()

        // Getting current EPK data - if there's any
        loadCurrentEpkData() // !!!!!!! it's possible to get it later from previous activities via bundle
    }

    fun loadCurrentEpkData(){
        FirebaseDataReader().getArtistPageData(pageId, this, null)
    }

    override fun showArtistPageData(artistPage: ArtistPage) {
        if (artistPage.biography != "null") bioInput?.setText(artistPage.biography)
        if (artistPage.genre != "null") genreInput?.setText(artistPage.genre)
        if (artistPage.instagramLink != "null") instagramInput?.setText(artistPage.instagramLink)
        if (artistPage.contact != "null") contactInput?.setText(artistPage.contact)
        if (artistPage.facebookLink != "null") facebookInput?.setText(artistPage.facebookLink)
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (hasFocus){
            activateToolbar()
        } else {
            disableToolbar()
        }
    }

    override fun onClick(view: View?) {
        when (view){
            genreInput, bioInput, facebookInput, instagramInput, contactInput -> {
                activateToolbar()
            }
            addImageButton -> {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 1)
            }
            saveDataButton -> {
                var dataMap : HashMap <String, Any> = HashMap()
                var artistName = artistNameInput?.text.toString()
                var genre = genreInput?.text.toString()
                var instaLink = instagramInput?.text.toString()
                var fbLink = facebookInput?.text.toString()
                var bio = bioInput?.text.toString()
                var contact = contactInput?.text.toString()

                showProgress()

                dataMap.put(c.ARTIST_NAME, artistName)
                dataMap.put(c.ARTIST_GENRE, genre)
                dataMap.put(c.ARTIST_IG, instaLink)
                dataMap.put(c.ARTIST_FB, fbLink)
                dataMap.put(c.ARTIST_BIO, bio)
                dataMap.put(c.ARTIST_CONTACT, contact)

                FirebaseElectronicPressKitHelper.saveEpkData(dataMap, pageId, this)
            }
            saveDataUpperButton -> {
                var dataMap : HashMap <String, Any> = HashMap()
                var artistName = artistNameInput?.text.toString()
                var genre = genreInput?.text.toString()
                var instaLink = instagramInput?.text.toString()
                var fbLink = facebookInput?.text.toString()
                var bio = bioInput?.text.toString()
                var contact = contactInput?.text.toString()

                showProgress()

                dataMap.put(c.ARTIST_NAME, artistName)
                dataMap.put(c.ARTIST_GENRE, genre)
                dataMap.put(c.ARTIST_IG, instaLink)
                dataMap.put(c.ARTIST_FB, fbLink)
                dataMap.put(c.ARTIST_BIO, bio)
                dataMap.put(c.ARTIST_CONTACT, contact)

                FirebaseElectronicPressKitHelper.saveEpkData(dataMap, pageId, this)
            }
            backButton -> {
                onBackPressed()
            }
            dismissButton -> {
                disableToolbar()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fileUploader = StorageFileUploader()

        // UI updates
        uploadProgressBar?.visibility = View.VISIBLE
        artistImage?.visibility = View.INVISIBLE

        if (requestCode == 1) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    fileUploader?.saveImage(bitmap, storageRef.child("epkPhotos/$pageId/cover.jpg"), this)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    //Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        //disableToolbar()
        super.onBackPressed()
    }

    fun activateToolbar(){
        hasEditingStarted = true
        backButton?.visibility = View.GONE
        dismissButton?.visibility = View.VISIBLE
        saveDataUpperButton?.visibility = View.VISIBLE
    }

    fun disableToolbar(){
        hasEditingStarted = false
        backButton?.visibility = View.VISIBLE
        dismissButton?.visibility = View.GONE
        saveDataUpperButton?.visibility = View.GONE
    }

    override fun hideProgress() {
        progressBar?.visibility = View.GONE
        dismissButton?.visibility = View.GONE
        backButton?.visibility = View.VISIBLE
    }

    override fun initializeUI() {
        artistNameInput?.setText(pageName.toString())
        Utils.hardDisableEditText(artistNameInput)
    }

    override fun showProgress() {
        saveDataUpperButton?.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
    }

    override fun updateUI(option: String, data : Any?) {
        when (option){
            const.IMAGE_SUCCESSFULLY_UPLOADED -> {
                artistImage?.setImageBitmap(bitmap)
                uploadProgressBar?.visibility = View.GONE
                artistImage?.visibility = View.VISIBLE
                Toast.makeText(this, "Image uploaded", Toast.LENGTH_SHORT).show()
            }
            "EPK_DATA_SUCCESSFULLY_SAVED" -> {
                hideProgress()
                Toast.makeText(this, "EPK data successfully saved", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun showArtistPages(artistPagesList: ArrayList<ArtistPage>) { }

    override fun showNoPagesText() { }

}
