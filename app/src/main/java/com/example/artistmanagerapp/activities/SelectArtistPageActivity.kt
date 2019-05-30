package com.example.artistmanagerapp.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.SelectArtistPageAdapter
import com.example.artistmanagerapp.firebase.FirebaseDataReader
import com.example.artistmanagerapp.interfaces.ArtistPagesPresenter
import com.example.artistmanagerapp.models.ArtistPage
import android.app.Dialog
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.artistmanagerapp.firebase.FirebaseDataWriter
import com.example.artistmanagerapp.interfaces.RedeemCodeDataReceiver
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.RedeemCode
import org.w3c.dom.Text

class SelectArtistPageActivity : BaseActivity(), ArtistPagesPresenter, UserInterfaceUpdater, RedeemCodeDataReceiver {

    override fun receiveCodeData(redeemCode: RedeemCode) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun receiveCodesList(codesList: ArrayList<RedeemCode>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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

    // Adapters
    private var adapter: SelectArtistPageAdapter? = null

    // Firebase stuff
    var dataReader : FirebaseDataReader? = null
    var dataWriter : FirebaseDataWriter? = null

    // Others
    var isFABOpen : Boolean? = null
    var isCreatePageDialogOpen : Boolean? = null
    var isRedeemCodeDialogOpen : Boolean? = null

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
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }

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

    // RecyclerView presenter method
    override fun showArtistPages(artistPagesList: ArrayList<ArtistPage>) {
        adapter?.update(artistPagesList)
    }

    override fun showNoPagesText() {
        Toast.makeText(this, "Nimo", Toast.LENGTH_SHORT).show()
        noPagesText?.visibility = View.VISIBLE
    }

    override fun updateUI() {
        Toast.makeText(this, "Henlo", Toast.LENGTH_SHORT).show()
    }

    // RecyclerView onClick
    fun artistPageClicked(artistPage: ArtistPage){
        Toast.makeText(this, artistPage.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun showFABMenu() {
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
    }

    private fun showCreatePageDialog(){
        isCreatePageDialogOpen = true

        // Views
        dialogNameInput = createPageDialog?.findViewById(R.id.dialog_artistname_input)
        dialogAddImageButton = createPageDialog?.findViewById(R.id.dialog_add_image_button)
        dialogClose = createPageDialog?.findViewById(R.id.dialog_close_x)
        dialogCreatePageButton = createPageDialog?.findViewById(R.id.dialog_submit_button)

        createPageDialog?.show()

        // OnClicks handling
        dialogClose?.setOnClickListener { createPageDialog?.hide() }

        dialogCreatePageButton?.setOnClickListener {
            var pageNameInputText = dialogNameInput?.text.toString()
            var artistPage = ArtistPage(pageNameInputText)

            dataWriter?.createArtistPage(artistPage, this, userId)
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
            var redeemCodeStringInput = redeemCodeInput.toString()

            dataReader?.getRedeemCodeData(redeemCodeStringInput, this)


        }
    }

    private fun closeCreatePageDialog(){
        isCreatePageDialogOpen = false
        createPageDialog?.hide()
    }

    private fun closeRedeemCodeDialog(){
        isRedeemCodeDialogOpen = false
    }

}
