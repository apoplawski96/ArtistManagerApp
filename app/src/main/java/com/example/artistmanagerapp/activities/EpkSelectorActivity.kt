package com.example.artistmanagerapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.interfaces.DataReceiver
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.ui.DialogCreator
import com.example.artistmanagerapp.utils.Constants

class EpkSelectorActivity : BaseActivity(), UserInterfaceUpdater, DialogCreator.DialogControllerCallback, DataReceiver {

    // Current ArtistPage data
    var pageId : String? = null
    var pageName : String? = null
    var epkShareCode : String? = null

    // Views
    var editEpkInfoButton : Button? = null
    var generateEpkButton : Button? = null
    var shareEpkButton : Button? = null
    var redeeemEpkButton : Button? = null
    var backButton : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epk_selector)

        // Getting bundled ArtistPageData
        pageId = intent.getStringExtra(Constants.PAGE_ID_BUNDLE)
        pageName = intent.getStringExtra(Constants.ARTIST_NAME_BUNDLE)
        epkShareCode = intent.getStringExtra(Constants.EPK_SHARE_CODE_BUNDLE)

        //Toast.makeText(this, "Name:$pageName+ ID:$pageId+ ShareCode:$epkShareCode", Toast.LENGTH_SHORT).show()

        // Views
        editEpkInfoButton = findViewById(R.id.change_password_button)
        generateEpkButton = findViewById(R.id.generate_epk_btn)
        shareEpkButton = findViewById(R.id.share_epk_btn)
        redeeemEpkButton = findViewById(R.id.redeem_epk_btn)
        backButton = findViewById(R.id.epk_selector_back_button)

        editEpkInfoButton?.setOnClickListener {
            val intent = Intent(applicationContext, EpkEditInfoActivity::class.java).apply{
                putExtra(Constants.PAGE_ID_BUNDLE, pageId)
                putExtra(Constants.ARTIST_NAME_BUNDLE, pageName)
                putExtra(Constants.EPK_SHARE_CODE_BUNDLE, epkShareCode)
            }
            startActivity(intent)
            finish()
        }

        generateEpkButton?.setOnClickListener {
            val intent = Intent(applicationContext, ArtistEpkKindOfActivity::class.java).apply{
                putExtra(Constants.PAGE_ID_BUNDLE, pageId)
                putExtra(Constants.ARTIST_NAME_BUNDLE, pageName)
                putExtra(Constants.EPK_SHARE_CODE_BUNDLE, epkShareCode)
            }
            startActivity(intent)
            finish()
        }

        shareEpkButton?.setOnClickListener {
            if ((epkShareCode == null) or (epkShareCode == "null")){
                DialogCreator.showDialog(DialogCreator.DialogType.EPK_NOT_GENERATED, this, this)
            } else {
                DialogCreator.showCodeDialog(DialogCreator.DialogType.SHARE_EPK_DIALOG, this, this, epkShareCode.toString())
            }
        }

        redeeemEpkButton?.setOnClickListener {
            DialogCreator.showCodeDialog(DialogCreator.DialogType.REDEEM_EPK_DIALOG, this, this, null)
        }

        backButton?.setOnClickListener {
            onBackPressed()
        }

    }

    override fun receiveData(data: Any?, mInterface: Any?) {

    }

    override fun onCodeRedeemed(redeemedPageId: String?) {
        val intent = Intent(applicationContext, SharedEpkActivity::class.java).apply{
            putExtra(Constants.EPK_SHARED_PAGE_ID, redeemedPageId)
            putExtra("wasCodeRedeemed", true)
        }
        startActivity(intent)
    }

    override fun onAccept(option: DialogCreator.DialogControllerCallback.CallbackOption?) {
        when (option){
            DialogCreator.DialogControllerCallback.CallbackOption.CODE_REDEEMED -> {

            }
        }
    }

    override fun onDismiss() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShown() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCallInvalid() {

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

    override fun updateUI(option: String, data : Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
