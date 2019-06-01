package com.example.artistmanagerapp.models

class ArtistPage {

    var artistName : String? = null
    var id : String? = null
    var artistPageAdmin : User? = null

    constructor(mArtistName: String?){
        this.artistName = mArtistName
    }

    constructor(mArtistName: String?, mId : String?){
        this.artistName = mArtistName
        this.id = mId
    }

    constructor(mArtistName: String?, mId: String?, mArtistPageAdmin : User?) {
        this.artistName = mArtistName
        this.id = mId
        this.artistPageAdmin = mArtistPageAdmin
    }
}
