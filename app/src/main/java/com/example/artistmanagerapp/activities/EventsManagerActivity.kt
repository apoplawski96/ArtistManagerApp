package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.EventsGridViewAdapter
import com.example.artistmanagerapp.models.Event
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.FirebaseEventsHelper

class EventsManagerActivity : BaseActivity(), FirebaseEventsHelper.EventsPresenter {

    // Collections
    var eventList = ArrayList<Event>()

    // Current ArtistPage data
    var pageId : String? = null
    var pageName : String? = null
    var epkShareCode : String? = null

    // Views
    var gridView : GridView? = null
    var addEventButton : Button? = null

    // Others
    var adapter : EventsGridViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_manager)

        // Getting bundled ArtistPageData
        pageId = intent.getStringExtra(Constants.PAGE_ID_BUNDLE)
        pageName = intent.getStringExtra(Constants.ARTIST_NAME_BUNDLE)
        epkShareCode = intent.getStringExtra(Constants.EPK_SHARE_CODE_BUNDLE)

        // Parsing all events and sending them to updateEvents() function
        FirebaseEventsHelper().parseEvents(artistPagesCollectionPath.document(pageId.toString()).collection("events"), this)

        // Views
        gridView = findViewById(R.id.events_grid_view)
        addEventButton = findViewById(R.id.add_event_button)

        // EventsGridViewAdapter stuff
        adapter = EventsGridViewAdapter(this, eventList)
        gridView?.adapter = adapter

        // OnClicks implementations
        gridView?.setOnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(this, position.toString(), Toast.LENGTH_SHORT ).show()
        }

        addEventButton?.setOnClickListener {
            val intent = Intent(applicationContext, CreateEventActivity::class.java).apply{
                //putExtra("isDbRecordCreated", "false")
            }
            startActivity(intent)
        }
    }

    fun populateEventList(){
        eventList.add(Event("123", "Tytuul", "Miejsce", "data", Event.EventCategory.REHEARSAL, false))
        eventList.add(Event("123", "Tytuul", "Miejsce2", "data", Event.EventCategory.REHEARSAL, false))
        eventList.add(Event("123", "Tytuul", "Miejsce3", "data", Event.EventCategory.REHEARSAL, false))
    }

    fun displayNoEventsMessage(){
        // TO CODE
    }

    override fun updateEvents(eventsList: ArrayList<Event>?) {
        if (eventsList != null){
            adapter?.updateItems(eventsList)
        } else {
            displayNoEventsMessage()
        }
    }

}
