package com.example.artistmanagerapp.interfaces

import com.example.artistmanagerapp.models.ArtistPage

interface ArtistPageDataReceiver {
    fun callback(artistPage : ArtistPage)
}