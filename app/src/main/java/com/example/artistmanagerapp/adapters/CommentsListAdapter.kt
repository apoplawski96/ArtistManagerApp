package com.example.artistmanagerapp.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.models.Comment
import com.example.artistmanagerapp.utils.Utils
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentsListAdapter (var commentsList : ArrayList<Comment>) : RecyclerView.Adapter<CommentsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(commentsList[position])
    }

    fun updateItems(newComments : ArrayList<Comment>){
        commentsList = newComments
        notifyDataSetChanged()
    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bind(comment : Comment){
            itemView.comment_content.text = comment.content
            itemView.comment_date.text = comment.dateCreated
            itemView.comment_created_by.text = comment.createdByDisplayName

            //var displayName = comment.createdByDisplayName
            //var namesDelimited : List<String> = displayName.split("")[2]

            //itemView.name_acronym.text = Utils.createNameAcronym()
        }
    }

}