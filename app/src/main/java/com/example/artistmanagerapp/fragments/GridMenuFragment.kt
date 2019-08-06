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
import com.example.artistmanagerapp.activities.*
import com.example.artistmanagerapp.adapters.MenuAdapter
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.models.MenuItem
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.TaskHelper

class GridMenuFragment : Fragment() {

    // Collections
    private var menuItemsList : ArrayList <MenuItem> = ArrayList()
    // Adapters
    private var adapter: MenuAdapter? = null
    // Page Id variable
    var pageId : String? = null

    companion object {
        @JvmStatic
        fun newInstance(pageId : String) : GridMenuFragment {
            val fragment = GridMenuFragment()
            val bundle = Bundle().apply{ putString ("PAGE_ID", pageId) }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_grid_menu, container, false)

        pageId = arguments?.getString("PAGE_ID").toString()

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
        var intent : Intent? = null
        var option = menuItem.itemName

        when (option){
            "Task manager" ->  {
                intent = Intent(activity, TaskListActivity::class.java).apply { putExtra (Constants.PAGE_ID_BUNDLE, pageId) }
            }
            "Events calendar" -> {
                intent = Intent(activity, EventsManagerActivity::class.java).apply { putExtra (Constants.PAGE_ID_BUNDLE, pageId) }
            }
            "Switch/create artist page" -> {
                intent = Intent(activity, SelectArtistPageActivity::class.java).apply { putExtra (Constants.PAGE_ID_BUNDLE, pageId) }
            }
        }


        /*if (menuItem.itemName.equals("Task manager")){
            intent = Intent(activity, TaskListActivity::class.java)
        }*/

        if (intent != null){ startActivity(intent) }
    }

    fun populateMenuItemsList(){
        menuItemsList.add(MenuItem("Task manager", R.mipmap.cover1))
        menuItemsList.add(MenuItem("Events manager", R.mipmap.cover3))
        menuItemsList.add(MenuItem("Electronic Press Kit", R.mipmap.cover_photo_4))
        menuItemsList.add(MenuItem("Switch/create artist page", R.mipmap.cover2))
    }

}
