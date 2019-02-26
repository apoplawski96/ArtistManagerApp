package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.widget.TextView
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.models.Artist
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val auth = FirebaseAuth.getInstance()

        bottom_nav_bar.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        //to delete later
        val logout = findViewById(R.id.logout_text_view) as TextView
        logout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java).apply {
                //putExtra("asdasd", "asda")
            }
            startActivity(intent)
        }
    }


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when (item.itemId){
            R.id.home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.frag2 -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.frag3 -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

}
