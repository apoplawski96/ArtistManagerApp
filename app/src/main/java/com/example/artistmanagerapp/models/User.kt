package com.example.artistmanagerapp.models

class User {

    var id : String? = null
    var firstName : String? = null
    var lastName : String? = null
    var pageRole : String? = null
    var artistRole : String? = null
    var artistPages : ArrayList<ArtistPage>? = null
    var currentArtistPageId : String? = null
    var email : String? = null

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


}