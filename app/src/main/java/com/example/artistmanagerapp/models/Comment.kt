package com.example.artistmanagerapp.models

import java.io.Serializable

class Comment : Serializable{

    var content : String? = null
    var createdById : String? = null
    var dateCreated : String? = null
    var createdByDisplayName : String? = null

    constructor(content: String?, createdById: String?, dateCreated: String?, createdByDisplayName : String?) {
        this.content = content
        this.createdById = createdById
        this.dateCreated = dateCreated
        this.createdByDisplayName = createdByDisplayName
    }
}