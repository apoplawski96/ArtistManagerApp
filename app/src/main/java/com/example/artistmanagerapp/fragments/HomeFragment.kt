package com.example.artistmanagerapp.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.activities.MainActivity
import com.example.artistmanagerapp.activities.TaskDetailsActivity
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.firebase.FirebaseDataReader
import com.example.artistmanagerapp.interfaces.TaskUpdater
import com.example.artistmanagerapp.interfaces.UserDataPresenter
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.TaskHelper
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment : BaseFragment(), TaskUpdater, UserDataPresenter {

    // Collections
    private var tasksList : ArrayList <Task> = ArrayList()
    // Adapters
    private var adapter: TaskListAdapter? = null
    // Views
    var displayName : TextView? = null
    var artistRole : TextView? = null
    var taskListRecyclerView : RecyclerView? = null
    //var userAvatar : CircleImageView? = null
    //var bandAvatar : CircleImageView? = null

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Values
        val context : Context? = context

        // Views
        taskListRecyclerView = rootView.findViewById(R.id.task_list_home_recyclerview) as RecyclerView
        displayName = rootView.findViewById(R.id.user_display_name)
        artistRole = rootView.findViewById(R.id.role_display_name)
        val userAvatar = rootView.findViewById(R.id.user_avatar_home) as CircleImageView
        val bandAvatar = rootView.findViewById(R.id.background_band) as ImageView

        val pathToTasksCollection = perfectArtistPagePath.collection("tasks")

        // Load band photo
        val options = RequestOptions()
        options.centerCrop()
        Glide.with(this).load(R.mipmap.band_photo_avatar).apply(options).into(bandAvatar)

        // Parse and show task list
        TaskHelper.parseTasks(perfectArtistPagePath.collection("tasks"), this)
        taskListRecyclerView?.layoutManager = LinearLayoutManager(MainActivity(), OrientationHelper.VERTICAL, false)
        adapter = TaskListAdapter(context, tasksList, pathToTasksCollection) { taskItem : Task -> taskItemClicked(taskItem)}
        taskListRecyclerView?.adapter = adapter

        // Show user data
        FirebaseDataReader().getUserData("perfectUser", this)

        // Load avatar
        val storageRef = FirebaseStorage.getInstance().reference
        val avatarPath = storageRef.child("avatars/perfectUser/avatar.jpg")
        Glide.with(this).load(R.mipmap.avatar).into(userAvatar)

        return rootView
    }

    override fun updateTasks(tasksOutput: ArrayList<Task>) {
        adapter?.updateItems(tasksOutput)
    }

    override fun showUserData(userData: User) {
        //displayName?.setText(userData.firstName + " " + userData.lastName)
        //artistRole?.setText(userData.artistRole)
    }

    fun taskItemClicked (taskItem : Task){
        Toast.makeText(activity, "Clicked: ${taskItem.title}", Toast.LENGTH_SHORT).show()
        /*val intent = Intent(applicationContext, TaskDetailsActivity::class.java).apply{
            putExtra("task_id", taskItem.taskId)
        }
        startActivity(intent)*/
    }

    fun initUI(){

    }


}
