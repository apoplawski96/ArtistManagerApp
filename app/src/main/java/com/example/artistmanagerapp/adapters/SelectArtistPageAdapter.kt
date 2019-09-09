package com.example.artistmanagerapp.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.models.ArtistPage
import kotlinx.android.synthetic.main.item_artist_page.view.*

class SelectArtistPageAdapter (var artistPageArrayList : ArrayList<ArtistPage>, val clickListener : (ArtistPage) -> Unit) : RecyclerView.Adapter<SelectArtistPageAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent : ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_artist_page, parent, false)
        return ViewHolder (view)
    }

    override fun getItemCount() : Int {
        return artistPageArrayList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(artistPageArrayList[position], clickListener)
    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind (artistPage : ArtistPage, clickListener: (ArtistPage) -> Unit){
            itemView.setOnClickListener { clickListener(artistPage) }
            itemView.artist_name_selector.text = "${artistPage.artistName}"
        }
    }

    fun update(newItems: ArrayList<ArtistPage>){
        artistPageArrayList = newItems
        notifyDataSetChanged()
    }

}