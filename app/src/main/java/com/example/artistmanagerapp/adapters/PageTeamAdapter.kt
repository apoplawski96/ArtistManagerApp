package com.example.artistmanagerapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.interfaces.UsersListListener
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.Utils
import kotlinx.android.synthetic.main.item_team_member.view.*

class PageTeamAdapter (usersListListener: UsersListListener, val context : Context?, var usersList : ArrayList<User>, val clickListener : (User) -> Unit) : RecyclerView.Adapter<PageTeamAdapter.ViewHolder>() {

    val usersListListener : UsersListListener = usersListListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_team_member, parent, false)
        return ViewHolder(usersListListener , view, context)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(usersListListener, usersList[position], clickListener, null)
    }

    fun showAdminSettingsOnItem(){

    }

    fun updateItems(newUsers : ArrayList<User>){
        usersList = newUsers
        notifyDataSetChanged()
    }

    class ViewHolder (usersListListener: UsersListListener, itemView : View, context: Context?) : RecyclerView.ViewHolder(itemView){

        fun bind(usersListListener: UsersListListener, user : User, clickListener: (User) -> Unit, isAdminModeEnabled: Boolean?){
            itemView.setOnClickListener {clickListener(user)}
            itemView.name_acronym.text = Utils.createNameAcronym(user.firstName.toString(), user.lastName.toString())
            itemView.full_name.text = "${user.firstName} ${user.lastName}"
            itemView.page_role_display.text = user.roleCategory
            itemView.artist_role_display.text = user.artistRole

            if (isAdminModeEnabled == true){
                itemView.admin_settings_opener.visibility = View.VISIBLE
            }

            itemView.option_dialog_opener.setOnClickListener {
                clickListener(user)
            }

            itemView.admin_settings_opener.setOnClickListener {
                usersListListener.controlAdminPanel(user, itemView)
            }

        }
    }

}