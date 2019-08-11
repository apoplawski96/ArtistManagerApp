package com.example.artistmanagerapp.activities

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.View
import android.widget.*
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.firebase.StorageFileUploader
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.ElectronicPressKitHelper
import com.example.artistmanagerapp.utils.FirebaseConstants
import com.example.artistmanagerapp.utils.Utils
import java.io.IOException

class EpkEditInfoActivity : BaseActivity(), UserInterfaceUpdater {

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

    // Image upload stuff
    var bitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epk_edit_info)

        // Getting bundled data
        pageId = intent.getStringExtra(Constants.PAGE_ID_BUNDLE)
        pageName = intent.getStringExtra(Constants.ARTIST_NAME_BUNDLE)

        Toast.makeText(this, pageId.toString(), Toast.LENGTH_SHORT).show()

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

        artistNameInput?.setText(pageName.toString())
        Utils.disableEditText(artistNameInput)

        // OnClicks
        addImageButton?.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 1)
        }

        saveDataButton?.setOnClickListener {
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

            ElectronicPressKitHelper.saveEpkData(dataMap, pageId, this)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fileUploader = StorageFileUploader()

        // UI updates
        uploadProgressBar?.visibility = View.VISIBLE
        artistImage?.visibility = View.INVISIBLE

        Toast.makeText(this, "kuraw wrucilem", Toast.LENGTH_SHORT).show()

        if (requestCode == 1) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    fileUploader?.saveImage(bitmap, storageRef.child("electronicPressKitPhotos/$pageId/cover.jpg"), this)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    //Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun hideProgress() {
        progressBar?.visibility = View.GONE
    }

    override fun initializeUI() { }

    override fun showProgress() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun updateUI(option: String) {
        when (option){
            const.IMAGE_SUCCESSFULLY_UPLOADED -> {
                artistImage?.setImageBitmap(bitmap)
                uploadProgressBar?.visibility = View.GONE
                artistImage?.visibility = View.VISIBLE
                Toast.makeText(this, "Image uploaded", Toast.LENGTH_SHORT).show()
            }

        }

    }
}
