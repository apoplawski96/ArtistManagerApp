package com.example.artistmanagerapp.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
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
import de.hdodenhof.circleimageview.CircleImageView

class UserProfileFragment : BaseFragment() {

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
    var logoutButton : Button? = null

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

        // Getting User bundled data
        pageId = arguments?.getString("PAGE_ID")
        mFirstName = arguments?.getString(c.FIRST_NAME_BUNDLE)
        mLastName = arguments?.getString(c.LAST_NAME_BUNDLE)
        pageRole = arguments?.getString(c.PAGE_ROLE_BUNDLE)

        // Views
        displayName = rootView.findViewById(R.id.profile_user_fullname)
        userAvatar = rootView.findViewById(R.id.user_avatar)
        logoutButton = rootView.findViewById(R.id.logout_button)

        displayName?.text = "${mFirstName} ${mLastName}, userId: ${user?.uid}"

        logoutButton?.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent?.putExtra(c.CURRENT_PAGE_BUNDLE, "null")
            startActivity(intent)
        }

        return rootView
    }

}
