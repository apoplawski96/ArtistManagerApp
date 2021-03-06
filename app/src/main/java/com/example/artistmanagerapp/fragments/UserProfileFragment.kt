package com.example.artistmanagerapp.fragments


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.activities.ChangePasswordActivity
import com.example.artistmanagerapp.activities.CreateUserProfileActivity
import com.example.artistmanagerapp.activities.LoginActivity
import com.example.artistmanagerapp.activities.SelectArtistPageActivity
import com.example.artistmanagerapp.firebase.StorageFileDownloader
import com.example.artistmanagerapp.interfaces.MediaLoader
import com.example.artistmanagerapp.models.ArtistPage
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.constants.Constants
import com.example.artistmanagerapp.utils.Utils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_user_profile.view.*

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
    var pageInstance : ArtistPage? = null

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
        fun newInstance(pageId : String, user : User?, artistPage : ArtistPage?) : UserProfileFragment {
            val c = Constants
            val fragment = UserProfileFragment()
            val bundle = Bundle().apply{
                putString ("PAGE_ID", pageId)
                putSerializable(Constants.BUNDLE_USER_INSTANCE, user)
                putSerializable(Constants.BUNDLE_ARTIST_PAGE_INSTANCE, artistPage)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var rootView : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_user_profile, container, false)
        Log.d(CTX_TAG, "Welcome to UserProfileFragment!")

        // Getting User bundled data
        pageId = arguments?.getString("PAGE_ID")
        userInstance = arguments?.getSerializable(Constants.BUNDLE_USER_INSTANCE) as User?
        pageInstance = arguments?.getSerializable(Constants.BUNDLE_ARTIST_PAGE_INSTANCE) as ArtistPage?

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
        StorageFileDownloader().downloadImageViaId(userInstance?.id, StorageFileDownloader.DownloadOption.USER_AVATAR, this)

        editProfile?.setOnClickListener{
            val intent = Intent(activity, CreateUserProfileActivity::class.java)
            intent?.apply {
                putExtra(Constants.MODE_KEY, Constants.USER_PROFILE_EDIT_MODE)
                putExtra(Constants.BUNDLE_USER_INSTANCE, userInstance)
            }
            startActivity(intent)
        }

        changePassword?.setOnClickListener {
            startActivity(Intent(activity, ChangePasswordActivity::class.java))
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

        rootView.home_screen_back_button.setOnClickListener {
            var intent = Intent(activity, SelectArtistPageActivity::class.java).apply { putExtra (
                Constants.PAGE_ID_BUNDLE, pageId) }
            putDataToBundle(intent)
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

        when (userInstance!!.roleCategory){
            "artist" -> {
                rootView.artist_category_display.layoutParams.width = 150
            }
            "manager" -> {
                rootView.artist_category_display.layoutParams.width = 235
            }
        }
        rootView.user_profession_display.text = userInstance!!.artistRole
        rootView.artist_category_display.text = userInstance!!.roleCategory
        displayName?.text = "${userInstance?.firstName} ${userInstance?.lastName}"
    }

    fun showProgress(){
        avatarProgressBar?.visibility = View.VISIBLE
        userAvatar?.visibility = View.INVISIBLE
    }

    fun hideProgress(){
        userAvatar?.visibility = View.VISIBLE
        avatarProgressBar?.visibility = View.INVISIBLE
    }

    override fun onLoadingFailed(error: String?) {

    }

    fun putDataToBundle(intent : Intent?){
        intent?.putExtra (Constants.PAGE_ID_BUNDLE, pageId)
        intent?.putExtra (Constants.BUNDLE_USER_INSTANCE, userInstance)
        intent?.putExtra (Constants.BUNDLE_ARTIST_PAGE_INSTANCE, pageInstance)
    }

}
