package com.example.artistmanagerapp.firebase

import android.util.Log
import android.widget.Toast
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.ArtistPagesPresenter
import com.example.artistmanagerapp.interfaces.DataReceiver
import com.example.artistmanagerapp.interfaces.RedeemCodeDataReceiver
import com.example.artistmanagerapp.interfaces.UserDataPresenter
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.RedeemCode
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

        userArtistPagesCollectionPath.get().addOnSuccessListener { documents ->
            if (!documents.isEmpty){
                for (document in documents){
                    var artistPageName : String? = document.get("artistName").toString()
                    artistPagesOutput!!.add(ArtistPage(artistPageName))
                }
                presenter.showArtistPages(artistPagesOutput)
            } else {
                Log.d(FIREBASE_TAG, "Doc snapshot doesn't exist")
            }
        }.addOnFailureListener{
            Log.d(FIREBASE_ERROR, "$it")
        }
    }

    // Checking if artist_pages collection exist by user record
    fun checkIfUserIsHasArtistPageLink(presenter: ArtistPagesPresenter){
        userArtistPagesCollectionPath.get().addOnSuccessListener { documents ->
            if (documents.isEmpty){
                presenter.showNoPagesText()
                Log.d(FIREBASE_TAG, "No artist_pages collection record in user database")
            }
        }.addOnFailureListener{
            Log.d(FIREBASE_ERROR, "$it")
        }
    }

    fun getRedeemCodesList(redeemCodeDataReceiver: RedeemCodeDataReceiver){
        var codeObjectsList : ArrayList <RedeemCode> = ArrayList()

        redeemCodesCollectionPath.get().addOnSuccessListener { documents ->
            if (!documents.isEmpty){
                for (document in documents){
                    var codeString : String = document.get("codeString").toString()
                    var wasUsed : Boolean = document.get("wasUsed").toString().toBoolean()
                    var artistPageId : String = document.get("artistPageId").toString()
                    var generatedById : String = document.get("generatedById").toString()
                    var redeemedById : String = document.get("redeemedById").toString()
                    codeObjectsList.add(RedeemCode(codeString, wasUsed, artistPageId, generatedById, redeemedById))
                }
                redeemCodeDataReceiver.receiveCodesList(codeObjectsList)
            }
        }
    }

    fun getRedeemCodeData(redeemCodeString : String, redeemCodeDataReceiver: RedeemCodeDataReceiver){
        redeemCodesCollectionPath.document(redeemCodeString).get().addOnSuccessListener { document ->
            if (document.exists()){
                var codeString : String = document.get("codeString").toString()
                var wasUsed : Boolean = document.get("wasUsed").toString().toBoolean()
                var artistPageId : String = document.get("artistPageId").toString()
                var generatedById : String = document.get("generatedById").toString()
                var redeemedById : String = document.get("redeemedById").toString()

                val redeemCodeObject : RedeemCode? = RedeemCode(codeString, wasUsed, artistPageId, generatedById, redeemedById)
                redeemCodeDataReceiver.redeemCode(redeemCodeObject)
            } else {
                // If document doesn't exist (so basically the code that user gave is incorrect) we send null as an argument
                redeemCodeDataReceiver.redeemCode(null)
            }
        }
    }

    fun getArtistPageData(artistPageId : String?, presenter : ArtistPagesPresenter){

    }

    fun getPageRole (userID : String, pageId : String, dataReceiver: DataReceiver){
        usersCollectionPath.document(userId).collection("artistPages").document(pageId).get().addOnSuccessListener { document ->
            if (document.exists()){
                val pageRole : String? = document.get("pageRole").toString()
                dataReceiver.receiveData(pageRole)
            } else {
                Log.d(FIREBASE_ERROR, "Artist page with provided pageId doesn't exist in user data")
            }
        }
    }

}