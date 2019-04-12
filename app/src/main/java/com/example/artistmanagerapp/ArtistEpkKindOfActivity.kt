package com.example.artistmanagerapp

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.graphics.Palette
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem

class ArtistEpkKindOfActivity : AppCompatActivity() {

    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_epk_kind_of)

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        collapsingToolbarLayout!!.title = "NOT ENOUGH"

        collapsingToolbarLayout!!.setExpandedTitleMargin(40,40,40,40)

        collapsingToolbarLayout!!.setExpandedTitleTextAppearance(R.style.ExpandedText)
        collapsingToolbarLayout!!.setCollapsedTitleTextAppearance(R.style.CollapsedText)

        dynamicColor()

    }

    private fun dynamicColor() {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.avatar)
        Palette.from(bitmap).generate { palette ->
            collapsingToolbarLayout!!.setContentScrimColor(palette!!.getMutedColor(resources.getColor(R.color.colorPrimary)))
            collapsingToolbarLayout!!.setStatusBarScrimColor(palette!!.getMutedColor(resources.getColor(R.color.colorAccent)))
        }
    }

}
