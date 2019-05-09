package com.example.artistmanagerapp.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.models.MenuItem
import com.example.artistmanagerapp.models.Task
import kotlinx.android.synthetic.main.item_menu.view.*
import kotlinx.android.synthetic.main.item_task.view.*

class MenuAdapter (var menuItemsList : ArrayList<MenuItem>, val clickListener: (MenuItem) -> Unit) : RecyclerView.Adapter<MenuAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menuItemsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menuItemsList[position], clickListener)
    }

    /*fun updateItems(newTasks : ArrayList<Task>){
        taskList = newTasks
        notifyDataSetChanged()
    }*/

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind (menuItem : MenuItem, clickListener: (MenuItem) -> Unit){
            itemView.setOnClickListener {clickListener(menuItem)}
            itemView.menu_item_name.text = menuItem.itemName
        }
    }
}