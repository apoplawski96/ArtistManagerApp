package com.example.artistmanagerapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.artistmanagerapp.R

class CreateEventActivity : AppCompatActivity() {

    // Current ArtistPage data
    var pageId : String? = null
    var pageName : String? = null
    var epkShareCode : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
    }
}
