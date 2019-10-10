package com.example.artistmanagerapp.activities

import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import android.widget.RadioButton
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.ActivityLogsAdapter
import com.example.artistmanagerapp.firebase.FirebaseActivityLogsManager
import com.example.artistmanagerapp.models.ActivityLog
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.constants.Constants
import kotlinx.android.synthetic.main.activity_logs.*
import kotlinx.android.synthetic.main.dialog_filter_logs.*

class ActivityLogsActivity : BaseActivity(), FirebaseActivityLogsManager.ActivityLogsPresenter {

    enum class FilterParameter{
        ONLY_ME,
        TEAM
    }

    enum class SortParameter{
        ASCENDING,
        DESCENDING
    }

    // Collections
    var activityLogsList : ArrayList<ActivityLog> = ArrayList()
    var activityLogsListRawBackup : ArrayList<ActivityLog> = ArrayList()

    // Bundle objects
    lateinit var userInstance : User
    lateinit var pageInstance : ArtistPage

    // Adapter
    var adapter : ActivityLogsAdapter? = null

    // Views
    var filterDialog : Dialog? = null

    // Sort/filter parameter vars
    var currentFilterParameter = FilterParameter.TEAM
    var currentSortParameter = SortParameter.DESCENDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)

        // Getting bundled data
        userInstance = intent.getSerializableExtra(Constants.BUNDLE_USER_INSTANCE) as User
        pageInstance = intent.getSerializableExtra(Constants.BUNDLE_ARTIST_PAGE_INSTANCE) as ArtistPage

        // Loading whole logs collection from the DB
        FirebaseActivityLogsManager.parseActivityLogs(pageInstance.artistPageId!!, this)
        showProgressBar()

        // Dialog setup
        filterDialog = Dialog(this)
        filterDialog?.setContentView(R.layout.dialog_filter_logs)

        // RecyclerView setup
        activity_logs_recycler_view.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        adapter = ActivityLogsAdapter(activityLogsList)
        activity_logs_recycler_view.adapter = adapter

        filter_logs_icon.setOnClickListener {
            filterDialog!!.show()
            setupFilterDialog()
        }
    }

    private fun showProgressBar(){
        logs_progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        logs_progress_bar.visibility = View.GONE
    }

    // Receiving logs from Firestore
    override fun receiveLogs(activityLogs : ArrayList<ActivityLog>) {
        // Initializing backup list with every log existing
        activityLogsListRawBackup.addAll(activityLogs)
        // Sorting
        val sortedLogs = filterAndSortLogs(activityLogs, currentSortParameter, currentFilterParameter)
        // UI
        hideProgressBar()
        adapter?.updateItems(sortedLogs)
    }

    private fun setupFilterDialog(){
        //Setting up filter parameter
        when (currentFilterParameter){
            FilterParameter.TEAM -> {
                filterDialog!!.radio_button_team.isChecked = true
                filterDialog!!.radio_button_only_me.isChecked = false
            }
            FilterParameter.ONLY_ME -> {
                filterDialog!!.radio_button_team.isChecked = false
                filterDialog!!.radio_button_only_me.isChecked = true
            }
        }
        // Setting up sort parameter
        when (currentSortParameter){
            SortParameter.DESCENDING -> {
                filterDialog!!.radio_button_descending.isChecked = true
                filterDialog!!.radio_button_ascending.isChecked = false
            }
            SortParameter.ASCENDING -> {
                filterDialog!!.radio_button_descending.isChecked = false
                filterDialog!!.radio_button_ascending.isChecked = true
            }
        }

        filterDialog!!.filter_dialog_apply_button.setOnClickListener {
            val fullLogsList : ArrayList <ActivityLog> = ArrayList()
            fullLogsList.addAll(activityLogsListRawBackup)

            logs_progress_bar.visibility = View.VISIBLE
            val newList = filterAndSortLogs(fullLogsList, currentSortParameter, currentFilterParameter)
            logs_progress_bar.visibility = View.GONE

            filterDialog!!.hide()
            adapter?.updateItems(newList)
        }

        filterDialog!!.filter_dialog_cancel_button.setOnClickListener {
            filterDialog!!.hide()
        }

    }

    override fun onBackPressed() {
        filterDialog!!.hide()
        super.onBackPressed()
    }

    private fun filterAndSortLogs (logsListRaw : ArrayList<ActivityLog>, sortParam : SortParameter, filterParam : FilterParameter) : ArrayList<ActivityLog>{
        val listFiltered = logsManager.filterActivityLogs(logsListRaw, filterParam, userInstance.id.toString())
        val listFinal = logsManager.sortActivityLogsByDate(listFiltered, sortParam)

        return listFinal
    }

    fun onFilterRadioButtonChecked(view : View){
        if (view is RadioButton) {
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_button_team ->
                    if (checked) {
                        filterDialog!!.radio_button_team.isChecked = true
                        filterDialog!!.radio_button_only_me.isChecked = false
                        currentFilterParameter = FilterParameter.TEAM
                    }
                R.id.radio_button_only_me ->
                    if (checked) {
                        filterDialog!!.radio_button_team.isChecked = false
                        filterDialog!!.radio_button_only_me.isChecked = true
                        currentFilterParameter = FilterParameter.ONLY_ME
                    }
                R.id.radio_button_descending ->
                    if (checked) {
                        filterDialog!!.radio_button_descending.isChecked = true
                        filterDialog!!.radio_button_ascending.isChecked = false
                        currentSortParameter = SortParameter.DESCENDING
                    }
                R.id.radio_button_ascending ->
                    if (checked) {
                        filterDialog!!.radio_button_descending.isChecked = false
                        filterDialog!!.radio_button_ascending.isChecked = true
                        currentSortParameter = SortParameter.ASCENDING
                    }
            }
        }
    }

}
