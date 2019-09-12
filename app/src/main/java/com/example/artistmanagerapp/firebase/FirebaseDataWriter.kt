package com.example.artistmanagerapp.firebase

import android.graphics.Bitmap
import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.DataReceiver
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.RedeemCode
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.ui.DialogCreator
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.FirebaseConstants
import com.example.artistmanagerapp.utils.Utils
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import java.io.ByteArrayOutputStream

class FirebaseDataWriter : BaseActivity(){

    private val const = Constants
    private val c = FirebaseConstants

    fun addUserDataToDbAndUpdateUi(collectionPath : CollectionReference, dataMap : HashMap<String, Any>, userId : String, uiUpdater: UserInterfaceUpdater, avatarBitmap: Bitmap?){

        // Initializing user record in database
        collectionPath.document(userId).set(dataMap, SetOptions.merge()).addOnSuccessListener {
            Log.d("ADD_USER_TO_DB", dataMap.get("category").toString())
            Log.d(FIREBASE_TAG, "Data successfully added: $dataMap")
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }

        // Uploading user avatar and triggering UI update in the corresponding activity
        val bytes = ByteArrayOutputStream()
        avatarBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val data = bytes.toByteArray()

        var uploadTask = storageRef.child("avatars/$userId/avatar.jpg").putBytes(data)
        uploadTask.addOnSuccessListener {
            uiUpdater.updateUI(const.USER_ADDED_TO_DB, null)
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }

    }

    fun updateArtistPageData(pageId : String, newData : Map<String, Any>, uiUpdater: UserInterfaceUpdater){
        artistPagesCollectionPath.document(pageId).update(newData).addOnSuccessListener {
            uiUpdater.updateUI(Constants.ARTIST_PAGE_UPDATED, null)
        }.addOnFailureListener {

        }
    }


    fun createNewArtistPageRecord(artistPage : ArtistPage, user : User, photoBitmap: Bitmap?, uiUpdater: UserInterfaceUpdater){
        val pageId = artistPagesCollectionPath.document().id
        val batch = db.batch()

        // Adding page record to artistPages collection
        val pageRef = artistPagesCollectionPath.document(pageId)
        batch.set(pageRef, artistPage)

        // Adding user record to artistPage -> pageMembers collection
        val pageMemberRef = pageRef.collection("pageMembers").document(user.id.toString())
        batch.set(pageMemberRef, user)

        // Linking artistPage with user data
        val linkedPageRef = userPath.collection(c.ARTIST_PAGES_COLLECTION_NAME).document(pageId)
        batch.set(linkedPageRef, artistPage)

        // Setting up currentArtistPageId
        batch.update(userPath, mapOf(c.CURRENT_ARTIST_PAGE to pageId))

        // Setting up and uploading page avatar
        val bytes = ByteArrayOutputStream()
        photoBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val data = bytes.toByteArray()

        var uploadTask = storageRef.child("pageAvatars/$pageId/avatar.jpg").putBytes(data)
        uploadTask.addOnSuccessListener {
            batch.commit().addOnSuccessListener {
                uiUpdater.updateUI(const.ARTIST_PAGE_CREATED, artistPage)
            }
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }
    }

    fun createArtistPage(artistPage : ArtistPage, uiUpdater: UserInterfaceUpdater, user: User?, photoBitmap: Bitmap?){
        // Creating and capturing new Page id
        val pageId = artistPagesCollectionPath.document().id

        // Setting up User data
        //val userInfo = User(userId, "admin")
        val mUser = user as User

        // Settin up Page data
        var artistPageInfo = HashMap<String, Any>()
        artistPageInfo.put(c.ARTIST_NAME, artistPage.artistName.toString())
        artistPageInfo.put(c.ARTIST_PAGE_ID, pageId)
        artistPageInfo.put(c.ARTIST_PAGE_ADMIN_ID, userId)
        artistPageInfo.put(c.ARTIST_GENRE, artistPage.genre.toString())

        artistPage.artistPageId = pageId
        artistPage.artistPageAdminId = userId
        artistPage.pageAdmins = arrayListOf(user.id.toString())

        // Setting up photo
        val bytes = ByteArrayOutputStream()
        photoBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val data = bytes.toByteArray()

        // Adding page record to artistPages collection
        artistPagesCollectionPath.document(pageId).set(artistPage, SetOptions.merge()).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Artist page successfully created: $artistPage")
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }

        // Adding user record and admin info to artist page record
        artistPagesCollectionPath.document(pageId).collection("pageMembers").document(userId).set(mUser, SetOptions.merge()).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "User info successfully added to page_members collection: $mUser")
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

        // Uploading page avatar
        var uploadTask = storageRef.child("pageAvatars/$pageId/avatar.jpg").putBytes(data)
        uploadTask.addOnSuccessListener {
            uiUpdater.updateUI(const.ARTIST_PAGE_CREATED, artistPage)
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }

        // Creating an activity log
        logsManager.createActivityLog(user, artistPage.artistPageId.toString(), null, FirebaseActivityLogsManager.ActivityLogCategory.PAGE_CREATED)
    }

    fun generateRedeemCode(redeemCodeString : String, userId : String, artistPageId : String){
        val redeemCodeObject = RedeemCode (redeemCodeString, false, artistPageId, userId, null)

        redeemCodesCollectionPath.document(redeemCodeString).set(redeemCodeObject).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Code successfully added to db: $redeemCodeObject")
        }.addOnFailureListener {
            Log.d(FIREBASE_TAG, "Error occured: $it")
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

        usersCollectionPath.document(userId).collection("artistPages").document(artistPageId.toString()).set(initData, SetOptions.merge()).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Artist page info successfully added to user record: $initData")
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }

    }

    // TO FIX
    fun addMemberToArtistPage(userId : String, pageId : String, user : User, uiUpdater: UserInterfaceUpdater){
        val mUser = user

        // Adding user record and admin info to artist page record
        artistPagesCollectionPath.document(pageId).collection("pageMembers").document(userId).set(mUser, SetOptions.merge()).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "User info successfully added to page_members collection: $mUser")
            uiUpdater.updateUI(Constants.CODE_SUCCESSFULLY_REDEEMED, pageId)
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }

    }

    fun updatePageRole(userId: String, pageId : String, pageRole : String?){
        usersCollectionPath.document(userId).collection("artistPages").document(pageId).update(mapOf("pageRole" to pageRole))
    }

}