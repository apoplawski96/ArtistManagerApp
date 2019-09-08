package com.example.artistmanagerapp.models

import java.io.Serializable

class ActivityLog : Serializable{

    var userDisplayName : String? = null
    var dateCreated : String? = null
    var timeCreated : String? = null
    var eventCategory : String? = null
    var logDescription : String? = null
    var userAcronym : String? = null
    var connectedPageId : String? = null
    var createdById : String? = null

    constructor(
        userDisplayName: String?,
        dateCreated: String?,
        timeCreated : String?,
        eventCategory: String?,
        logDescription: String?,
        userAcronym : String?,
        connectedPageId : String?,
        createdById : String?
    ) {
        this.userDisplayName = userDisplayName
        this.dateCreated = dateCreated
        this.timeCreated = timeCreated
        this.eventCategory = eventCategory
        this.logDescription = logDescription
        this.userAcronym = userAcronym
        this.connectedPageId = connectedPageId
        this.createdById = createdById
    }
}