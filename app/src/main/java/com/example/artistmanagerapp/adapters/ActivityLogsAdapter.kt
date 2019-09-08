package com.example.artistmanagerapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.interfaces.UsersListListener
import com.example.artistmanagerapp.models.ActivityLog
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.Utils
import kotlinx.android.synthetic.main.item_activity_log.view.*
import kotlinx.android.synthetic.main.item_team_member.view.*
import kotlinx.android.synthetic.main.item_team_member.view.name_acronym

class ActivityLogsAdapter (var logsList : ArrayList<ActivityLog>) : RecyclerView.Adapter<ActivityLogsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_activity_log, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return logsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(logsList[position])
    }

    fun updateItems(newLogs : ArrayList<ActivityLog>){
        logsList = newLogs
        notifyDataSetChanged()
    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(log : ActivityLog){
            itemView.name_acronym.text = log.userAcronym
            itemView.activity_log_description.text = log.logDescription
            itemView.activity_log_date.text = "${log.dateCreated} - ${log.timeCreated}"
        }
    }

}