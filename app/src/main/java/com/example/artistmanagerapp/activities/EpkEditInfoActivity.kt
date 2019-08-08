package com.example.artistmanagerapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.ElectronicPressKitHelper
import com.example.artistmanagerapp.utils.FirebaseConstants

class EpkEditInfoActivity : AppCompatActivity(), UserInterfaceUpdater {

    val c = FirebaseConstants
    var pageId : String? = null

    // Views
    var artistNameInput : EditText? = null
    var genreInput : EditText? = null
    var instagramInput : EditText? = null
    var facebookInput : EditText? = null
    var bioInput : EditText? = null
    var addImageButton : ImageView? = null
    var artistImage : ImageView? = null
    var saveDataButton : Button? = null
    var progressBar : ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epk_edit_info)
        pageId = intent.getStringExtra(Constants.PAGE_ID_BUNDLE)

        Toast.makeText(this, pageId.toString(), Toast.LENGTH_SHORT).show()

        // Views
        artistNameInput = findViewById(R.id.artist_name_epk)
        genreInput = findViewById(R.id.artist_genre_epk)
        bioInput = findViewById(R.id.artist_bio_epk)
        instagramInput = findViewById(R.id.artist_insta_link)
        facebookInput = findViewById(R.id.artist_fb_link)
        saveDataButton = findViewById(R.id.generate_button)
        artistImage = findViewById(R.id.artist_cover_photo)
        addImageButton = findViewById(R.id.add_image_button)
        progressBar = findViewById(R.id.progress_bar_epk_edit)

        // OnClicks
        addImageButton?.setOnClickListener {  }
        saveDataButton?.setOnClickListener {
            var dataMap : HashMap <String, Any> = HashMap()
            var artistName = artistNameInput?.text.toString()
            var genre = genreInput?.text.toString()
            var instaLink = instagramInput?.text.toString()
            var fbLink = facebookInput?.text.toString()
            var bio = bioInput?.text.toString()

            showProgress()

            dataMap.put(c.ARTIST_NAME, artistName)
            dataMap.put(c.ARTIST_GENRE, genre)
            dataMap.put(c.ARTIST_IG, instaLink)
            dataMap.put(c.ARTIST_FB, fbLink)
            dataMap.put(c.ARTIST_BIO, bio)

            ElectronicPressKitHelper.saveEpkData(dataMap, pageId, this)
        }

    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initializeUI() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        progressBar?.visibility = View.GONE
    }

    override fun updateUI(option: String) {
        Toast.makeText(this, "Zapisane", Toast.LENGTH_SHORT).show()
    }
}
