package com.example.artistmanagerapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.artistmanagerapp.R
import com.google.firebase.firestore.auth.User

class UserProfileFragment : Fragment() {

    var pageId : String? = null

    companion object {
        @JvmStatic
        fun newInstance(pageId : String) : UserProfileFragment {
            val fragment = UserProfileFragment()
            val bundle = Bundle().apply{ putString ("PAGE_ID", pageId) }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_user_profile, container, false)

        pageId = arguments?.getString("PAGE_ID")

        return rootView
    }

}
