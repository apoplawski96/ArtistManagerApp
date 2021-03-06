package com.example.artistmanagerapp.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.constants.FirebaseConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

abstract class BaseFragment : Fragment() {

    // Firebase basic stuff
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    var user = auth.currentUser
    val userIdGlobal = auth.currentUser?.uid.toString()

    // Firebase Firestore paths
    val perfectUserPath = db.collection("users").document("perfectUser")
    val perfectArtistPagePath = db.collection("artist_pages").document("perfect_artistpage_id")

    val userPath = db.collection("users").document(user?.uid.toString())
    val artistsCollectionPath = userPath.collection(R.string.firestore_artistpages_collection.toString())
    val artistPagesCollectionPath = db.collection(FirebaseConstants.ARTIST_PAGES_COLLECTION_NAME)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        //Toast.makeText(activity, "Current user Id: ${user?.uid}", Toast.LENGTH_SHORT).show()
    }

}