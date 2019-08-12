package com.example.artistmanagerapp.activities

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.graphics.Palette
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.firebase.ArtistPagesHelper
import com.example.artistmanagerapp.firebase.FirebaseDataReader
import com.example.artistmanagerapp.interfaces.ArtistPagesPresenter
import com.example.artistmanagerapp.interfaces.BundleUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.ui.DialogCreator
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.ElectronicPressKitHelper
import com.example.artistmanagerapp.utils.MyAppGlideModule



class SharedEpkActivity : BaseActivity(), ArtistPagesPresenter {

    // ArtistPage data
    var pageId : String? = null
    var epkShareCode : String? = null
    var wasCodeRedeemed = false
    var sharedPageId : String? = null

    // Views
    var genre : TextView? = null
    var instaLink : TextView? = null
    var fbLink : TextView? = null
    var contact : TextView? = null
    var bio : TextView? = null
    var returnButton : ImageView? = null
    var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    var progressOverlay : ConstraintLayout? = null
    var progressBar : ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_epk_kind_of)

        // ArtistPage bundled data
        wasCodeRedeemed = intent.getBooleanExtra("wasCodeRedeemed", false)
        sharedPageId = intent.getStringExtra(Constants.EPK_SHARED_PAGE_ID)

        // Views
        genre = findViewById(R.id.epk_genre)
        instaLink = findViewById(R.id.epk_insta)
        fbLink = findViewById(R.id.epk_facebook)
        contact = findViewById(R.id.epk_contact)
        bio = findViewById(R.id.epk_bio)
        returnButton = findViewById(R.id.epk_return_button)
        progressOverlay = findViewById(R.id.progress_overlay)
        progressBar = findViewById(R.id.epk_progress_bar)

        // Collapsing Toolbar Setup (jakby ktos nie zauwazyl xD)
        val typeface : Typeface? = ResourcesCompat.getFont(this, R.font.montserrat)
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout!!.title = "NOT ENOUGH"
        collapsingToolbarLayout!!.setExpandedTitleMargin(40,40,40,44)
        collapsingToolbarLayout!!.setExpandedTitleTextAppearance(R.style.ExpandedText)
        collapsingToolbarLayout!!.setCollapsedTitleTextAppearance(R.style.CollapsedText)
        collapsingToolbarLayout!!.setStatusBarScrimColor(Color.parseColor("#2e2c36"))
        collapsingToolbarLayout!!.setCollapsedTitleTypeface(typeface)
        collapsingToolbarLayout!!.setExpandedTitleTypeface(typeface)

        initUI()

        FirebaseDataReader().getArtistPageData(sharedPageId, this, null)

        returnButton?.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun initUI(){
        progressBar?.visibility = View.VISIBLE
        progressOverlay?.visibility = View.VISIBLE
    }

    fun updateUI(){
        progressBar?.visibility = View.GONE
        progressOverlay?.visibility = View.GONE
    }

    override fun showArtistPageData(artistPage: ArtistPage) {
        var artistPageData : HashMap <String, String> = HashMap()

        collapsingToolbarLayout!!.title = artistPage.artistName.toString()
        genre?.text = artistPage.genre.toString()
        instaLink?.text = artistPage.instagramLink.toString()
        fbLink?.text = artistPage.facebookLink.toString()
        bio?.text = artistPage.biography.toString()
        contact?.text = artistPage.biography.toString()

        artistPageData.put("Genre", genre?.text.toString())
        artistPageData.put("Instagram link",instaLink?.text.toString())
        artistPageData.put("Facebook link", fbLink?.text.toString())
        artistPageData.put("Biography", bio?.text.toString())
        artistPageData.put("Contact", contact?.text.toString())
        artistPageData.put("Name", artistPage.artistName.toString())

        // Cover photo retrieving and loading into a view
        val imageRef = storageRef.child("electronicPressKitPhotos/MvFdswTbaR9YdnWr967C/cover.jpg")
        var artistImage : ImageView = findViewById(R.id.artist_cover_photo_epk)
        imageRef.getBytes(1024*1024).addOnSuccessListener { bitmapData ->
            val bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData?.size!!.toInt())
            artistImage.setImageBitmap(bitmap)

            updateUI()
        }

    }

    override fun showArtistPages(artistPagesList: ArrayList<ArtistPage>) {}
    override fun showNoPagesText() {}
}
