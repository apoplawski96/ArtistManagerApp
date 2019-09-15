package com.example.artistmanagerapp.firebase

import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.constants.Constants
import com.example.artistmanagerapp.interfaces.ArtistPagesPresenter
import com.example.artistmanagerapp.interfaces.BundleUpdater
import com.example.artistmanagerapp.interfaces.DataReceiver
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.ShareEpkCode
import com.example.artistmanagerapp.ui.DialogCreator
import com.example.artistmanagerapp.constants.FirebaseConstants
import com.example.artistmanagerapp.utils.Utils
import com.google.firebase.firestore.SetOptions

object FirebaseElectronicPressKitHelper : BaseActivity() {

    val c = FirebaseConstants

    fun saveEpkData (epkData : HashMap<String, Any>, pageId : String?, presenter : UserInterfaceUpdater){
       artistPagesCollectionPath.document(pageId.toString()).set(epkData, SetOptions.merge()).addOnSuccessListener {
           presenter.updateUI("EPK_DATA_SUCCESSFULLY_SAVED", null)
       }.addOnFailureListener {
           presenter.updateUI("ERROR_SAVING_EPK_DATA", null)
       }
    }

    fun readEpkData (pageId: String?, presenter : ArtistPagesPresenter){
        artistPagesCollectionPath.document(pageId.toString()).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()){
                var name = documentSnapshot.get(FirebaseConstants.ARTIST_NAME).toString()
                var bio = documentSnapshot.get(FirebaseConstants.ARTIST_BIO).toString()
                var insta = documentSnapshot.get(FirebaseConstants.ARTIST_IG).toString()
                var fb = documentSnapshot.get(FirebaseConstants.ARTIST_FB).toString()
                var genre = documentSnapshot.get(FirebaseConstants.ARTIST_GENRE).toString()

                val epkData = ArtistPage(name, bio, insta, fb, genre)
                presenter.showArtistPageData(epkData)
            } else {

            }
        }
    }

    fun generateShareCode (pageId : String?, bundleUpdater: BundleUpdater){
        val redeemCodeString : String =
            Utils.generateCodeString(Constants.EPK_SHARE_CODE_LENGHT)
        val redeemCodeObject = ShareEpkCode(redeemCodeString, pageId)

        // Adding ShareCode to shareCodes collection
        epkShareCodesCollectionPath.document(redeemCodeString).set(redeemCodeObject).addOnSuccessListener {

        }

        // Updating shareCode field in ArtistPages
        artistPagesCollectionPath.document(pageId.toString()).set (mapOf(FirebaseConstants.ARTIST_SHARE_CODE to redeemCodeString), SetOptions.merge()).addOnSuccessListener {
            bundleUpdater.updateBundleData(redeemCodeString)
        }

    }

    fun redeemShareCode (shareCode : String, receiver : DataReceiver, dialogControllerCallback: DialogCreator.DialogControllerCallback) {
        epkShareCodesCollectionPath.document(shareCode).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()){
                receiver.receiveData(documentSnapshot.get("connectedPageId").toString(), dialogControllerCallback)
            } else {
                receiver.receiveData(null, dialogControllerCallback)
            }
        }
    }

    fun redeemEpkShareCode (shareCode : String, receiver : DataReceiver) {
        epkShareCodesCollectionPath.document(shareCode).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()){
                receiver.receiveData(documentSnapshot.get("connectedPageId").toString(), null)
            } else {
                receiver.receiveData(null, null)
            }
        }
    }

}