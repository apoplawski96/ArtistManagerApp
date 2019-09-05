package com.example.artistmanagerapp.models

import java.io.Serializable

class Comment : Serializable{

    var content : String? = null
    var createdById : String? = null
    var dateCreated : String? = null
    var createdByDisplayName : String? = null
    var authorFirstName : String? = null
    var authorLastName : String? = null

    constructor(content: String?, createdById: String?, dateCreated: String?, createdByDisplayName : String?, authorFirstName : String?, authorLastName : String?) {
        this.content = content
        this.createdById = createdById
        this.dateCreated = dateCreated
        this.createdByDisplayName = createdByDisplayName
        this.authorFirstName = authorFirstName
        this.authorLastName = authorLastName
    }



}