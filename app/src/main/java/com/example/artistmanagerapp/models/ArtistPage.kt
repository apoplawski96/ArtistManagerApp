package com.example.artistmanagerapp.models

import java.io.Serializable

class ArtistPage : Serializable {

    var artistName : String? = null
    var artistPageAdminId : String? = null
    var artistPageId : String? = null
    var biography : String? = null
    var instagramLink : String? = null
    var facebookLink : String? = null
    var genre : String? = null
    var contact : String? = null
    var epkShareCode : String? = null
    var pageCategory : String? = null
    var dateCreated : String? = null
    var timeCreated : String? = null
    var createdById : String? = null
    var createdByDisplayName : String? = null
    var pageAdmins : ArrayList<String>? = null
    var newTasks : Number? = 0
    var upcomingEvents : Number? = 0

    constructor()

    constructor(mArtistName: String?){
        this.artistName = mArtistName
    }

    constructor(mArtistName: String?, mArtistPageId: String?){
        this.artistName = mArtistName
        this.artistPageId = mArtistPageId
    }

    constructor(mArtistName: String?, mArtistPageId: String?, genre: String?){
        this.artistName = mArtistName
        this.artistPageId = mArtistPageId
        this.genre = genre
    }

    constructor(artistName: String?, biography: String?, instaLink: String?, fbLink: String?, genre: String?) {
        this.artistName = artistName
        this.biography = biography
        this.instagramLink = instaLink
        this.facebookLink = fbLink
        this.genre = genre
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

    constructor(
        artistName: String?,
        genre: String?,
        pageCategory: String?,
        dateCreated: String?,
        timeCreated: String?,
        createdById: String?,
        createdByDisplayName : String
    ) {
        this.artistName = artistName
        this.genre = genre
        this.pageCategory = pageCategory
        this.dateCreated = dateCreated
        this.timeCreated = timeCreated
        this.createdById = createdById
        this.createdByDisplayName = createdByDisplayName
    }

    constructor(
        artistName: String?,
        artistPageId: String?,
        biography: String?,
        instagramLink: String?,
        facebookLink: String?,
        genre: String?,
        contact: String?,
        epkShareCode: String?,
        pageCategory: String?,
        dateCreated: String?,
        timeCreated: String?,
        createdById: String?,
        createdByDisplayName: String?,
        pageAdmins : ArrayList<String>,
        newTasks: Number?,
        upcomingEvents: Number?
    ) {
        this.artistName = artistName
        this.artistPageId = artistPageId
        this.biography = biography
        this.instagramLink = instagramLink
        this.facebookLink = facebookLink
        this.genre = genre
        this.contact = contact
        this.epkShareCode = epkShareCode
        this.pageCategory = pageCategory
        this.dateCreated = dateCreated
        this.timeCreated = timeCreated
        this.createdById = createdById
        this.createdByDisplayName = createdByDisplayName
        this.pageAdmins = pageAdmins
        this.newTasks = newTasks
        this.upcomingEvents = upcomingEvents
    }


    override fun toString(): String {
        return "*ARTIST_PAGE* id: $artistPageId, name: $artistName, adminId: $artistPageAdminId"
    }

}
