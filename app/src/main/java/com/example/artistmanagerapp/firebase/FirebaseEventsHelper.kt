package com.example.artistmanagerapp.firebase

import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions

class FirebaseEventsHelper : BaseActivity() {

    /*fun parseEvents (pathToEventsCollection : CollectionReference, presenter : EventsPresenter){
        var eventsOutput : ArrayList<Event> = ArrayList()

        pathToEventsCollection.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents){
                        // Need to work on assignees list later
                        val id : String? = document.get("eventId").toString()
                        val title : String? = document.get("eventTitle").toString()
                        val date : String? = document.get("eventDate").toString()
                        val location : String? = document.get("eventPlace").toString()
                        var category : Event.EventCategory? = document.get("category") as Event.EventCategory
                        var isPast : Boolean? = document.get("isPast") as Boolean
                        eventsOutput.add(Event(id, title, location, date, category, isPast))
                    }
                    presenter.updateEvents(eventsOutput)
                } else {
                    presenter.updateEvents(null)
                }
            }
    }

    fun createEvent (pathToEventsCollection: CollectionReference, event : Event?, presenter: EventsPresenter){
        val mEvent = event as Event

        // Adding Event to Page events collection
        pathToEventsCollection.document().set(mEvent, SetOptions.merge()).addOnSuccessListener {
            Log.d(FIREBASE_TAG, "Event successfully added")
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }
    }

    interface EventsPresenter{
        fun updateEvents (eventsList : ArrayList<Event>?)
    }*/

}