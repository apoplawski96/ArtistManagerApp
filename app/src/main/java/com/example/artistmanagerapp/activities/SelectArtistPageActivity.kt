package com.example.artistmanagerapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.SelectArtistPageAdapter
import com.example.artistmanagerapp.models.Artist
import com.example.artistmanagerapp.models.ArtistPage
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.horizontal_select_artist_recycler_view.*

class SelectArtistPageActivity : BaseActivity() {

    private var artistPageArrayList : ArrayList <ArtistPage> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_artist_page)

        var selectArtistRecyclerView = findViewById(R.id.select_artist_recycler_view) as RecyclerView

        populateListWithFakeStuff()
        loadArtistPages()

        selectArtistRecyclerView.layoutManager = LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false)
        selectArtistRecyclerView.adapter = SelectArtistPageAdapter(artistPageArrayList)

        //Here I have to somehow upload the RecyclerView - I got the data, but the recycler view remains as it was

    }

    fun populateListWithFakeStuff(){
        artistPageArrayList.add(ArtistPage("hui"))
        artistPageArrayList.add(ArtistPage("hui2"))
    }

    fun loadArtistPages(){
        perfectUserPath.collection("artist_pages")
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents){
                        artistPageArrayList.add(ArtistPage(document.get("artist_name").toString()))
                    }
                } else {

                }
            }
    }



}
