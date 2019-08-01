package com.example.artistmanagerapp.models

class ArtistPage {

    var artistName : String? = null
    var id : String? = null
    var artistPageAdminId : String? = null

    constructor(mArtistName: String?){
        this.artistName = mArtistName
    }

    constructor(mArtistName: String?, mArtistPageAdminId : String?){
        this.artistName = mArtistName
        this.artistPageAdminId = mArtistPageAdminId
    }

    constructor(mArtistName: String?, mId: String?, mArtistPageAdminId : String?) {
        this.artistName = mArtistName
        this.id = mId
        this.artistPageAdminId = mArtistPageAdminId
    }
}
