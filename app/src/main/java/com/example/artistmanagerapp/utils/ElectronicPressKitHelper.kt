package com.example.artistmanagerapp.utils

import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.ArtistPagesPresenter
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.google.firebase.firestore.SetOptions

object ElectronicPressKitHelper : BaseActivity() {

    val c = FirebaseConstants

    fun saveEpkData (epkData : HashMap<String, Any>, pageId : String?, presenter : UserInterfaceUpdater){
       artistPagesCollectionPath.document(pageId.toString()).set(epkData, SetOptions.merge()).addOnSuccessListener {
           presenter.updateUI("EPK_DATA_SUCCESSFULLY_SAVED")
       }.addOnFailureListener {
           presenter.updateUI("ERROR_SAVING_EPK_DATA")
       }
    }

    fun readEpkData (pageId: String?, presenter : ArtistPagesPresenter){
        artistPagesCollectionPath.document(pageId.toString()).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()){
                var name = documentSnapshot.get(c.ARTIST_NAME).toString()
                var bio = documentSnapshot.get(c.ARTIST_BIO).toString()
                var insta = documentSnapshot.get(c.ARTIST_IG).toString()
                var fb = documentSnapshot.get(c.ARTIST_FB).toString()
                var genre = documentSnapshot.get(c.ARTIST_GENRE).toString()

                val epkData = ArtistPage(name, bio, insta, fb, genre)
                presenter.showArtistPageData(epkData)
            } else {

            }
        }
    }

}