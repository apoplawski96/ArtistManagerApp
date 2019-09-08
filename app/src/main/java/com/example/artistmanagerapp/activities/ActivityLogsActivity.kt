package com.example.artistmanagerapp.activities

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.ActivityLogsAdapter
import com.example.artistmanagerapp.firebase.FirebaseActivityLogsManager
import com.example.artistmanagerapp.models.ActivityLog
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.Constants
import kotlinx.android.synthetic.main.activity_logs.*

class ActivityLogsActivity : BaseActivity(), FirebaseActivityLogsManager.ActivityLogsPresenter {

    var activityLogsList : ArrayList<ActivityLog> = ArrayList()

    lateinit var userInstance : User
    lateinit var pageInstance : ArtistPage

    var adapter : ActivityLogsAdapter? = null

    var filterDialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)

        userInstance = intent.getSerializableExtra(Constants.BUNDLE_USER_INSTANCE) as User
        pageInstance = intent.getSerializableExtra(Constants.BUNDLE_ARTIST_PAGE_INSTANCE) as ArtistPage

        FirebaseActivityLogsManager.parseActivityLogs(pageInstance.artistPageId!!, this)

        filterDialog = Dialog(this)
        filterDialog?.setContentView(R.layout.dialog_filter_logs)

        activity_logs_recycler_view.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        adapter = ActivityLogsAdapter(activityLogsList)
        activity_logs_recycler_view.adapter = adapter

    }

    override fun showLogs(activityLogs : ArrayList<ActivityLog>) {
        adapter?.updateItems(activityLogs)
    }

}
