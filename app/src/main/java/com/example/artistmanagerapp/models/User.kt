package com.example.artistmanagerapp.models

import java.io.Serializable

class User : Serializable {

    var id : String? = null
    var firstName : String? = null
    var lastName : String? = null
    var pageRole : String? = null
    var artistRole : String? = null
    var roleCategory : String? = null
    var artistPages : ArrayList<ArtistPage>? = null
    var currentArtistPageId : String? = null
    var email : String? = null
    var profileCompletionStatus : String? = null
    var tasksCompleted : Int? = 0
    var eventsAttended : Int? = 0
    var eventsCreated : Int? = 0
    var assignmentsPending : Int? = 0

    constructor()

    constructor(id: String?, firstName: String?, lastName: String?) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
    }

    constructor(id: String?, pageRole: String?) {
        this.id = id
        this.pageRole = pageRole
    }

    constructor(firstName: String?, lastName: String?, artistRole: String?, pageRole: String?) {
        this.firstName = firstName
        this.lastName = lastName
        this.pageRole = pageRole
        this.artistRole = artistRole
    }

    constructor(firstName: String?, lastName: String?, artistRole: String?, pageRole: String?, currentArtistPageId: String?) {
        this.firstName = firstName
        this.lastName = lastName
        this.pageRole = pageRole
        this.artistRole = artistRole
        this.currentArtistPageId = currentArtistPageId
    }

    constructor(id: String?, firstName: String?, lastName: String?, pageRole: String?, artistRole: String?, artistPages: ArrayList<ArtistPage>?, currentArtistPageId: String?, email: String?) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.pageRole = pageRole
        this.artistRole = artistRole
        this.artistPages = artistPages
        this.currentArtistPageId = currentArtistPageId
        this.email = email
    }

    constructor(
        id: String?,
        firstName: String?,
        lastName: String?,
        pageRole: String?,
        artistRole: String?,
        roleCategory: String?,
        artistPages: ArrayList<ArtistPage>?,
        currentArtistPageId: String?,
        email: String?
    ) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.pageRole = pageRole
        this.artistRole = artistRole
        this.roleCategory = roleCategory
        this.artistPages = artistPages
        this.currentArtistPageId = currentArtistPageId
        this.email = email
    }

    constructor(
        id: String?,
        firstName: String?,
        lastName: String?,
        pageRole: String?,
        artistRole: String?,
        roleCategory: String?,
        artistPages: ArrayList<ArtistPage>?,
        currentArtistPageId: String?,
        email: String?,
        profileCompletionStatus: String?,
        tasksCompleted: Int?,
        eventsAttended: Int?,
        eventsCreated: Int?,
        assignmentsPending: Int?
    ) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.pageRole = pageRole
        this.artistRole = artistRole
        this.roleCategory = roleCategory
        this.artistPages = artistPages
        this.currentArtistPageId = currentArtistPageId
        this.email = email
        this.profileCompletionStatus = profileCompletionStatus
        this.tasksCompleted = tasksCompleted
        this.eventsAttended = eventsAttended
        this.eventsCreated = eventsCreated
        this.assignmentsPending = assignmentsPending
    }


}