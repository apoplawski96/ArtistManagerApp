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
import android.graphics.Bitmap
import android.provider.MediaStore
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.CardView
import android.view.View
import android.widget.*
import com.example.artistmanagerapp.firebase.FirebaseDataWriter
import com.example.artistmanagerapp.interfaces.DataReceiver
import com.example.artistmanagerapp.interfaces.RedeemCodeDataReceiver
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.RedeemCode
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.UsersHelper
import com.example.artistmanagerapp.utils.Utils
import kotlinx.android.synthetic.main.activity_select_artist_page.*
import java.io.IOException

class SelectArtistPageActivity : BaseActivity(), ArtistPagesPresenter, UserInterfaceUpdater, RedeemCodeDataReceiver, DataReceiver {

    // Tags
    val ACT_TAG = "SelectArtistPageActivity"

    // Collections
    private var artistPageArrayList : ArrayList <ArtistPage> = ArrayList()

    // Variables set - User
    var userObject : User? = null
    var mFirstName : String? = null
    var mLastName : String? = null
    var pageRole : String? = null
    var artistRole : String? = null
    var currentPage : String? = null
    var email : String? = null

    // Bundle objects
    var userBundleInstance : User? = null

    // Views
    var selectArtistRecyclerView : RecyclerView? = null
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
    var createArtistPageItem : CardView? = null
    var joinArtistPageItem : CardView? = null
    var createDialogProgressBar : ProgressBar? = null
    var redeemDialogProgressBar : ProgressBar? = null
    var coverSolid : ConstraintLayout? = null
    var coverProgress : ProgressBar? = null


    // Adapters
    private var adapter: SelectArtistPageAdapter? = null

    // Firebase stuff
    var dataReader : FirebaseDataReader? = null
    var dataWriter : FirebaseDataWriter? = null

    // Boolean controllers
    var isFABOpen : Boolean? = null
    var isCreatePageDialogOpen : Boolean? = null
    var isRedeemCodeDialogOpen : Boolean? = null
    var isPhotoUploaded = false
    var isPageNameValid = false

    // Objects
    val const = Constants
    val utils = Utils
    var bitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_artist_page)
        Log.d(ACT_TAG, "Welcome to SelectArtistPageActivity! - onCreate()")

        // Getting User data
        var uId = auth.currentUser?.uid.toString()
        UsersHelper.getUserData(uId, this)
        Log.d(ACT_TAG, "getUserData() called")

        // Booleans initialization
        isFABOpen = false
        isCreatePageDialogOpen = false
        isRedeemCodeDialogOpen = false

        // Firebase utils objects
        dataReader = FirebaseDataReader()
        dataWriter = FirebaseDataWriter()

        // Views
        selectArtistRecyclerView = findViewById(R.id.artist_page_selector_recycler_view)
        createArtistPageItem = findViewById(R.id.create_artist_page_cardview)
        joinArtistPageItem = findViewById(R.id.join_artist_page_cardview)
        coverSolid = findViewById(R.id.cover_solid) as ConstraintLayout
        coverProgress = findViewById(R.id.cover_progress) as ProgressBar

        // Dialog stuff
        createPageDialog = Dialog(this)
        createPageDialog?.setContentView(R.layout.dialog_create_page)
        redeemCodeDialog = Dialog(this)
        redeemCodeDialog?.setContentView(R.layout.dialog_redeem_code)

        // Getting artist pages from database
        dataReader?.checkIfUserIsHasArtistPageLink(this)
        Log.d(ACT_TAG, "checkIfUserHasArtistPageLink() called")
        dataReader?.getArtistPages(this)
        Log.d(ACT_TAG, "getArtistPages() called")

        // Adapter stuff
        selectArtistRecyclerView?.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        adapter = SelectArtistPageAdapter(artistPageArrayList) { item : ArtistPage -> artistPageClicked(item)}
        selectArtistRecyclerView?.adapter = adapter

        createArtistPageItem?.setOnClickListener { showCreatePageDialog() }
        joinArtistPageItem?.setOnClickListener { showRedeemCodeDialog() }
    }

    // Setting up custom behaviour when dialogs are shown
    override fun onBackPressed() {
        when {
            isCreatePageDialogOpen == true -> closeCreatePageDialog()
            isRedeemCodeDialogOpen == true -> closeRedeemCodeDialog()
            else -> super.onBackPressed()
        }
    }

    // Getting photo from storage and getting it ready to upload
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    dialogBackgroundImage?.setImageBitmap(bitmap)
                    isPhotoUploaded = true
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    // RecyclerView presenter method
    override fun showArtistPages(artistPagesList: ArrayList<ArtistPage>) {
        noPagesText?.visibility = View.GONE
        adapter?.update(artistPagesList)
    }

    override fun showNoPagesText() {
        noPagesText?.visibility = View.VISIBLE
    }

    override fun updateUI(option : String, data : Any?) {
        when (option){
            const.CURRENT_PAGE_NOT_NULL ->{
                val currentPageId : String = data as String
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(Constants.FIRST_NAME_BUNDLE, mFirstName)
                    putExtra(Constants.LAST_NAME_BUNDLE, mLastName)
                    putExtra(Constants.PAGE_ROLE_BUNDLE, pageRole)
                    putExtra("CURRENT_PAGE_BUNDLE", currentPageId)
                }
                Log.d("CURRENT PAGE NOT NULL -> MainActivity", "Bundle data sent: ${currentPageId}")
                startActivity(intent)
            }
            const.ARTIST_PAGE_CREATED -> {
                val newArtistPage : ArtistPage = data as ArtistPage
                userBundleInstance?.currentArtistPageId = newArtistPage.artistPageId
                val intent = Intent(this, CreateArtistPageActivity::class.java).apply {
                    putExtra(Constants.BUNDLE_USER_INSTANCE, userBundleInstance)
                    putExtra(Constants.BUNDLE_ARTIST_PAGE_INSTANCE, newArtistPage)
                }
                //Log.d("SelectArtistPageActivity -> MainActivity", "Bundle data sent: ${pageInstance?.artistName},ID: ${pageInstance?.artistPageId}")
                startActivity(intent)
                finish()
            }
            const.CODE_SUCCESSFULLY_REDEEMED -> {
                val currentPageId : String = data as String
                userBundleInstance?.currentArtistPageId = currentPageId
                val intent = Intent(applicationContext, MainActivity::class.java).apply{
                    putExtra(Constants.BUNDLE_USER_INSTANCE, userBundleInstance)
                }
                startActivity(intent)
            }
            const.ARTIST_PAGE_SELECTED -> {
                val pageInstance : ArtistPage? = data as ArtistPage
                userBundleInstance?.currentArtistPageId = pageInstance?.artistPageId
                Log.d("updateUI() fun inside of SelectArtistPageActivity", "Option: ${const.ARTIST_PAGE_SELECTED}")
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(Constants.BUNDLE_USER_INSTANCE, userBundleInstance)
                    putExtra(Constants.BUNDLE_ARTIST_PAGE_INSTANCE, pageInstance)
                }
                Log.d("SelectArtistPageActivity -> MainActivity", "Bundle data sent: ${pageInstance?.artistName},ID: ${pageInstance?.artistPageId}")
                startActivity(intent)
            }
        }
    }

    // ArtisPage selector RecyclerView onClick
    fun artistPageClicked(artistPage: ArtistPage){
        Log.d(ACT_TAG, "pageItemClicked() - ${artistPage.artistName}, ${artistPage.artistPageId}")
        UsersHelper.setCurrentArtistPage(userId, artistPage, this)
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
        dialogNameInput = createPageDialog?.findViewById(R.id.dialog_redeem_code_text)
        dialogAddImageButton = createPageDialog?.findViewById(R.id.dialog_add_image_button)
        dialogClose = createPageDialog?.findViewById(R.id.dialog_close_x)
        dialogCreatePageButton = createPageDialog?.findViewById(R.id.dialog_copy_button)
        dialogBackgroundImage = createPageDialog?.findViewById(R.id.dialog_background_image)
        createDialogProgressBar = findViewById(R.id.create_dialog_progress_bar)

        createPageDialog?.show()

        // OnClicks handling
        dialogClose?.setOnClickListener { createPageDialog?.hide() }

        dialogCreatePageButton?.setOnClickListener {
            var pageNameInputText = dialogNameInput?.text.toString()
            isPageNameValid = Utils.validatePageName(pageNameInputText)

            if (isPageNameValid && isPhotoUploaded){
                var artistPage = ArtistPage(pageNameInputText, userId)
                showProgress()
                createDialogProgressBar?.visibility = View.VISIBLE
                dataWriter?.createArtistPage(artistPage, this, userObject, bitmap)
                createPageDialog!!.hide()
            } else {
                Toast.makeText(this, "Page name must be at least 3 characters long and photo needs to be uploaded", Toast.LENGTH_LONG).show()
            }
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
        redeemDialogProgressBar = findViewById(R.id.redeem_dialog_progress_bar)

        redeemCodeDialog?.show()

        // Events handling
        dialogClose2?.setOnClickListener { redeemCodeDialog?.hide() }

        redeemCodeSubmitButton?.setOnClickListener {
            var redeemCodeStringInput = redeemCodeInput?.text.toString()

            dataReader?.getRedeemCodeData(redeemCodeStringInput, this)
        }
    }

    // Redeem code function
    override fun redeemCode(redeemCode: RedeemCode?) {
        if (redeemCode != null){
            // Setting all the stuff up in database
            dataWriter?.markCodeAsRedeemed(redeemCode.codeString.toString(), userId)
            dataWriter?.addArtistReferenceToUserRecord(userId, redeemCode.artistPageId)
            dataWriter?.addMemberToArtistPage(userId, redeemCode.artistPageId.toString(), userObject as User, this)

        } else {
            Toast.makeText(this, "The code is not valid, please try again", Toast.LENGTH_SHORT).show()
        }
    }

    override fun receiveCodesList(codesList: ArrayList<RedeemCode>) { }

    fun showProgressOverlay(){
        coverProgress?.visibility = View.VISIBLE
        coverSolid?.visibility = View.VISIBLE
        linear_layout_container.visibility = View.GONE
    }

    override fun showProgress() {
        createPageDialog?.hide()
        redeemCodeDialog?.hide()
        showProgressOverlay()
    }

    override fun hideProgress() {
        createDialogProgressBar?.visibility = View.GONE
    }

    fun codeRedeemedUiUpdater() {

    }

    override fun initializeUI() {

    }

    // We're receiving CurrentPage data to decide if we stay here or go to CurrentPage
    override fun receiveData(data: Any?, mInterface: Any?) {
        val userInfo = data as User
        userBundleInstance = data
        userObject = data
        currentPage = userInfo.currentArtistPageId.toString()
        mFirstName = userInfo.firstName.toString()
        mLastName = userInfo.lastName.toString()
        email = userInfo.email.toString()
        pageRole = userInfo.pageRole.toString()
        artistRole = userInfo.artistRole.toString()

        Log.d("USER DATA REECEIVED", userObject.toString())

        if (currentPage != "null"){
            Log.d("SelectArtistPage, receiveData()", currentPage)
            updateUI(const.CURRENT_PAGE_NOT_NULL, currentPage)
        }
    }

    override fun showArtistPageData(artistPage: ArtistPage) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun closeCreatePageDialog(){
        isCreatePageDialogOpen = false
        createPageDialog?.hide()
    }

    private fun closeRedeemCodeDialog(){
        isRedeemCodeDialogOpen = false
        redeemCodeDialog?.hide()
    }

}
