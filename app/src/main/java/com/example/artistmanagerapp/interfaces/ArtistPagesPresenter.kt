package com.example.artistmanagerapp.interfaces

import com.example.artistmanagerapp.models.ArtistPage

interface ArtistPagesPresenter {
    fun showArtistPages(artistPagesList : ArrayList <ArtistPage>)
}