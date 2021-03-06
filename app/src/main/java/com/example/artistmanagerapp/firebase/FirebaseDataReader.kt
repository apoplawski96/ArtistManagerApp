package com.example.artistmanagerapp.firebase

import android.graphics.Bitmap
import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.*
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.RedeemCode
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.constants.FirebaseConstants

class FirebaseDataReader : BaseActivity () {

    val c = FirebaseConstants

    /// Getting user data
    fun getUserData(userID : String, userDataPresenter: UserDataPresenter){
        lateinit var person : User
        db.collection(c.USERS_COLLECTION_NAME).document(userID).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()){
                    var firstName = documentSnapshot.getString(c.FIRST_NAME).toString()
                    var lastName = documentSnapshot.getString(c.LAST_NAME).toString()
                    var artistRole = documentSnapshot.getString(c.ARTIST_ROLE).toString()
                    var pageRole = documentSnapshot.getString(c.PAGE_ROLE).toString()
                    var currentArtistPageId = documentSnapshot.getString(c.CURRENT_ARTIST_PAGE).toString()
                    var roleCategory = documentSnapshot.getString(c.ROLE_CATEGORY).toString()
                    person = User(firstName, lastName, artistRole, pageRole, currentArtistPageId, roleCategory)
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
                    var artistPageName : String? = document.get(c.ARTIST_NAME).toString()
                    var artistPageId : String? = document.get(c.ARTIST_PAGE_ID).toString()
                    var artistPageGenre : String? = document.get(c.ARTIST_GENRE).toString()
                    artistPagesOutput!!.add(ArtistPage(artistPageName, artistPageId, artistPageGenre))
                }
                Log.d("getArtistPages()", "Pages parsed: ${artistPagesOutput.toString()}")
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

    fun getArtistPageData(artistPageId : String?, presenter : ArtistPagesPresenter?, receiver: ArtistPageDataReceiver?){
        artistPagesCollectionPath.document(artistPageId.toString()).get().addOnSuccessListener {documentSnapshot ->
            if (documentSnapshot.exists()){
                val artistName = documentSnapshot.get(c.ARTIST_NAME).toString()
                val genre = documentSnapshot.get(c.ARTIST_GENRE).toString()
                val bio = documentSnapshot.get(c.ARTIST_BIO).toString()
                val fbLink = documentSnapshot.get(c.ARTIST_FB).toString()
                val instaLink = documentSnapshot.get(c.ARTIST_IG).toString()
                val contact = documentSnapshot.get(c.ARTIST_CONTACT).toString()
                val id = documentSnapshot.get(c.ARTIST_PAGE_ID).toString()
                val shareCode = documentSnapshot.get(c.ARTIST_SHARE_CODE).toString()
                val category = documentSnapshot.get(c.ARTIST_CATEGORY).toString()
                val dateCreated = documentSnapshot.get(c.ARTIST_DATE_CREATED).toString()
                val timeCreated = documentSnapshot.get(c.ARTIST_TIME_CREATED).toString()
                val createdById = documentSnapshot.get(c.ARTIST_CREATED_BY_ID).toString()
                val createdByDisplayName = documentSnapshot.get(c.ARTIST_CREATED_BY_DISPLAY_NAME).toString()
                val pageAdmins = documentSnapshot.get(c.PAGE_ADMINS) as ArrayList<String>
                val newTasks = documentSnapshot.get(c.ARTIST_NEW_TASKS) as Number
                val upcomingEvents = documentSnapshot.get(c.ARTIST_UPCOMING_EVENTS) as Number

                val artistPage = ArtistPage(artistName, id, bio, instaLink, fbLink, genre, contact, shareCode, category, dateCreated, timeCreated, createdById, createdByDisplayName, pageAdmins, newTasks, upcomingEvents)
                presenter?.showArtistPageData(artistPage)
                receiver?.callback(artistPage)
            }
        }.addOnFailureListener {

        }
    }

    fun getPageRole (userID : String, pageId : String, dataReceiver: DataReceiver){
        usersCollectionPath.document(userId).collection("artistPages").document(pageId).get().addOnSuccessListener { document ->
            if (document.exists()){
                val pageRole : String? = document.get("pageRole").toString()
                dataReceiver.receiveData(pageRole, null)
            } else {
                Log.d(FIREBASE_ERROR, "Artist page with provided pageId doesn't exist in user data")
            }
        }
    }

    fun getStorageAvatars (idList : ArrayList<String?>, dataReceiver: DataReceiver, option : String){
        var idBitmapHashMap : HashMap <String?, Bitmap?> = HashMap()

        for (id in idList){

        }
    }

}