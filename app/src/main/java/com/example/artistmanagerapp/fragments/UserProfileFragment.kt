package com.example.artistmanagerapp.fragments


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.activities.CreateUserProfileActivity
import com.example.artistmanagerapp.activities.LoginActivity
import com.example.artistmanagerapp.firebase.StorageDataRetriever
import com.example.artistmanagerapp.interfaces.MediaLoader
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.Utils
import de.hdodenhof.circleimageview.CircleImageView

class UserProfileFragment : BaseFragment(), MediaLoader {

    //
    val CTX_TAG = "UserProfileFragment"

    // Objects
    val c = Constants

    // Variables set - User
    var pageId : String? = null
    var userObject : User? = null
    var mFirstName : String? = null
    var mLastName : String? = null
    var pageRole : String? = null
    var artistRole : String? = null
    var currentPage : String? = null
    var email : String? = null
    var userInstance : User? = null

    // Views
    var displayName : TextView? = null
    var userAvatar : CircleImageView? = null
    var logoutButton : TextView? = null
    var editProfile : TextView? = null
    var changePassword : TextView? = null
    var legalInfo : TextView? = null
    var appInfo : TextView? = null
    var avatarProgressBar : ProgressBar? = null

    companion object {
        @JvmStatic
        fun newInstance(pageId : String, user : User?) : UserProfileFragment {
            val c = Constants
            val fragment = UserProfileFragment()
            val bundle = Bundle().apply{
                putString ("PAGE_ID", pageId)
                putSerializable(Constants.BUNDLE_USER_INSTANCE, user)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_user_profile, container, false)
        Log.d(CTX_TAG, "Welcome to UserProfileFragment!")

        // Getting User bundled data
        pageId = arguments?.getString("PAGE_ID")
        userInstance = arguments?.getSerializable(Constants.BUNDLE_USER_INSTANCE) as User?

        // Views
        displayName = rootView.findViewById(R.id.profile_user_fullname)
        userAvatar = rootView.findViewById(R.id.user_avatar)
        logoutButton = rootView.findViewById(R.id.logout_button)
        editProfile = rootView.findViewById(R.id.edit_profile)
        changePassword = rootView.findViewById(R.id.change_password)
        appInfo = rootView.findViewById(R.id.app_info)
        legalInfo = rootView.findViewById(R.id.legal_info)
        avatarProgressBar = rootView.findViewById(R.id.user_avatar_progress_bar)

        initUI()

        // Load page avatar
        StorageDataRetriever().downloadImageViaId(userInstance?.id, StorageDataRetriever.DownloadOption.USER_AVATAR, this)

        displayName?.text = "${userInstance?.firstName} ${userInstance?.lastName}"

        editProfile?.setOnClickListener{
            val intent = Intent(activity, CreateUserProfileActivity::class.java)
            intent?.apply {
                putExtra(Constants.MODE_KEY, Constants.USER_PROFILE_EDIT_MODE)
                putExtra(Constants.BUNDLE_USER_INSTANCE, userInstance)
            }
            startActivity(intent)
        }

        changePassword?.setOnClickListener {

        }

        appInfo?.setOnClickListener {

        }

        legalInfo?.setOnClickListener {

        }

        logoutButton?.setOnClickListener {
            Log.d(CTX_TAG, "signOut() called - we're logging out...")
            auth.signOut()
            val dir = context?.cacheDir
            Utils.deleteDir(dir)
            val intent = Intent(activity, LoginActivity::class.java)
            intent?.putExtra(c.CURRENT_PAGE_BUNDLE, "Asda")
            startActivity(intent)
        }

        return rootView
    }

    override fun loadImage(bitmap: Bitmap?, option: MediaLoader.MediaLoaderOptions?) {
        userAvatar?.setImageBitmap(bitmap)
        hideProgress()
    }

    fun initUI(){
        showProgress()
    }

    fun showProgress(){
        avatarProgressBar?.visibility = View.VISIBLE
        userAvatar?.visibility = View.INVISIBLE
    }

    fun hideProgress(){
        userAvatar?.visibility = View.VISIBLE
        avatarProgressBar?.visibility = View.INVISIBLE
    }

}
