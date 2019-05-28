package com.example.artistmanagerapp.models

import com.google.firebase.storage.StorageReference

data class ArtistPage (var artistName : String?, var artistPageId : String?){
    constructor(artistName : String?, artistPageId : String?, artistPagePhotoStorageRef : StorageReference ) : this (artistName, artistPageId)
}
