package com.example.artistmanagerapp.models

import java.io.Serializable

class Comment : Serializable{

    var content : String? = null
    var createdBy : String? = null
    var dateCreated : String? = null

    constructor(content: String?, createdBy: String?, dateCreated: String?) {
        this.content = content
        this.createdBy = createdBy
        this.dateCreated = dateCreated
    }
}