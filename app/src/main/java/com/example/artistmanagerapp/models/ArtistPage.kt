package com.example.artistmanagerapp.models

import java.io.Serializable

class ArtistPage : Serializable {

    // JESLI ZMIENIE COŚ W MODELU TO MUSZE TEZ KURWA ZMIENIC W READERZE

    var artistName : String? = null
    var artistPageAdminId : String? = null
    var artistPageId : String? = null
    var biography : String? = null
    var instagramLink : String? = null
    var facebookLink : String? = null
    var genre : String? = null
    var contact : String? = null
    var epkShareCode : String? = null

    constructor()

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
        this.instagramLink = instaLink
        this.facebookLink = fbLink
        this.genre = genre
    }

    constructor(artistName: String?, biography: String?, instaLink: String?, fbLink: String?, genre: String?, contact: String?) {
        this.artistName = artistName
        this.biography = biography
        this.instagramLink = instaLink
        this.facebookLink = fbLink
        this.genre = genre
        this.contact = contact
    }

    constructor(
        artistName: String?,
        artistPageAdminId: String?,
        artistPageId: String?,
        biography: String?,
        instaLink: String?,
        fbLink: String?,
        genre: String?,
        contact: String?,
        epkShareCode: String?
    ) {
        this.artistName = artistName
        this.artistPageAdminId = artistPageAdminId
        this.artistPageId = artistPageId
        this.biography = biography
        this.instagramLink = instaLink
        this.facebookLink = fbLink
        this.genre = genre
        this.contact = contact
        this.epkShareCode = epkShareCode
    }

    override fun toString(): String {
        return "*ARTIST_PAGE* id: $artistPageId, name: $artistName, adminId: $artistPageAdminId"
    }

}
