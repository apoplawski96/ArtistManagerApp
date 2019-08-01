package com.example.artistmanagerapp.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.SelectArtistPageAdapter
import com.example.artistmanagerapp.firebase.FirebaseDataReader
import com.example.artistmanagerapp.interfaces.ArtistPagesPresenter
import com.example.artistmanagerapp.models.ArtistPage
import android.app.Dialog
import android.content.Intent
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.*
import com.example.artistmanagerapp.firebase.FirebaseDataWriter
import com.example.artistmanagerapp.firebase.StorageFileUploader
import com.example.artistmanagerapp.interfaces.RedeemCodeDataReceiver
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.RedeemCode
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.Utils
import org.w3c.dom.Text
import java.io.IOException

class SelectArtistPageActivity : BaseActivity(), ArtistPagesPresenter, UserInterfaceUpdater, RedeemCodeDataReceiver {

    // Collections
    private var artistPageArrayList : ArrayList <ArtistPage> = ArrayList()

    // Views
    var selectArtistRecyclerView : RecyclerView? = null
    var fab : FloatingActionButton? = null
    var fabMin1 : FloatingActionButton? = null
    var fabMin2 : FloatingActionButton? = null
    var fabMin3 : FloatingActionButton? = null
    var createPageDialog : Dialog? = null
    var dialogNameInput : EditText? = null
    var dialogAddImageButton : FloatingActionButton? = null
    var dialogCreatePageButton : Button? = null
    var dialogClose : TextView? = null
    var noPagesText : TextView? = null
    var redeemCodeDialog : Dialog? = null
    var redeemCodeInput : EditText? = null
    var redeemCodeSubmitButton : Button? = null
    var dialogClose2 : TextView? = null
    var dialogBackgroundImage : ImageView? = null
    var createArtistPageTV : TextView? = null
    var joinArtistPageTV : TextView? = null
    var createDialogProgressBar : ProgressBar? = null
    var redeemDialogProgressBar : ProgressBar? = null

    // Adapters
    private var adapter: SelectArtistPageAdapter? = null

    // Firebase stuff
    var dataReader : FirebaseDataReader? = null
    var dataWriter : FirebaseDataWriter? = null

    // Others
    var isFABOpen : Boolean? = null
    var isCreatePageDialogOpen : Boolean? = null
    var isRedeemCodeDialogOpen : Boolean? = null

    // Objects
    val const = Constants
    val utils = Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_artist_page)
        Log.d(ACTIVITY_WELCOME_TAG, "Welcome to SelectArtistPageActivity")

        // Booleans initialization
        isFABOpen = false
        isCreatePageDialogOpen = false
        isRedeemCodeDialogOpen = false

        // Firebase utils objects
        dataReader = FirebaseDataReader()
        dataWriter = FirebaseDataWriter()

        // Views
        selectArtistRecyclerView = findViewById(R.id.select_artist_recycler_view)
        fab = findViewById(R.id.fab_main)
        fabMin1 = findViewById(R.id.fab_mini_1)
        fabMin2 = findViewById(R.id.fab_mini_2)
        noPagesText = findViewById(R.id.no_pages_text)
        createArtistPageTV = findViewById(R.id.create_artist_page)
        joinArtistPageTV = findViewById(R.id.join_artist_page)
        createDialogProgressBar = findViewById(R.id.create_dialog_progress_bar)
        redeemDialogProgressBar = findViewById(R.id.redeem_dialog_progress_bar)
        redeemDialogProgressBar?.visibility = View.GONE

        // Dialog stuff
        createPageDialog = Dialog(this)
        createPageDialog?.setContentView(R.layout.dialog_create_page)
        redeemCodeDialog = Dialog(this)
        redeemCodeDialog?.setContentView(R.layout.dialog_redeem_code)

        // Getting artist pages from database
        dataReader?.checkIfUserIsHasArtistPageLink(this)
        dataReader?.getArtistPages(this)

        // Adapter stuff
        selectArtistRecyclerView?.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        adapter = SelectArtistPageAdapter(artistPageArrayList) { item : ArtistPage -> artistPageClicked(item)}
        selectArtistRecyclerView?.adapter = adapter

        // Setting up Floating Action Button
        fab?.setOnClickListener {
            if (isFABOpen == false){
                //showFABMenu()
            } else {
                //closeFABMenu()
            }
        }

        createArtistPageTV?.setOnClickListener {showCreatePageDialog()}
        joinArtistPageTV?.setOnClickListener { showRedeemCodeDialog() }

        // Setting up "Create Page" Floating Action Button
        fabMin1?.setOnClickListener { if (isFABOpen == true){ showCreatePageDialog() } }
        fabMin2?.setOnClickListener { if (isFABOpen == true){ showRedeemCodeDialog() } }

    }

    // Setting up custom behaviour when dialog is shown
    override fun onBackPressed() {
        when {
            isCreatePageDialogOpen == true -> closeCreatePageDialog()
            isRedeemCodeDialogOpen == true -> closeRedeemCodeDialog()
            else -> super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Toast.makeText(this, "kuraw wrucilem", Toast.LENGTH_SHORT).show()

        if (requestCode == 1) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    dialogBackgroundImage?.setImageBitmap(bitmap)
                    Toast.makeText(this, "kuraw wrucilem", Toast.LENGTH_SHORT).show()
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    //Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // RecyclerView presenter method
    override fun showArtistPages(artistPagesList: ArrayList<ArtistPage>) {
        adapter?.update(artistPagesList)
    }

    override fun showNoPagesText() {
        Toast.makeText(this, "Nimo", Toast.LENGTH_SHORT).show()
        noPagesText?.visibility = View.VISIBLE
    }

    override fun updateUI(option : String) {
        when (option){
            const.ARTIST_PAGE_CREATED -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            const.CODE_SUCCESSFULLY_REDEEMED -> {

            }
        }
    }

    // RecyclerView onClick
    fun artistPageClicked(artistPage: ArtistPage){
        Toast.makeText(this, artistPage.toString(), Toast.LENGTH_SHORT).show()
    }

    /*private fun showFABMenu() {
        isFABOpen = true
        Toast.makeText(this, "show", Toast.LENGTH_SHORT).show()
        fabMin1?.animate()?.translationY(resources.getDimension(R.dimen.standard_55))
        fabMin2?.animate()?.translationY(resources.getDimension(R.dimen.standard_105))
        fabMin3?.animate()?.translationY(resources.getDimension(R.dimen.standard_155))
    }

    private fun closeFABMenu() {
        isFABOpen = false
        Toast.makeText(this, "hide", Toast.LENGTH_SHORT).show()
        fabMin1?.animate()?.translationY(0.toFloat())
        fabMin2?.animate()?.translationY(0.toFloat())
        fabMin3?.animate()?.translationY(0.toFloat())
    }*/

    private fun showCreatePageDialog(){
        isCreatePageDialogOpen = true

        // Views
        dialogNameInput = createPageDialog?.findViewById(R.id.dialog_artistname_input)
        dialogAddImageButton = createPageDialog?.findViewById(R.id.dialog_add_image_button)
        dialogClose = createPageDialog?.findViewById(R.id.dialog_close_x)
        dialogCreatePageButton = createPageDialog?.findViewById(R.id.dialog_submit_button)
        dialogBackgroundImage = createPageDialog?.findViewById(R.id.dialog_background_image)

        createPageDialog?.show()

        // OnClicks handling
        dialogClose?.setOnClickListener { createPageDialog?.hide() }

        dialogCreatePageButton?.setOnClickListener {
            var pageNameInputText = dialogNameInput?.text.toString()
            var artistPage = ArtistPage(pageNameInputText, userId)

            showProgress()
            dataWriter?.createArtistPage(artistPage, this, userId)
        }

        dialogAddImageButton?.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, const.GALLERY)
        }

    }

    private fun showRedeemCodeDialog(){
        isRedeemCodeDialogOpen = true

        // Views
        redeemCodeInput = redeemCodeDialog?.findViewById(R.id.redeem_code_input)
        redeemCodeSubmitButton = redeemCodeDialog?.findViewById(R.id.redeem_code_button)
        dialogClose2 = redeemCodeDialog?.findViewById(R.id.dialog_close_x)

        redeemCodeDialog?.show()

        // Events handling
        dialogClose2?.setOnClickListener { redeemCodeDialog?.hide() }

        redeemCodeSubmitButton?.setOnClickListener {
            var redeemCodeStringInput = redeemCodeInput?.text.toString()

            dataReader?.getRedeemCodeData(redeemCodeStringInput, this)
        }
    }

    private fun closeCreatePageDialog(){
        isCreatePageDialogOpen = false
        createPageDialog?.hide()
    }

    private fun closeRedeemCodeDialog(){
        isRedeemCodeDialogOpen = false
        redeemCodeDialog?.hide()
    }

    // Redeem code function
    override fun redeemCode(redeemCode: RedeemCode?) {
        if (redeemCode != null){
            // Setting all the stuff up in database
            dataWriter?.markCodeAsRedeemed(redeemCode.codeString.toString(), userId)
            dataWriter?.addArtistReferenceToUserRecord(userId, redeemCode.artistPageId)
            dataWriter?.addMemberToArtistPage(userId, redeemCode.artistPageId.toString())

            // UI update
            codeRedeemedUiUpdater()

            // Pass the data and go to MainActivity
            val intent = Intent(applicationContext, MainActivity::class.java).apply{ putExtra("artistPageId", redeemCode.artistPageId) }
            startActivity(intent)
        } else {
            Toast.makeText(this, "The code is not valid, please try again", Toast.LENGTH_SHORT).show()
        }
    }

    override fun receiveCodesList(codesList: ArrayList<RedeemCode>) { }

    override fun showProgress() {
        dialogNameInput?.visibility = View.GONE
        dialogClose?.visibility = View.GONE
        dialogCreatePageButton?.visibility = View.GONE
        createDialogProgressBar?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        createDialogProgressBar?.visibility = View.GONE
    }

    fun codeRedeemedUiUpdater() {

    }

    override fun initializeUI() {

    }

}
