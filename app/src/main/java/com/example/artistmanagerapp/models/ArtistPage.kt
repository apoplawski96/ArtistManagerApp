package com.example.artistmanagerapp.models

class ArtistPage {

    var artistName : String? = null
    var artistPageAdminId : String? = null
    var artistPageId : String? = null
    var biography : String? = null
    var instaLink : String? = null
    var fbLink : String? = null
    var genre : String? = null


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

    constructor(artistName: String?, biography: String?, instaLink: String?, fbLink: String?, genre: String?) {
        this.artistName = artistName
        this.biography = biography
        this.instaLink = instaLink
        this.fbLink = fbLink
        this.genre = genre
    }

}
