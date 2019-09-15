package com.example.artistmanagerapp.firebase

import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.User
import com.google.firebase.firestore.FieldValue

object FirebaseArtistPagesHelper : BaseActivity( ){

    fun giveUserAdminRights(targetUser : User?, artistPage: ArtistPage?, givenBy : User?){
        val pageId = artistPage!!.artistPageId.toString()
        val targetUserId = targetUser!!.id.toString()

        artistPagesCollectionPath.document(pageId).update("pageAdmins", FieldValue.arrayUnion(targetUserId)).addOnSuccessListener {

        }.addOnFailureListener {

        }
    }

    fun removeMember(){

    }

}