package com.example.artistmanagerapp.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.activities.*
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.firebase.FirebaseActivityLogsManager
import com.example.artistmanagerapp.firebase.FirebaseDataReader
import com.example.artistmanagerapp.firebase.FirebaseStatisticsHelper
import com.example.artistmanagerapp.firebase.StorageDataRetriever
import com.example.artistmanagerapp.interfaces.*
import com.example.artistmanagerapp.models.ActivityLog
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.*
import com.google.firebase.storage.FirebaseStorage
import com.makeramen.roundedimageview.RoundedImageView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlin.math.roundToInt

class HomeFragment : BaseFragment(), UserDataPresenter, DataReceiver, ArtistPagesPresenter, MediaLoader, FirebaseActivityLogsManager.ActivityLogsPresenter, FirebaseStatisticsHelper.StatisticsReceiver {

    // Bundled instances
    lateinit var userInstance : User
    lateinit var pageInstance : ArtistPage

    // ArtistPage info
    var pageInfoMapBundle : HashMap <String?, String?> = HashMap()
    var pageId : String? = null
    var pageName : String? = null
    var epkShareCode : String? = null

    // Views
    var helloUser : TextView? = null
    var thisIsBandName : TextView? = null
    var bandAvatar : ImageView? = null
    var tasksCounter : TextView? = null
    var eventsCounter : TextView? = null
    var assignmentsCounter : TextView? = null
    var pageAvatar : CircleImageView? = null
    var manageTeamButton : RoundedImageView? = null
    var avatarProgressBar : ProgressBar? = null
    var activityLogsListButton : RoundedImageView? = null

    companion object {
        @JvmStatic
        val c = Constants

        fun newInstance(pageId : String, artistPage : ArtistPage, user : User) : HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle().apply{
                putSerializable (c.BUNDLE_USER_INSTANCE, user)
                putSerializable (c.BUNDLE_ARTIST_PAGE_INSTANCE, artistPage)
                putString (c.PAGE_ID_BUNDLE, artistPage.artistPageId)
                putString (c.ARTIST_NAME_BUNDLE, artistPage.artistName)
                putString (c.EPK_SHARE_CODE_BUNDLE, artistPage.epkShareCode)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var rootView : View

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Getting bundle data
        userInstance = arguments?.getSerializable(c.BUNDLE_USER_INSTANCE) as User
        pageInstance = arguments?.getSerializable(c.BUNDLE_ARTIST_PAGE_INSTANCE) as ArtistPage

        pageId = arguments?.getString(c.PAGE_ID_BUNDLE).toString()
        pageName = arguments?.getString(c.ARTIST_NAME_BUNDLE).toString()
        epkShareCode = arguments?.get(c.EPK_SHARE_CODE_BUNDLE).toString()
        pageInfoMapBundle.put(c.PAGE_ID_BUNDLE, pageId)
        pageInfoMapBundle.put(c.ARTIST_NAME_BUNDLE, pageName)
        pageInfoMapBundle.put(c.EPK_SHARE_CODE_BUNDLE, epkShareCode)

        Log.d("Welcome to HomeFragment - onCreateView()", "Fragment Entered")
        Log.d("HomeFragment", "PageName: $pageName, pageId: $pageId, epkShareCode: $epkShareCode")

        // Views
        helloUser = rootView.findViewById(R.id.hello_user)
        thisIsBandName = rootView.findViewById(R.id.this_is_band_name)
        pageAvatar = rootView.findViewById(R.id.circle_page_avatar)
        manageTeamButton = rootView.findViewById(R.id.manage_team_button)
        avatarProgressBar = rootView.findViewById(R.id.avatar_progress_bar)

        initUI()

        // Load statistics - logs count
        FirebaseActivityLogsManager.parseActivityLogs(pageInstance!!.artistPageId.toString(), this)
        // Load statistics - new tasks
        TaskHelper.checkNewTasksCount(pageInstance.artistPageId.toString(), this)
        // Load statistics - pending assignments
        FirebaseStatisticsHelper.readStats(userInstance.id.toString(), null, "tasksAssigned", this)

        // Load page avatar
        StorageDataRetriever().downloadImageViaId(pageId, StorageDataRetriever.DownloadOption.PAGE_AVATAR, this)

        // Show user data
        FirebaseDataReader().getUserData(userIdGlobal, this)

        // OnClicks managed
        manageTeamButton?.setOnClickListener {
            var intent = Intent(activity, ManageTeamActivity::class.java)
            putDataToBundle(intent)
            startActivity(intent)
        }

        rootView.activity_logs_list_button.setOnClickListener {
            var intent = Intent(activity, ActivityLogsActivity::class.java)
            putDataToBundle(intent)
            startActivity(intent)
        }

        rootView.home_screen_back_button.setOnClickListener {
            var intent = Intent(activity, SelectArtistPageActivity::class.java).apply { putExtra (Constants.PAGE_ID_BUNDLE, pageId) }
            putDataToBundle(intent)
            startActivity(intent)
        }

        return rootView
    }

    override fun loadImage(bitmap: Bitmap?, option: MediaLoader.MediaLoaderOptions?) {
        avatarProgressBar?.visibility = View.GONE
        pageAvatar?.visibility = View.VISIBLE
        pageAvatar?.setImageBitmap(bitmap)
        loadUserData()
        //Toast.makeText(activity, "hui", Toast.LENGTH_SHORT).show()
    }

    fun putDataToBundle(intent : Intent?){
        intent?.putExtra (Constants.PAGE_ID_BUNDLE, pageId)
        intent?.putExtra (Constants.ARTIST_NAME_BUNDLE, pageName)
        intent?.putExtra (Constants.EPK_SHARE_CODE_BUNDLE, epkShareCode)

        intent?.putExtra (Constants.BUNDLE_USER_INSTANCE, userInstance)
        intent?.putExtra (Constants.BUNDLE_ARTIST_PAGE_INSTANCE, pageInstance)
    }

    override fun showUserData(userData: User) {

    }

    fun loadUserData(){
        thisIsBandName?.text = pageInstance.artistName
        rootView.artist_genre_home.text = pageInstance.genre

        rootView.artist_category_display.visibility = View.VISIBLE
        thisIsBandName?.visibility = View.VISIBLE
        rootView.artist_genre_home.visibility = View.VISIBLE
        rootView.home_progress_bar_small.visibility = View.GONE

        when (pageInstance.pageCategory){
            "Band" -> {
                rootView.artist_category_display.text = pageInstance.pageCategory
                rootView.artist_category_display.layoutParams.width = 150
            }
            "Solo Artist" -> {
                rootView.artist_category_display.text = pageInstance.pageCategory
                rootView.artist_category_display.layoutParams.width = 300
            }
        }
    }

    fun initUI(){
        // HIDE
        rootView.artist_category_display.visibility = View.INVISIBLE
        pageAvatar?.visibility = View.INVISIBLE
        thisIsBandName?.visibility = View.INVISIBLE
        rootView.artist_genre_home.visibility = View.INVISIBLE

        // SHOW
        rootView.home_progress_bar_small.visibility = View.VISIBLE
        avatarProgressBar?.visibility = View.VISIBLE
    }

    override fun receiveData(data: Any?, mInterface: Any?) {
        if (data == null){
            val intent = Intent(activity, SelectArtistPageActivity::class.java)
            startActivity(intent)
        } else if (data is Double?){
            val dataInt = data.roundToInt()
            presentStats(dataInt, "newTasks")
        }
    }

    fun presentStats(counter : Any, option : String){
        when (option){
            "newTasks" -> {
                rootView.new_tasks_counter.text = counter.toString()
            }
            "pendingAssignments" -> {
                rootView.pending_assignments_counter.text = counter.toString()
            }
            "todaysActivity" -> {
                rootView.todays_activity_counter.text = counter.toString()
            }
        }

    }

    override fun receiveLogs(activityLogs: ArrayList<ActivityLog>) {
        var todaysLogsCount : Int = countTodaysLogs(activityLogs)
        presentStats(todaysLogsCount, "todaysActivity")
    }

    fun countTodaysLogs(activityLogs: ArrayList<ActivityLog>) : Int{
        var counter = 0
        for (log in activityLogs){
            if (log.dateCreated == Utils.getCurrentDateShort()) counter++
        }
        return counter
    }

    override fun onStatsReceived(data: Any?, option: String?) {
        when (option){
            "tasksAssigned" -> {
                val counter = data as Int
                presentStats(counter, "pendingAssignments")
            }
        }

    }

    override fun showArtistPageData(artistPage: ArtistPage) {


    }

    override fun showNoPagesText() {

    }

    override fun showArtistPages(artistPagesList: ArrayList<ArtistPage>) {

    }

    override fun onLoadingFailed(error: String?) {

    }

}
