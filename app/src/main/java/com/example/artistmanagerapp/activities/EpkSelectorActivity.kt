package com.example.artistmanagerapp.activities

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.interfaces.DataReceiver
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.ui.DialogCreator
import com.example.artistmanagerapp.constants.Constants
import com.example.artistmanagerapp.firebase.FirebaseElectronicPressKitHelper
import kotlinx.android.synthetic.main.activity_epk_selector.*
import kotlinx.android.synthetic.main.dialog_share_epk_code.*
import kotlinx.android.synthetic.main.dialog_use_epk_code.*

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

    // Views - Dialogs
    var shareEpkCodeDialog : Dialog? = null
    var redeemEpkCodeDialog : Dialog? = null

    // Variable
    var isDialogShown : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epk_selector)

        // Getting bundled ArtistPageData
        pageId = intent.getStringExtra(Constants.PAGE_ID_BUNDLE)
        pageName = intent.getStringExtra(Constants.ARTIST_NAME_BUNDLE)
        epkShareCode = intent.getStringExtra(Constants.EPK_SHARE_CODE_BUNDLE)

        // Views
        editEpkInfoButton = findViewById(R.id.change_password_button)
        generateEpkButton = findViewById(R.id.generate_epk_btn)
        shareEpkButton = findViewById(R.id.share_epk_btn)
        redeeemEpkButton = findViewById(R.id.redeem_epk_btn)
        backButton = findViewById(R.id.epk_selector_back_button)

        // Views - Dialogs
        shareEpkCodeDialog = Dialog(this)
        shareEpkCodeDialog?.setContentView(R.layout.dialog_share_epk_code)
        redeemEpkCodeDialog = Dialog(this)
        redeemEpkCodeDialog?.setContentView(R.layout.dialog_use_epk_code)

        editEpkInfoButton?.setOnClickListener {
            val intent = Intent(applicationContext, EpkEditInfoActivity::class.java).apply{
                putExtra(Constants.PAGE_ID_BUNDLE, pageId)
                putExtra(Constants.ARTIST_NAME_BUNDLE, pageName)
                putExtra(Constants.EPK_SHARE_CODE_BUNDLE, epkShareCode)
            }
            startActivity(intent)
        }

        generateEpkButton?.setOnClickListener {
            val intent = Intent(applicationContext, ArtistEpkKindOfActivity::class.java).apply{
                putExtra(Constants.PAGE_ID_BUNDLE, pageId)
                putExtra(Constants.ARTIST_NAME_BUNDLE, pageName)
                putExtra(Constants.EPK_SHARE_CODE_BUNDLE, epkShareCode)
            }
            startActivity(intent)
        }

        shareEpkButton?.setOnClickListener {
            if ((epkShareCode == null) or (epkShareCode == "null")){
                DialogCreator.showDialog(DialogCreator.DialogType.EPK_NOT_GENERATED, this, this)
            } else {
                //DialogCreator.showCodeDialog(DialogCreator.DialogType.SHARE_EPK_DIALOG, this, this, epkShareCode.toString())
                showShareEpkDialog(epkShareCode.toString())
            }
        }

        redeeemEpkButton?.setOnClickListener {
            //DialogCreator.showCodeDialog(DialogCreator.DialogType.REDEEM_EPK_DIALOG, this, this, null)
            showRedeemEpkDialog()
        }

        backButton?.setOnClickListener {
            onBackPressed()
        }

    }

    fun hideButtons(){
        editEpkInfoButton!!.visibility = View.INVISIBLE
        generateEpkButton!!.visibility = View.INVISIBLE
        shareEpkButton!!.visibility = View.INVISIBLE
        redeeemEpkButton!!.visibility = View.INVISIBLE
    }

    fun showButtons(){
        editEpkInfoButton!!.visibility = View.VISIBLE
        generateEpkButton!!.visibility = View.VISIBLE
        shareEpkButton!!.visibility = View.VISIBLE
        redeeemEpkButton!!.visibility = View.VISIBLE
    }

    fun showShareEpkDialog(epkShareCode : String){
        shareEpkCodeDialog!!.dialog_redeem_code_text.setText(epkShareCode)

        shareEpkCodeDialog!!.dialog_copy_button.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied redeem code", epkShareCode)
            clipboard.primaryClip = clip
            Toast.makeText(this, "Code copied!", Toast.LENGTH_SHORT).show()
        }

        shareEpkCodeDialog!!.share_dialog_close_x.setOnClickListener { shareEpkCodeDialog!!.cancel() }
        shareEpkCodeDialog!!.setOnShowListener { hideButtons() }
        shareEpkCodeDialog!!.setOnCancelListener { showButtons() }
        shareEpkCodeDialog!!.show()
    }

    fun showRedeemEpkDialog(){
        redeemEpkCodeDialog!!.redeem_code_button.setOnClickListener {
            val codeInput = redeemEpkCodeDialog!!.epk_code_input.text.toString()
            if (codeInput.length == 5){
                FirebaseElectronicPressKitHelper.redeemEpkShareCode(codeInput, this)
                epk_selector_progress_bar.visibility = View.VISIBLE
                redeemEpkCodeDialog!!.hide()
            } else {
                Toast.makeText(this, "Code format is invalid, please try again", Toast.LENGTH_SHORT).show()
            }
        }

        redeemEpkCodeDialog!!.redeem_dialog_close_x.setOnClickListener { redeemEpkCodeDialog!!.cancel() }
        redeemEpkCodeDialog!!.setOnShowListener { hideButtons() }
        redeemEpkCodeDialog!!.setOnCancelListener { showButtons() }
        redeemEpkCodeDialog!!.show()
    }



    override fun receiveData(data: Any?, mInterface: Any?) {
        if (data != null){
            val codeConnectedPageId = data as String
            val intent = Intent(applicationContext, SharedEpkActivity::class.java).apply{
                putExtra(Constants.EPK_SHARED_PAGE_ID, codeConnectedPageId)
                putExtra("wasCodeRedeemed", true)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "Provided code is invalid", Toast.LENGTH_SHORT).show()
            epk_selector_progress_bar.visibility = View.GONE
            redeemEpkCodeDialog!!.show()
        }
    }

    override fun onCodeRedeemed(redeemedPageId: String?) {}

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

    override fun onDismissWithOption(option: DialogCreator.DialogControllerCallback.DismissCalbackOption) {

    }
}
