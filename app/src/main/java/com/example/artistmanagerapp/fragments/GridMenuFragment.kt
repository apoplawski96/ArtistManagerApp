package com.example.artistmanagerapp.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.activities.*
import com.example.artistmanagerapp.adapters.MenuAdapter
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.MenuItem
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.Communicator
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.TaskHelper
import com.example.artistmanagerapp.utils.UsersHelper

class GridMenuFragment : BaseFragment(), UserInterfaceUpdater {

    // Others
    val TAG = "GridMenuFragment"

    // Collections
    private var menuItemsList : ArrayList <MenuItem> = ArrayList()
    // Adapters
    private var adapter: MenuAdapter? = null
    // ArtistPage info
    var pageId : String? = null
    var pageName : String? = null
    var epkShareCode : String? = null

    // Bundle objects
    var artistPageInstance : ArtistPage? = null
    var userInstance : User? = null

    companion object {
        @JvmStatic
        val c = Constants
        fun newInstance(pageId : String, artistPage: ArtistPage, user: User) : GridMenuFragment {
            val fragment = GridMenuFragment()
            val bundle = Bundle().apply{
                putString (c.PAGE_ID_BUNDLE, artistPage.artistPageId)
                putString (c.ARTIST_NAME_BUNDLE, artistPage.artistName)
                putString (c.EPK_SHARE_CODE_BUNDLE, artistPage.epkShareCode)
                putSerializable(c.BUNDLE_ARTIST_PAGE_INSTANCE, artistPage)
                putSerializable(c.BUNDLE_USER_INSTANCE, user)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_grid_menu, container, false)

        // Getting bundled objects
        artistPageInstance = arguments?.getSerializable(c.BUNDLE_ARTIST_PAGE_INSTANCE) as ArtistPage?
        userInstance = arguments?.getSerializable(c.BUNDLE_USER_INSTANCE) as User?

        Log.d(TAG, "GridMenuFragment entered")
        Log.d(TAG, artistPageInstance.toString())
        Log.d(TAG, userInstance.toString())

        pageId = arguments?.getString(c.PAGE_ID_BUNDLE).toString()
        pageName = arguments?.getString(c.ARTIST_NAME_BUNDLE).toString()
        epkShareCode = arguments?.getString(c.EPK_SHARE_CODE_BUNDLE).toString()

        // Views
        var menuRecyclerView = rootView.findViewById(R.id.menu_recycler_view) as RecyclerView

        Toast.makeText(activity, "$pageName+$pageId+$epkShareCode+asd", Toast.LENGTH_SHORT).show()

        // Parse tasks
        populateMenuItemsList()
        menuRecyclerView.layoutManager = LinearLayoutManager(MainActivity(), OrientationHelper.VERTICAL, false)
        adapter = MenuAdapter(menuItemsList) { menuItem : MenuItem -> menuItemClicked(menuItem)}
        menuRecyclerView.adapter = adapter

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Toast.makeText(activity, "$pageName+$pageId+$epkShareCode+asd", Toast.LENGTH_SHORT).show()
    }

    fun putDataToBundle(intent : Intent?){
        intent?.putExtra (Constants.PAGE_ID_BUNDLE, pageId)
        intent?.putExtra (Constants.ARTIST_NAME_BUNDLE, pageName)
        intent?.putExtra (Constants.EPK_SHARE_CODE_BUNDLE, epkShareCode)
        intent?.putExtra (Constants.BUNDLE_USER_INSTANCE, userInstance)
        intent?.putExtra (Constants.BUNDLE_ARTIST_PAGE_INSTANCE, artistPageInstance)
    }

    fun menuItemClicked (menuItem: MenuItem){
        var intent : Intent? = null
        var option = menuItem.itemName

        Toast.makeText(activity, "$pageName+$pageId+$epkShareCode", Toast.LENGTH_SHORT).show()

        when (option){
            "Task manager" ->  {
                intent = Intent(activity, TaskListActivity::class.java)
            }
            "Events manager" -> {
                intent = Intent(activity, EventsManagerActivity::class.java)
            }
            "Electronic Press Kit" -> {
                intent = Intent(activity, EpkSelectorActivity::class.java)
            }
            "Switch/create artist page" -> {
                UsersHelper.removeCurrentArtistPage(userIdGlobal, this)
            }
        }

        putDataToBundle(intent)
        if (intent != null){ startActivity(intent) }
    }

    fun populateMenuItemsList(){
        menuItemsList.add(MenuItem("Task manager", R.mipmap.cover1))
        menuItemsList.add(MenuItem("Events manager", R.mipmap.cover3))
        menuItemsList.add(MenuItem("Electronic Press Kit", R.mipmap.cover_photo_4))
        menuItemsList.add(MenuItem("Switch/create artist page", R.mipmap.cover2))
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initializeUI() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUI(option: String, data : Any?) {
        if (option == Constants.CURRENT_ARTIST_PAGE_REMOVED){
            var intent = Intent(activity, SelectArtistPageActivity::class.java).apply { putExtra (Constants.PAGE_ID_BUNDLE, pageId) }
            startActivity(intent)
        }
    }

}
