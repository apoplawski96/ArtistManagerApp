package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
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
import com.example.artistmanagerapp.interfaces.FragmentsMessenger
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.UsersHelper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import android.R.id.message
import android.widget.Toast
import com.example.artistmanagerapp.interfaces.ArtistPagesPresenter


class MainActivity : BaseActivity(), DataReceiver, ArtistPageDataReceiver{

    // ArtistPage info
    var artistPageInstance : ArtistPage = ArtistPage()
    var pageId : String? = null
    var pageName : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UsersHelper.getCurrentArtistPage(userId, this)
        bottom_nav_bar.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when (item.itemId){
            R.id.home -> {
                replaceFragment(HomeFragment.newInstance(pageId.toString(), artistPageInstance))
                return@OnNavigationItemSelectedListener true
            }
            R.id.frag2 -> {
                replaceFragment(GridMenuFragment.newInstance(pageId.toString(), artistPageInstance))
                return@OnNavigationItemSelectedListener true
            }
            R.id.frag3 -> {
                replaceFragment(UserProfileFragment.newInstance(pageId.toString()))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun replaceFragment(fragment : Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    override fun receiveData(data: Any?) {
        if (data == null){
            val intent = Intent(applicationContext, SelectArtistPageActivity::class.java)
            startActivity(intent)
        }
        else {
            pageId = data.toString()
            FirebaseDataReader().getArtistPageData(pageId, null, this)
        }
    }

    override fun callback(artistPage : ArtistPage) {
        artistPageInstance = artistPage
        Toast.makeText(this, artistPage.artistPageId, Toast.LENGTH_SHORT).show()
        replaceFragment(HomeFragment.newInstance(pageId.toString(), artistPageInstance))
    }

}
