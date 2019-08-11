package com.example.artistmanagerapp.interfaces

import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.User

interface FragmentsMessenger {

    fun sendPageData(artistPage : ArtistPage)
    fun sendUserData(user : User)

}