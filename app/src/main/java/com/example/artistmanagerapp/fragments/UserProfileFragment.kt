package com.example.artistmanagerapp.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.activities.CreateUserProfileActivity
import com.example.artistmanagerapp.activities.LoginActivity
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.Utils
import de.hdodenhof.circleimageview.CircleImageView

class UserProfileFragment : BaseFragment() {

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

    // Views
    var displayName : TextView? = null
    var userAvatar : CircleImageView? = null
    var logoutButton : TextView? = null
    var editProfile : TextView? = null
    var changePassword : TextView? = null
    var legalInfo : TextView? = null
    var appInfo : TextView? = null

    companion object {
        @JvmStatic
        fun newInstance(pageId : String, user : User?) : UserProfileFragment {
            val c = Constants
            val fragment = UserProfileFragment()
            val bundle = Bundle().apply{
                putString ("PAGE_ID", pageId)
                putString (c.FIRST_NAME_BUNDLE, user?.firstName.toString())
                putString (c.LAST_NAME_BUNDLE, user?.lastName.toString())
                putString (c.PAGE_ROLE_BUNDLE, user?.pageRole.toString())
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
        mFirstName = arguments?.getString(c.FIRST_NAME_BUNDLE)
        mLastName = arguments?.getString(c.LAST_NAME_BUNDLE)
        pageRole = arguments?.getString(c.PAGE_ROLE_BUNDLE)

        // Views
        displayName = rootView.findViewById(R.id.profile_user_fullname)
        userAvatar = rootView.findViewById(R.id.user_avatar)
        logoutButton = rootView.findViewById(R.id.logout_button)
        editProfile = rootView.findViewById(R.id.edit_profile)
        changePassword = rootView.findViewById(R.id.change_password)
        appInfo = rootView.findViewById(R.id.app_info)
        legalInfo = rootView.findViewById(R.id.legal_info)

        displayName?.text = "${mFirstName} ${mLastName}, userId: ${user?.uid}"

        editProfile?.setOnClickListener{
            val intent = Intent(activity, CreateUserProfileActivity::class.java)
            intent?.putExtra(Constants.MODE_KEY, Constants.USER_PROFILE_EDIT_MODE)
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

}
