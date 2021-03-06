package com.example.artistmanagerapp.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.interfaces.UsersListListener
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.Utils
import kotlinx.android.synthetic.main.item_user_avatar.view.*

class UsersListAdapter (usersListListener: UsersListListener, val context : Context?, var usersList : ArrayList<User>, val clickListener : (User, View) -> Unit) : RecyclerView.Adapter<UsersListAdapter.ViewHolder>() {

    val usersListListener : UsersListListener = usersListListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_user_avatar, parent, false)
        return ViewHolder(usersListListener , view, context)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(usersListListener, usersList[position], clickListener)
    }

    fun updateItems(newUsers : ArrayList<User>){
        usersList = newUsers
        notifyDataSetChanged()
    }

    class ViewHolder (usersListListener: UsersListListener, itemView : View, context: Context?) : RecyclerView.ViewHolder(itemView){

        fun bind(usersListListener: UsersListListener, user : User, clickListener: (User, View) -> Unit){

            itemView.setOnClickListener { view ->
                clickListener(user, view)
            }
            itemView.name_acronym.text = Utils.createNameAcronym(user.firstName.toString(), user.lastName.toString())

        }

    }

}