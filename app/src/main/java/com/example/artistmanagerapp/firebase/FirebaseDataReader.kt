package com.example.artistmanagerapp.firebase

import android.util.Log
import android.widget.Toast
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.ArtistPagesPresenter
import com.example.artistmanagerapp.interfaces.UserDataPresenter
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.google.firebase.storage.StorageReference

class FirebaseDataReader : BaseActivity () {

    /// Getting user data
    fun getUserData(userID : String, userDataPresenter: UserDataPresenter){
        lateinit var person : User
        Log.d("chuj", "tujeste")
        db.collection("users").document("perfectUser").get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()){
                    var firstName = documentSnapshot.getString("first_name").toString()
                    var lastName = documentSnapshot.getString("last_name").toString()
                    var artistRole = documentSnapshot.getString("artist_role").toString()
                    var pageRole = documentSnapshot.getString("page_role").toString()
                    person = User(firstName, lastName, artistRole, pageRole)
                }
                Log.d("chuj", documentSnapshot.data.toString())
                userDataPresenter.showUserData(person)
            }.addOnFailureListener { exception ->
                Log.d("ERROR", "get failed with ", exception)
            }
    }

    // Getting artist pages
    fun getArtistPages(presenter: ArtistPagesPresenter) {
        var artistPagesOutput : ArrayList<ArtistPage> = ArrayList()

        artistPagesCollectionPath.get().addOnSuccessListener { documents ->
            if (!documents.isEmpty){
                for (document in documents){
                    var artistPageName : String? = document.get("artist_page_name").toString()

                    artistPagesOutput!!.add(ArtistPage(artistPageName))
                }
                presenter.showArtistPages(artistPagesOutput)

            } else {
                Log.d(FIREBASE_TAG, "Doc snapshot doesn't egsist")
            }
        }.addOnFailureListener{
            Log.d(FIREBASE_ERROR, "$it")
        }
    }

    fun checkIfUserIsHasArtistPageLink(){
        artistPagesCollectionPath.get().addOnSuccessListener { documents ->
            if (!documents.isEmpty){
                //Toast.makeText(this, "Cosjest", Toast.LENGTH_SHORT).show()
                //goToCreateOrJoinActivity()
            } else {
                // Getting and checking the "profileCompletionStatus" value
                //Toast.makeText(this, "Nic nie ma", Toast.LENGTH_SHORT).show()
                //goToSelectArtistPageActivity()
            }
        }.addOnFailureListener{

        }
    }

}