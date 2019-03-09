package com.example.artistmanagerapp.adapters

import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.models.ArtistPage
import org.w3c.dom.Text
import java.text.FieldPosition

class SelectArtistPageAdapter (var artistPageArrayList : ArrayList<ArtistPage>) : RecyclerView.Adapter<SelectArtistPageAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.artist_page_item, parent, false)
        return ViewHolder (view)
    }

    override fun getItemCount() : Int {
        return artistPageArrayList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.artistName.text = artistPageArrayList[position].artistName
    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val artistName : TextView = itemView.findViewById(R.id.artist_name_selector) as TextView
    }

}