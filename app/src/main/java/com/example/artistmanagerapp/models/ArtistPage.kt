package com.example.artistmanagerapp.models

class ArtistPage {

    var artistName : String? = null
    var artistPageAdminId : String? = null
    var artistPageId : String? = null

    constructor(mArtistName: String?){
        this.artistName = mArtistName
    }

    constructor(mArtistName: String?, mArtistPageId: String?){
        this.artistName = mArtistName
        this.artistPageId = mArtistPageId
    }

    constructor(artistName: String?, artistPageAdminId: String?, artistPageId: String?) {
        this.artistName = artistName
        this.artistPageAdminId = artistPageAdminId
        this.artistPageId = artistPageId
    }

}
