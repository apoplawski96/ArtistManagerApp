package com.example.artistmanagerapp.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.firebase.FirebaseDataWriter
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.constants.Constants
import com.example.artistmanagerapp.constants.FirebaseConstants
import com.example.artistmanagerapp.utils.Utils
import kotlinx.android.synthetic.main.activity_create_artist_page.*

class CreateArtistPageActivity : BaseActivity(), UserInterfaceUpdater {

    val TAG = "CreateArtistPageActivity"

    // Bundled objects
    var userBundleInstance : User? = null
    var artistPageInstance : ArtistPage? = null
    var photoBitmap : Bitmap? = null
    var photoByteArray : ByteArray? = null

    // Boolean controllers
    var isSoloArtistRadioChecked = false
    var isBandRadioChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_artist_page)

        Log.d(TAG, "Activity entered")

        // Receiving bundled objects
        artistPageInstance = intent.extras.getSerializable(Constants.BUNDLE_ARTIST_PAGE_INSTANCE) as ArtistPage
        userBundleInstance = intent.extras.getSerializable(Constants.BUNDLE_USER_INSTANCE) as User
        photoByteArray = intent.extras.getByteArray("photoCompressed")

        initUI()

        submit_button.setOnClickListener {
            val c = FirebaseConstants
            if (genre_input.text.isNotEmpty() && (isSoloArtistRadioChecked || isBandRadioChecked)){
                showProgressOverlay()
                lateinit var category : String
                val name = artist_page_name_input.text.toString()
                val genre = genre_input.text.toString()
                val currentTime = Utils.getCurrentTimeShort()
                val currentDate = Utils.getCurrentDateShort()
                val createdById = userBundleInstance!!.id.toString()
                var createdByDisplayName = userBundleInstance!!.getDisplayName()
                if (isSoloArtistRadioChecked) category = radio_button_solo_artist.text.toString()
                else category = radio_button_band.text.toString()

                val newData = mapOf<String, Any>(
                    c.ARTIST_NAME to name,
                    c.ARTIST_GENRE to genre,
                    c.ARTIST_TIME_CREATED to currentTime,
                    c.ARTIST_DATE_CREATED to currentDate,
                    c.ARTIST_CREATED_BY_ID to createdById,
                    c.ARTIST_CREATED_BY_DISPLAY_NAME to createdByDisplayName,
                    c.ARTIST_CATEGORY to category)

                val newArtistPage = ArtistPage()
                newArtistPage.artistName = name
                newArtistPage.genre = genre
                newArtistPage.pageCategory = category
                newArtistPage.createdById = createdById
                newArtistPage.createdByDisplayName = createdByDisplayName

                FirebaseDataWriter()?.createArtistPage(newArtistPage!!, this, userBundleInstance, convertBytesArrayToBitmap(photoByteArray!!))
                //FirebaseDataWriter().updateArtistPageData(artistPageInstance.artistPageId.toString(), newData, this)

            } else {
                Toast.makeText(this, "Genre and category has to be filled", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun onRadioButtonClicked(view : View){
        if (view is RadioButton) {
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_button_solo_artist ->
                    if (checked) {
                        isSoloArtistRadioChecked = true
                        isBandRadioChecked = false
                        radio_button_band?.isChecked = false
                    }
                R.id.radio_button_band ->
                    if (checked) {
                        isBandRadioChecked = true
                        isSoloArtistRadioChecked = false
                        radio_button_solo_artist?.isChecked = false
                    }
            }
        }
    }

    fun showProgressOverlay(){
        cover_solid.visibility = View.VISIBLE
        cover_progress.visibility = View.VISIBLE
    }

    fun hideProgressOverlay(){
        cover_solid.visibility = View.GONE
        cover_progress.visibility = View.GONE
    }

    fun initUI(){
        artist_page_name_input.setText(artistPageInstance!!.artistName.toString())
        Utils.hardDisableEditText(artist_page_name_input)
        val bitmap = convertBytesArrayToBitmap(photoByteArray!!)
        artist_cover_photo.setImageBitmap(bitmap)
    }

    fun convertBytesArrayToBitmap(byteArray: ByteArray) : Bitmap{
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size!!.toInt())
    }

    override fun updateUI(option: String, data: Any?) {
        hideProgressOverlay()
        when (option){
            Constants.ARTIST_PAGE_CREATED -> {
                val artistPage : ArtistPage = data as ArtistPage
                userBundleInstance?.currentArtistPageId = artistPage!!.artistPageId
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(Constants.BUNDLE_USER_INSTANCE, userBundleInstance)
                    putExtra(Constants.BUNDLE_ARTIST_PAGE_INSTANCE, artistPage)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    override fun hideProgress() { }

    override fun initializeUI() { }

    override fun showProgress() { }

}
