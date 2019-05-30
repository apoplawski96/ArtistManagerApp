package com.example.artistmanagerapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.artistmanagerapp.R

class CheckArtistPageLinkActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_artist_page_link)

        checkIfUserIsHasArtistPageLink()
    }

    fun goToCreateOrJoinActivity(){
        val intent = Intent(this, CreateOrJoinActivity::class.java)
        startActivity(intent)
    }

    fun goToSelectArtistPageActivity(){
        val intent = Intent(this, SelectArtistPageActivity::class.java)
        startActivity(intent)
    }

    fun checkIfUserIsHasArtistPageLink() {
        artistPagesCollectionPath.document("perfect_artist_page_id").get().addOnSuccessListener { docSnapshot ->
            if (docSnapshot.exists()){
                Toast.makeText(this, "Hujnicniema", Toast.LENGTH_SHORT).show()
                goToCreateOrJoinActivity()
            } else {
                // Getting and checking the "profileCompletionStatus" value
                Toast.makeText(this, "Cosjest", Toast.LENGTH_SHORT).show()
                goToSelectArtistPageActivity()
            }
        }.addOnFailureListener{

        }
    }

}
