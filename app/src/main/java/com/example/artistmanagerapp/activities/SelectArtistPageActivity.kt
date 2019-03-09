package com.example.artistmanagerapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
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

        populateListWithFakeStuff()
        loadArtistPages()

        select_artist_recycler_view.layoutManager = LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false)
        select_artist_recycler_view.adapter = SelectArtistPageAdapter(artistPageArrayList)

        //Here I have to somehow upload the RecyclerView - I got the data, but the recycler view remains as it was

    }

    fun populateListWithFakeStuff(){
        artistPageArrayList.add(ArtistPage("hui"))
        artistPageArrayList.add(ArtistPage("hui2"))
    }

    fun loadArtistPages(){
        perfectUserPath.collection("artist_pages")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result){
                        artistPageArrayList.add(ArtistPage(document.get("artist_name").toString()))
                    }
                } else {

                }
            }
    }


    fun loadArtistPagesIntoRecyclerView(){
        perfectUserPath.collection("artist_pages").get().addOnSuccessListener { queryDocumentSnapshot ->
            Toast.makeText(this, queryDocumentSnapshot.documents.toString(), Toast.LENGTH_SHORT)
            if (!queryDocumentSnapshot.isEmpty){
                var artistPagesList : List <DocumentSnapshot> = queryDocumentSnapshot.documents

                for (i in artistPagesList){
                    artistPageArrayList.add(ArtistPage(i.get("artist_name").toString()))
                }

            }
        }
    }


}
