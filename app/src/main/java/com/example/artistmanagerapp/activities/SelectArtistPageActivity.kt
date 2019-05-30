package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.SelectArtistPageAdapter
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.firebase.FirebaseDataReader
import com.example.artistmanagerapp.interfaces.ArtistPagesPresenter
import com.example.artistmanagerapp.models.Artist
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.Task
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.horizontal_select_artist_recycler_view.*
import android.support.v4.view.ViewCompat.animate
import android.R.attr.translationY
import android.support.design.widget.FloatingActionButton


class SelectArtistPageActivity : BaseActivity(), ArtistPagesPresenter {

    // Collections
    private var artistPageArrayList : ArrayList <ArtistPage> = ArrayList()

    // Views
    var selectArtistRecyclerView : RecyclerView? = null
    var fab : FloatingActionButton? = null
    var fabMin1 : FloatingActionButton? = null
    var fabMin2 : FloatingActionButton? = null
    var fabMin3 : FloatingActionButton? = null

    // Adapters
    private var adapter: SelectArtistPageAdapter? = null

    // Others
    var isFABOpen : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_artist_page)
        Log.d(ACTIVITY_WELCOME_TAG, "Welcome to SelectArtistPageActivity")

        // Objects
        val dataReader = FirebaseDataReader()

        // Views
        selectArtistRecyclerView = findViewById(R.id.select_artist_recycler_view)
        fab = findViewById(R.id.fab_main)
        fabMin1 = findViewById(R.id.fab_mini_1)
        fabMin2 = findViewById(R.id.fab_mini_2)
        fabMin3 = findViewById(R.id.fab_mini_3)

        // Setting up artist pages
        dataReader.checkIfUserIsHasArtistPageLink()
        dataReader.getArtistPages(this)
        loadArtistPages()

        // Setting up Floating Action Button
        isFABOpen = false
        fab?.setOnClickListener {
            if (isFABOpen == false){
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }

        selectArtistRecyclerView?.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        adapter = SelectArtistPageAdapter(artistPageArrayList) { item : ArtistPage -> artistPageClicked(item)}
        selectArtistRecyclerView?.adapter = adapter

        //Here I have to somehow upload the RecyclerView - I got the data, but the recycler view remains as it was

    }

    fun loadArtistPages(){
        db.collection("users").document("$userId").collection("artist_pages")
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents){
                        var artistPageName = document.get("artist_name").toString()
                        var artistPageId = document.id

                        artistPageArrayList.add(ArtistPage(artistPageName, artistPageId))
                    }
                } else {

                }
            }
    }

    override fun showArtistPages(artistPagesList: ArrayList<ArtistPage>) {
        adapter?.update(artistPagesList)
    }

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

    fun goToCreateOrJoinActivity(){
        val intent = Intent(this, CreateOrJoinActivity::class.java)
        startActivity(intent)
    }

    fun goToSelectArtistPageActivity(){
        val intent = Intent(this, SelectArtistPageActivity::class.java)
        startActivity(intent)
    }

}
