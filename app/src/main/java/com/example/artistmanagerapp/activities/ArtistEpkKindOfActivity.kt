package com.example.artistmanagerapp.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
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
import com.example.artistmanagerapp.firebase.FirebaseDataReader
import com.example.artistmanagerapp.firebase.StorageFileDownloader
import com.example.artistmanagerapp.interfaces.ArtistPagesPresenter
import com.example.artistmanagerapp.interfaces.BundleUpdater
import com.example.artistmanagerapp.interfaces.MediaLoader
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.ui.DialogCreator
import com.example.artistmanagerapp.constants.Constants
import com.example.artistmanagerapp.firebase.FirebaseElectronicPressKitHelper
import kotlinx.android.synthetic.main.activity_artist_epk_kind_of.*

class ArtistEpkKindOfActivity : BaseActivity(), ArtistPagesPresenter, DialogCreator.DialogControllerCallback, BundleUpdater, MediaLoader {

    // ArtistPage data
    var pageId : String? = null
    var pageName : String? = null
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

    // Boolean controllers
    var isEpkDataMissing : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_epk_kind_of)

        // ArtistPage data
        pageId = intent.getStringExtra(Constants.PAGE_ID_BUNDLE)
        pageName = intent.getStringExtra(Constants.ARTIST_NAME_BUNDLE)
        epkShareCode = intent.getStringExtra(Constants.EPK_SHARE_CODE_BUNDLE)
        wasCodeRedeemed = intent.getBooleanExtra("wasCodeRedeemed", false)
        sharedPageId = intent.getStringExtra(Constants.EPK_SHARED_PAGE_ID)

        Toast.makeText(this, epkShareCode, Toast.LENGTH_SHORT).show()

        // Views
        genre = findViewById(R.id.epk_genre)
        instaLink = findViewById(R.id.epk_insta)
        fbLink = findViewById(R.id.epk_facebook)
        contact = findViewById(R.id.epk_contact)
        bio = findViewById(R.id.epk_bio)
        returnButton = findViewById(R.id.epk_return_button)
        progressOverlay = findViewById(R.id.progress_overlay)
        progressBar = findViewById(R.id.progress_bar_epk_edit)

        initUI()

        FirebaseDataReader().getArtistPageData(pageId, this, null)

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

        returnButton?.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

        // Making sure that updated data is sent back to EpkSelectorActivity
        val intent = Intent(applicationContext, EpkSelectorActivity::class.java).apply{
            putExtra(Constants.PAGE_ID_BUNDLE, pageId)
            putExtra(Constants.ARTIST_NAME_BUNDLE, pageName)
            putExtra(Constants.EPK_SHARE_CODE_BUNDLE, epkShareCode)
        }
        startActivity(intent)
    }

    private fun dynamicColor() {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.avatar)
        Palette.from(bitmap).generate { palette ->
            collapsingToolbarLayout!!.setContentScrimColor(palette!!.getMutedColor(resources.getColor(R.color.colorPrimary)))
            collapsingToolbarLayout!!.setStatusBarScrimColor(palette!!.getMutedColor(resources.getColor(R.color.colorAccent)))
        }
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
        var isEpkDataMissing = false

        collapsingToolbarLayout!!.title = artistPage.artistName.toString()
        genre?.text = artistPage.genre.toString()
        instaLink?.text = artistPage.instagramLink.toString()
        fbLink?.text = artistPage.facebookLink.toString()
        bio?.text = artistPage.biography.toString()
        contact?.text = artistPage.contact.toString()

        artistPageData.put("Genre", genre?.text.toString())
        artistPageData.put("Instagram link",instaLink?.text.toString())
        artistPageData.put("Facebook link", fbLink?.text.toString())
        artistPageData.put("Biography", bio?.text.toString())
        artistPageData.put("Contact", contact?.text.toString())
        artistPageData.put("Name", artistPage.artistName.toString())

        // Checking if there's all the necessary data
        for ((key, value) in artistPageData){
            if (value == "null"){
                DialogCreator.showDialog(DialogCreator.DialogType.MISSING_EPK_DATA, this, this)
                progressOverlay?.visibility = View.VISIBLE
                progressBar?.visibility = View.GONE
                /*isEpkDataMissing = true
                Toast.makeText(this, "You need to fill all the EPK data first!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, EpkSelectorActivity::class.java))
                finish()*/
            }
        }

        StorageFileDownloader().downloadImageViaId(pageId, StorageFileDownloader.DownloadOption.EPK_COVER_PHOTO, this)
    }

    override fun loadImage(bitmap: Bitmap?, option: MediaLoader.MediaLoaderOptions?) {
        artist_cover_photo_epk.setImageBitmap(bitmap)
        onImageSuccessfullyLoaded()
    }

    override fun onLoadingFailed(error: String?) {
        DialogCreator.showDialog(DialogCreator.DialogType.MISSING_EPK_DATA, this, this)
        progressOverlay?.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
    }

    fun onImageSuccessfullyLoaded(){
        if ((epkShareCode == null) or (epkShareCode == "null")){ FirebaseElectronicPressKitHelper.generateShareCode(pageId, this) }
        updateUI()
    }

    override fun onAccept(option : DialogCreator.DialogControllerCallback.CallbackOption?) {
        this.finish()
    }

    override fun onCodeRedeemed(pageId: String?) {

    }

    override fun onDismiss() {

    }

    override fun onShown() {

    }

    override fun onCallInvalid() {

    }

    override fun updateBundleData(newData: Any?) {
        epkShareCode = newData.toString()
        //Toast.makeText(this, "Data updated with: $epkShareCode", Toast.LENGTH_SHORT).show()
    }

    override fun showArtistPages(artistPagesList: ArrayList<ArtistPage>) {}
    override fun showNoPagesText() {}

    override fun onDismissWithOption(option: DialogCreator.DialogControllerCallback.DismissCalbackOption) {

    }

}
