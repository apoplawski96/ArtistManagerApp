package com.example.artistmanagerapp.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.activities.MainActivity
import com.example.artistmanagerapp.activities.TaskDetailsActivity
import com.example.artistmanagerapp.activities.TaskListActivity
import com.example.artistmanagerapp.adapters.MenuAdapter
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.models.MenuItem
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.utils.TaskHelper

class GridMenuFragment : Fragment() {

    // Collections
    private var menuItemsList : ArrayList <MenuItem> = ArrayList()
    // Adapters
    private var adapter: MenuAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_grid_menu, container, false)

        // Views
        var menuRecyclerView = rootView.findViewById(R.id.menu_recycler_view) as RecyclerView

        // Parse tasks
        populateMenuItemsList()
        menuRecyclerView.layoutManager = LinearLayoutManager(MainActivity(), OrientationHelper.VERTICAL, false)
        adapter = MenuAdapter(menuItemsList) { menuItem : MenuItem -> menuItemClicked(menuItem)}
        menuRecyclerView.adapter = adapter

        return rootView
    }

    fun menuItemClicked (menuItem: MenuItem){
        Toast.makeText(activity, "Clicked: ${menuItem.itemName}", Toast.LENGTH_SHORT).show()
        var intent : Intent? = null

        if (menuItem.itemName.equals("Task manager")){
            intent = Intent(activity, TaskListActivity::class.java)
        }

        startActivity(intent)
    }

    fun populateMenuItemsList(){
        menuItemsList.add(MenuItem("Task manager", R.mipmap.cover2))
        menuItemsList.add(MenuItem("chuj2", R.mipmap.cover2))
        menuItemsList.add(MenuItem("Change or create new ArtistPage!", R.mipmap.cover2))
    }

}
