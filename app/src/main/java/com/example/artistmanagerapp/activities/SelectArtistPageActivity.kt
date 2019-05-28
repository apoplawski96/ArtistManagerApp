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

class SelectArtistPageActivity : BaseActivity(), ArtistPagesPresenter {

    // Collections
    private var artistPageArrayList : ArrayList <ArtistPage> = ArrayList()

    // Views
    var selectArtistRecyclerView : RecyclerView? = null

    // Adapters
    private var adapter: SelectArtistPageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_artist_page)
        Log.d(ACTIVITY_WELCOME_TAG, "Welcome to SelectArtistPageActivity")

        // Objects
        val dataReader : FirebaseDataReader = FirebaseDataReader()

        // Views
        selectArtistRecyclerView = findViewById(R.id.select_artist_recycler_view)

        dataReader.checkIfUserIsHasArtistPageLink()
        dataReader.getArtistPages(this)

        //populateListWithFakeStuff()
        loadArtistPages()
        addBlankArtistPage()

        selectArtistRecyclerView?.layoutManager = LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false)
        adapter = SelectArtistPageAdapter(artistPageArrayList)
        selectArtistRecyclerView?.adapter = adapter

        //Here I have to somehow upload the RecyclerView - I got the data, but the recycler view remains as it was

    }

    fun populateListWithFakeStuff(){
        artistPageArrayList.add(ArtistPage("hui", "1"))
        artistPageArrayList.add(ArtistPage("hui2", "2"))
    }

    fun loadArtistPages(){
        perfectUserPath.collection("artist_pages")
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents){
                        var artistPageName = document.get("artist_name").toString()
                        var artistPageId = document.get("artist_page_id").toString()

                        artistPageArrayList.add(ArtistPage(artistPageName, artistPageId))
                    }
                } else {

                }
            }
    }

    override fun showArtistPages(artistPagesList: ArrayList<ArtistPage>) {
        adapter?.update(artistPagesList)
    }

    fun addBlankArtistPage(){

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
