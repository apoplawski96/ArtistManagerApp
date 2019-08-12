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
import com.example.artistmanagerapp.activities.SelectArtistPageActivity
import com.example.artistmanagerapp.activities.TaskDetailsActivity
import com.example.artistmanagerapp.adapters.TaskListAdapter
import com.example.artistmanagerapp.firebase.FirebaseDataReader
import com.example.artistmanagerapp.firebase.StorageDataRetriever
import com.example.artistmanagerapp.interfaces.*
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.utils.Communicator
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.FirebaseConstants
import com.example.artistmanagerapp.utils.TaskHelper
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment : BaseFragment(), UserDataPresenter, DataReceiver, ArtistPagesPresenter, MediaLoader {

    // FragmentsMessenger
    var model : Communicator? = null

    // ArtistPage info
    var pageInfoMapBundle : HashMap <String?, String?> = HashMap()
    var pageId : String? = null
    var pageName : String? = null
    var shareEpkCode : String? = null

    // Views
    var helloUser : TextView? = null
    var thisIsBandName : TextView? = null
    var bandAvatar : ImageView? = null
    var tasksCounter : TextView? = null
    var eventsCounter : TextView? = null
    var assignmentsCounter : TextView? = null
    var pageAvatar : CircleImageView? = null

    companion object {
        @JvmStatic
        val c = Constants

        fun newInstance(pageId : String, artistPage: ArtistPage) : HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle().apply{
                putString (c.PAGE_ID_BUNDLE, artistPage.artistPageId)
                putString (c.ARTIST_NAME_BUNDLE, artistPage.artistName)
                putString (c.EPK_SHARE_CODE_BUNDLE, artistPage.epkShareCode)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Getting bundle data
        pageId = arguments?.getString(c.PAGE_ID_BUNDLE).toString()
        pageName = arguments?.getString(c.ARTIST_NAME_BUNDLE).toString()
        shareEpkCode = arguments?.get(c.EPK_SHARE_CODE_BUNDLE).toString()
        pageInfoMapBundle.put(c.PAGE_ID_BUNDLE, pageId)
        pageInfoMapBundle.put(c.ARTIST_NAME_BUNDLE, pageName)
        pageInfoMapBundle.put(c.EPK_SHARE_CODE_BUNDLE, shareEpkCode)

        Toast.makeText(activity, "$pageName+asdasd$pageId+asdasd$shareEpkCode", Toast.LENGTH_SHORT).show()

        // Views
        helloUser = rootView.findViewById(R.id.hello_user)
        thisIsBandName = rootView.findViewById(R.id.this_is_band_name)
        pageAvatar = rootView.findViewById(R.id.circle_page_avatar)
        thisIsBandName?.text = pageName

        model = ViewModelProviders.of(activity!!).get(Communicator::class.java)
        model!!.setMsgCommunicator(pageName.toString())

        // Load page avatar
        StorageDataRetriever().downloadImageViaId(pageId, StorageDataRetriever.DownloadOption.PAGE_AVATAR, this)

        // Show user data
        FirebaseDataReader().getUserData(user?.uid.toString(), this)

        return rootView
    }

    override fun loadImage(bitmap: Bitmap?, option: MediaLoader.MediaLoaderOptions?) {
        pageAvatar?.setImageBitmap(bitmap)
        Toast.makeText(activity, "hui", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val model = ViewModelProviders.of(activity!!).get(Communicator::class.java)
        model.message.observe(this, object : Observer<Any> {
            override fun onChanged(t: Any?) {
                var ap : HashMap<String?, String?> = t as HashMap<String?, String?>
                pageName = ap.get(GridMenuFragment.c.ARTIST_NAME_BUNDLE)
                pageId = ap.get(GridMenuFragment.c.PAGE_ID_BUNDLE)
                shareEpkCode = ap.get(GridMenuFragment.c.EPK_SHARE_CODE_BUNDLE)
            }
        })*/
    }


    override fun showUserData(userData: User) {
    }

    fun initUI(){

    }

    override fun receiveData(data: Any?, mInterface: Any?) {
        if (data == null){
            val intent = Intent(activity, SelectArtistPageActivity::class.java)
            startActivity(intent)
        }
    }

    override fun showArtistPageData(artistPage: ArtistPage) {
        thisIsBandName?.text = artistPage.artistName
    }

    override fun showNoPagesText() {

    }

    override fun showArtistPages(artistPagesList: ArrayList<ArtistPage>) {

    }

}
