package com.example.artistmanagerapp.fragments

import android.support.v4.app.Fragment
import com.example.artistmanagerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

abstract class BaseFragment : Fragment() {

    // Firebase basic stuff
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    var user = auth.currentUser

    // Firebase Firestore paths
    val perfectUserPath = db.collection("users").document("perfectUser")
    val perfectArtistPagePath = db.collection("artist_pages").document("perfect_artistpage_id")

    val userPath = db.collection("users").document(user?.uid.toString())
    val artistsCollectionPath = userPath.collection(R.string.firestore_artistpages_collection.toString())

}