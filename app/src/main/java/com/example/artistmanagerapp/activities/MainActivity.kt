package com.example.artistmanagerapp.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.firebase.FirebaseDataReader
import com.example.artistmanagerapp.fragments.GridMenuFragment
import com.example.artistmanagerapp.fragments.HomeFragment
import com.example.artistmanagerapp.fragments.UserProfileFragment
import com.example.artistmanagerapp.interfaces.ArtistPageDataReceiver
import com.example.artistmanagerapp.interfaces.DataReceiver
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.User
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import com.example.artistmanagerapp.constants.Constants


class MainActivity : BaseActivity(), DataReceiver, ArtistPageDataReceiver{

    // Others
    val ACT_TAG = "MainActivity"

    // Bundle objects
    var userBundleInstance : User? = null
    var artistPageInstance : ArtistPage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Getting bundled data
        userBundleInstance = intent.extras.getSerializable(Constants.BUNDLE_USER_INSTANCE) as User?
        artistPageInstance = intent.extras.getSerializable(Constants.BUNDLE_ARTIST_PAGE_INSTANCE) as ArtistPage?

        Log.d(ACT_TAG, "MainActivity entered")
        Log.d(ACT_TAG, "User data from bundle: ${userBundleInstance?.firstName}, ${userBundleInstance?.lastName}, ${userBundleInstance?.email}")
        Log.d(ACT_TAG, "Page instance from bundle: ${artistPageInstance?.artistPageId}, ${artistPageInstance?.artistName}")
        Log.d(ACT_TAG, "Current page from bundle: ${userBundleInstance?.currentArtistPageId}")

        // TO CHANGE LATER
        //val mUserId = auth.currentUser?.uid
        //FirebaseUsersManager.getCurrentArtistPage(userIdGlobal, this)

        FirebaseDataReader().getArtistPageData(userBundleInstance?.currentArtistPageId, null, this)

        bottom_nav_bar.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when (item.itemId){
            R.id.home -> {
                replaceFragment(HomeFragment.newInstance(userBundleInstance?.currentArtistPageId.toString(), artistPageInstance as ArtistPage, userBundleInstance as User))
                return@OnNavigationItemSelectedListener true
            }
            R.id.frag2 -> {
                replaceFragment(GridMenuFragment.newInstance(userBundleInstance?.currentArtistPageId.toString(), artistPageInstance as ArtistPage, userBundleInstance as User))
                return@OnNavigationItemSelectedListener true
            }
            R.id.frag3 -> {
                replaceFragment(UserProfileFragment.newInstance(userBundleInstance?.currentArtistPageId.toString(), userBundleInstance, artistPageInstance as ArtistPage))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun replaceFragment(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null).commit()
    }

    override fun receiveData(data: Any?, mInterface: Any?) {
        if (data == null){
            val intent = Intent(applicationContext, SelectArtistPageActivity::class.java)
            startActivity(intent)
        }
        else {
            var pageId = data.toString()
            FirebaseDataReader().getArtistPageData(pageId, null, this)
        }
    }

    override fun callback(artistPage : ArtistPage) {
        Log.d("MainActivity - getPageInfo() -> receiver", "${artistPage.artistPageId} : ${artistPage.artistName}")
        artistPageInstance = artistPage
        //Toast.makeText(this, artistPage.artistPageId, Toast.LENGTH_SHORT).show()
        replaceFragment(HomeFragment.newInstance(userBundleInstance?.currentArtistPageId.toString(), artistPageInstance as ArtistPage, userBundleInstance as User))
    }

}
