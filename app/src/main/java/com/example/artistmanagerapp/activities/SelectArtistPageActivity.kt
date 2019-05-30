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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.artistmanagerapp.firebase.FirebaseDataWriter
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater

class SelectArtistPageActivity : BaseActivity(), ArtistPagesPresenter, UserInterfaceUpdater {

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

    // Adapters
    private var adapter: SelectArtistPageAdapter? = null

    // Firebase stuff
    var dataReader : FirebaseDataReader? = null
    var dataWriter : FirebaseDataWriter? = null

    // Others
    var isFABOpen : Boolean? = null
    var isDialogOpen : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_artist_page)
        Log.d(ACTIVITY_WELCOME_TAG, "Welcome to SelectArtistPageActivity")

        // Booleans initialization
        isFABOpen = false
        isDialogOpen = false

        // Firebase utils objecst
        dataReader = FirebaseDataReader()
        dataWriter = FirebaseDataWriter()

        // Views
        selectArtistRecyclerView = findViewById(R.id.select_artist_recycler_view)
        fab = findViewById(R.id.fab_main)
        fabMin1 = findViewById(R.id.fab_mini_1)
        fabMin2 = findViewById(R.id.fab_mini_2)

        // Dialog stuff
        createPageDialog = Dialog(this)
        createPageDialog?.setContentView(R.layout.create_page_popup)

        // Getting artist pages from database
        dataReader?.checkIfUserIsHasArtistPageLink()
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
        fabMin1?.setOnClickListener {
            if (isFABOpen == true){
                showDialog()
            }
        }
    }

    // Setting up custom behaviour when dialog is shown
    override fun onBackPressed() {
        if (isDialogOpen == true){
            closeDialog()
        } else{
            super.onBackPressed()
        }
    }

    // RecyclerView presenter method
    override fun showArtistPages(artistPagesList: ArrayList<ArtistPage>) {
        adapter?.update(artistPagesList)
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

    private fun showDialog(){
        isDialogOpen = true

        // Views
        dialogNameInput = createPageDialog?.findViewById(R.id.dialog_artistname_input)
        dialogAddImageButton = createPageDialog?.findViewById(R.id.dialog_add_image_button)
        dialogClose = createPageDialog?.findViewById(R.id.dialog_close_x)
        dialogCreatePageButton = createPageDialog?.findViewById(R.id.dialog_submit_button)

        // Actions
        createPageDialog?.show()

        // OnClicks handling
        dialogClose?.setOnClickListener {
            createPageDialog?.hide()
        }

        dialogCreatePageButton?.setOnClickListener {
            var pageNameInputText = dialogNameInput?.text.toString()
            var artistPage = ArtistPage(pageNameInputText)

            dataWriter?.createArtistPage(artistPage, this, userId)
        }
    }

    private fun closeDialog(){
        isDialogOpen = false
        createPageDialog?.hide()
    }
}
