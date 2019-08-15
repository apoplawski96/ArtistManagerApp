package com.example.artistmanagerapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.models.Event
import com.example.artistmanagerapp.models.Task
import kotlinx.android.synthetic.main.item_event.view.*

class EventsGridViewAdapter : BaseAdapter {
    var eventsList = ArrayList<Event>()
    var context : Context? = null

    constructor(context: Context, eventsList : ArrayList<Event>) : super(){
        this.context = context
        this.eventsList = eventsList
    }

    override fun getView(position: Int, convertView: View?, p2: ViewGroup?): View {
        val event = this.eventsList[position]

        var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var eventView = inflater.inflate(R.layout.item_event, null)

        eventView.event_title.text = event.eventTitle
        eventView.event_location.text = event.eventPlace

        return eventView
    }

    override fun getItem(p0: Int): Any {
        return eventsList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return eventsList.size
    }

    fun updateItems(newEvents : ArrayList<Event>){
        eventsList = newEvents
        notifyDataSetChanged()
    }

}