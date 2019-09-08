package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.firebase.FirebaseDataWriter
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.FirebaseConstants
import com.example.artistmanagerapp.utils.Utils
import kotlinx.android.synthetic.main.activity_create_artist_page.*

class CreateArtistPageActivity : BaseActivity(), UserInterfaceUpdater {

    // Bundled objects
    lateinit var userBundleInstance : User
    lateinit var artistPageInstance : ArtistPage

    // Boolean controllers
    var isSoloArtistRadioChecked = false
    var isBandRadioChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_artist_page)

        // Receiving bundled objects
        artistPageInstance = intent.extras.getSerializable(Constants.BUNDLE_ARTIST_PAGE_INSTANCE) as ArtistPage
        userBundleInstance = intent.extras.getSerializable(Constants.BUNDLE_USER_INSTANCE) as User

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
                val createdById = userBundleInstance.id.toString()
                var createdByDisplayName = userBundleInstance.getDisplayName()
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

                FirebaseDataWriter().updateArtistPageData(artistPageInstance.artistPageId.toString(), newData, this)

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
        artist_page_name_input.setText(artistPageInstance.artistName.toString())
        Utils.hardDisableEditText(artist_page_name_input)
    }

    override fun updateUI(option: String, data: Any?) {
        hideProgressOverlay()
        when (option){
            Constants.ARTIST_PAGE_UPDATED -> {
                userBundleInstance?.currentArtistPageId = artistPageInstance.artistPageId
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(Constants.BUNDLE_USER_INSTANCE, userBundleInstance)
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
