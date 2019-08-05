package com.example.artistmanagerapp.firebase

import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.RedeemCode
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.FirebaseConstants
import com.example.artistmanagerapp.utils.Utils
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions

class FirebaseDataWriter : BaseActivity(){

    private val const = Constants
    private val c = FirebaseConstants

    fun addUserDataToDbAndUpdateUi(collectionPath : CollectionReference, dataMap : HashMap<String, Any>, userId : String, uiUpdater: UserInterfaceUpdater){
        collectionPath.document(userId).set(dataMap, SetOptions.merge()).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Data successfully added: $dataMap")
            uiUpdater.updateUI(const.USER_ADDED_TO_DB)
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }
    }

    fun createArtistPage(artistPage : ArtistPage, uiUpdater: UserInterfaceUpdater, userId: String){
        // Setting up data
        val pageId = artistPagesCollectionPath.document().id
        val userInfo = User(userId, "admin") // PAGE ROLE SOMEHOW DOESN'T ADD TO DATABASE
        var artistPageInfo = HashMap<String, Any>()
        artistPage.artistPageId = pageId
        artistPage.artistPageAdminId = userId
        artistPageInfo.put(c.ARTIST_NAME, artistPage.artistName.toString())
        artistPageInfo.put(c.ARTIST_PAGE_ID, pageId)
        artistPageInfo.put(c.ARTIST_PAGE_ADMIN_ID, userId)


        // Adding page record to artistPages collection
        artistPagesCollectionPath.document(pageId).set(artistPage, SetOptions.merge()).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Artist page successfully created: $artistPage")
            uiUpdater.updateUI(const.ARTIST_PAGE_CREATED)
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }

        // Adding user record and admin info to artist page record
        artistPagesCollectionPath.document(pageId).collection("pageMembers").document(userId).set(userInfo, SetOptions.merge()).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "User info successfully added to page_members collection: $userInfo")
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }

        // Adding artist page link to user data
        userPath.collection(c.ARTIST_PAGES_COLLECTION_NAME).document(pageId).set(artistPageInfo, SetOptions.merge()).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Artist page info successfully added to user record: $artistPageInfo")
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }

        // Setting up currentArtistPage
        userPath.set(mapOf(c.CURRENT_ARTIST_PAGE to pageId), SetOptions.merge()).addOnSuccessListener {  }.addOnFailureListener {  }

    }

    fun generateRedeemCode(redeemCodeString : String, userId : String, artistPageId : String){
        val redeemCodeObject = RedeemCode (redeemCodeString, false, artistPageId, userId, null)

        redeemCodesCollectionPath.document(redeemCodeString).set(redeemCodeObject).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Code successfully added to db: $redeemCodeObject")
        }
    }

    fun markCodeAsRedeemed(redeemCodeString : String, redeemedById : String){
        var updateData = HashMap <String, Any>()
        updateData.put("wasUsed", true)
        updateData.put("redeemedById", redeemedById)

        redeemCodesCollectionPath.document(redeemCodeString).update(updateData).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Data successfully updated: $updateData")
        }
    }

    fun addArtistReferenceToUserRecord(userId : String, artistPageId : String?){
        val initData : HashMap<String, Any?> = HashMap()
        initData.put("pageRole", null)

        usersCollectionPath.document(userId).collection("artistPages").document(artistPageId.toString()).set(initData).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Artist page info successfully added to user record: $initData")
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }
    }

    fun addMemberToArtistPage(userId : String, pageId : String){
        val userData : HashMap<String, Any?>  = HashMap()
        userData.put("pageRole", null)

        artistPagesCollectionPath.document(pageId).collection("pageMembers").document(userId).set(userData).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "User info successfully added to page_members collection: $userData")
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }
    }

    fun updatePageRole(userId: String, pageId : String, pageRole : String?){
        usersCollectionPath.document(userId).collection("artistPages").document(pageId).update(mapOf("pageRole" to pageRole))
    }

}