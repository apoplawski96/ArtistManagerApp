package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.FirebaseConstants

class EpkSelectorActivity : AppCompatActivity(), UserInterfaceUpdater {

    // Current ArtistPage
    var pageId : String? = null

    // Views
    var editEpkInfoButton : Button? = null
    var generateEpkButton : Button? = null
    var shareEpkButton : Button? = null
    var redeeemEpkButton : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epk_selector)

        pageId = intent.getStringExtra(Constants.PAGE_ID_BUNDLE)

        // Views
        editEpkInfoButton = findViewById(R.id.edit_info_bt)
        generateEpkButton = findViewById(R.id.generate_epk_btn)
        shareEpkButton = findViewById(R.id.share_epk_btn)
        redeeemEpkButton = findViewById(R.id.redeem_epk_btn)

        editEpkInfoButton?.setOnClickListener {
            val intent = Intent(applicationContext, EpkEditInfoActivity::class.java).apply{
                putExtra(Constants.PAGE_ID_BUNDLE, pageId)
            }
            startActivity(intent)
        }
        generateEpkButton?.setOnClickListener {
            val intent = Intent(applicationContext, ArtistEpkKindOfActivity::class.java).apply{
                //putExtra(FirebaseConstants.CURRENT_ARTIST_PAGE, "true")
            }
            startActivity(intent)
        }
        shareEpkButton?.setOnClickListener {  }
        redeeemEpkButton?.setOnClickListener {  }

    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initializeUI() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUI(option: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
