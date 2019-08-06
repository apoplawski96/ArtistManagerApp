package com.example.artistmanagerapp.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.activities.MainActivity
import com.example.artistmanagerapp.activities.SelectArtistPageActivity
import com.example.artistmanagerapp.activities.TaskDetailsActivity
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.firebase.FirebaseDataReader
import com.example.artistmanagerapp.interfaces.ArtistPagesPresenter
import com.example.artistmanagerapp.interfaces.DataReceiver
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.interfaces.UserDataPresenter
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.TaskHelper
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment : BaseFragment(), UserDataPresenter, DataReceiver, ArtistPagesPresenter {

    // Views
    var helloUser : TextView? = null
    var thisIsBandName : TextView? = null
    var bandAvatar : ImageView? = null
    var tasksCounter : TextView? = null
    var eventsCounter : TextView? = null
    var assignmentsCounter : TextView? = null

    companion object {
        @JvmStatic
        fun newInstance(pageId : String) : HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle().apply{ putString ("PAGE_ID", pageId) }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val pageId = arguments?.getString("PAGE_ID").toString()

        // Views
        helloUser = rootView.findViewById(R.id.hello_user)
        thisIsBandName = rootView.findViewById(R.id.this_is_band_name)

        // Load band photo
        //val options = RequestOptions()
        //options.centerCrop()
        //Glide.with(this).load(R.mipmap.band_photo_avatar).apply(options).into(bandAvatar)

        // Show user data
        FirebaseDataReader().getUserData(user?.uid.toString(), this)

        // Load avatar
        val storageRef = FirebaseStorage.getInstance().reference
        val avatarPath = storageRef.child("avatars/perfectUser/avatar.jpg")
        //Glide.with(this).load(R.mipmap.avatar).into(userAvatar)

        return rootView
    }

    override fun showUserData(userData: User) {
    }

    fun initUI(){

    }

    override fun receiveData(data: Any?) {
        if (data == null){
            val intent = Intent(activity, SelectArtistPageActivity::class.java)
            startActivity(intent)
        }
    }

    override fun showArtistPageData(artistPage: ArtistPage) {
        thisIsBandName?.text = artistPage.artistName
    }

    override fun showNoPagesText() {

    }

    override fun showArtistPages(artistPagesList: ArrayList<ArtistPage>) {

    }

}
