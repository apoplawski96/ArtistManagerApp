package com.example.artistmanagerapp.models

class Event {

    enum class EventCategory{
        REHEARSAL,
        MEETING,
        LIVE_SHOW
    }

    var eventId : String? = null
    var eventTitle : String? = null
    var eventPlace : String? = null
    var eventDate : String? = null
    var category : EventCategory? = null
    var isPast : Boolean? = null

    constructor(eventId: String?, eventTitle: String?, eventPlace: String?, eventDate: String?, category: EventCategory?, isPast: Boolean?) {
        this.eventId = eventId
        this.eventTitle = eventTitle
        this.eventPlace = eventPlace
        this.eventDate = eventDate
        this.category = category
        this.isPast = isPast
    }
}