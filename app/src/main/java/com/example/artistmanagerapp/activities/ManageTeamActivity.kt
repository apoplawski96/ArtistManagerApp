package com.example.artistmanagerapp.activities

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.PageTeamAdapter
import com.example.artistmanagerapp.firebase.FirebaseDataWriter
import com.example.artistmanagerapp.interfaces.UsersListListener
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.ui.DialogCreator
import com.example.artistmanagerapp.constants.Constants
import com.example.artistmanagerapp.utils.Utils
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.*
import com.example.artistmanagerapp.firebase.FirebaseArtistPagesHelper
import com.example.artistmanagerapp.firebase.FirebaseUsersManager
import com.example.artistmanagerapp.models.ArtistPage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_manage_team.*
import kotlinx.android.synthetic.main.dialog_user_details.*
import kotlinx.android.synthetic.main.item_team_member.*
import kotlinx.android.synthetic.main.item_team_member.view.*


class ManageTeamActivity : BaseActivity(), UsersListListener, DialogCreator.DialogControllerCallback{

    enum class AccessMode{
        REGULAR,
        ADMIN,
        ERROR
    }

    // Collections
    var teamMembersList : ArrayList<User> = ArrayList()

    // Bundled page data
    lateinit var userInstance : User
    lateinit var pageInstance : ArtistPage
    var pageId : String? = null
    var pageName : String? = null

    // Views
    var inviteButton : FloatingActionButton? = null
    var teamMembersRecyclerView : RecyclerView? = null

    // Views - invite dialog
    var inviteDialog : Dialog? = null
    var dialogText : EditText? = null
    var dialogCloseButton : TextView? = null
    var dialogCopyButton : Button? = null

    // Views - user details dialog
    var userDetailsDialog : Dialog? = null
    var userDetailsDialogCloseX : TextView? = null
    var userDetailsDialogDisplayName : TextView? = null
    var userDetailsDialogRole : TextView? = null
    var userAvatar : CircleImageView? = null
    var statsCounterLeft : TextView? = null
    var statsCounterCenter : TextView? = null
    var statsCounterRight : TextView? = null

    // Variables
    var codeString : String? = null
    var isAdminModeEnabled : Boolean? = false
    var isAdminSettingsOpen : Boolean? = false
    var currentUserSelected : User? = null
    var currentItemViewSelected : View? = null
    var isAdminButtonChecked : Boolean = false
    var isRegularButtonChecked : Boolean = false

    // Adapter
    var adapter : PageTeamAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_team)

        // Getting bundled page data
        pageId = intent.getStringExtra(Constants.PAGE_ID_BUNDLE)
        pageName = intent.getStringExtra(Constants.ARTIST_NAME_BUNDLE)

        userInstance = intent.getSerializableExtra(Constants.BUNDLE_USER_INSTANCE) as User
        pageInstance = intent.getSerializableExtra(Constants.BUNDLE_ARTIST_PAGE_INSTANCE) as ArtistPage

        // Views
        inviteButton = findViewById(R.id.invite_member_button)
        teamMembersRecyclerView = findViewById(R.id.team_members_recycler_view)

        // Views - invite dialog
        inviteDialog = Dialog(this)
        inviteDialog?.setContentView(R.layout.dialog_invite_member)
        dialogText = inviteDialog?.findViewById(R.id.dialog_redeem_code_text)
        dialogCloseButton = inviteDialog?.findViewById(R.id.dialog_close_x)
        dialogCopyButton = inviteDialog?.findViewById(R.id.dialog_copy_button)

        // Views - admin options dialog

        // Initializing and modifying UI according to access mode
        initUI(Utils.getUserAccess(userInstance.id.toString(), pageInstance.pageAdmins!!))

        // Getting team members list
        FirebaseUsersManager.parseUsers(artistPagesCollectionPath.document(pageId.toString()).collection("pageMembers"), this)

        teamMembersRecyclerView?.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        adapter = PageTeamAdapter(this, this, teamMembersList) {
                item : User -> userItemClicked(item)

        }
        teamMembersRecyclerView?.adapter = adapter

        inviteButton?.setOnClickListener {
            createRedeemCode()
            setupDialog()
        }

    }

    fun initUI(mode : AccessMode){
        when (mode){
            AccessMode.REGULAR -> { admin_tool_icon.visibility = View.GONE }
            AccessMode.ADMIN -> {
                admin_tool_icon.visibility = View.VISIBLE
                admin_tool_icon.setOnClickListener {
                    if (isAdminModeEnabled == false){ enableAdminMode() }
                    else { disableAdminMode() }
                }
            }
        }
    }

    fun showAdminPanel(user: User, itemView: View){
        itemView.admin_settings_opener.setImageResource(R.drawable.ic_settings_red)
        itemView.admin_settings_container.visibility = View.VISIBLE

        when (user.pageRole){
            "admin" -> {
                itemView.admin_radio_button.isChecked = true
                itemView.regular_radio_button.isChecked = false
            }
            "regular" -> {
                itemView.regular_radio_button.isChecked = true
                itemView.admin_radio_button.isChecked = false
            }
        }
    }

    fun onPageRoleChangeButtonClicked(view : View){
        if (view is RadioButton) {
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.admin_radio_button ->
                    if (checked) {
                        isAdminButtonChecked = true
                        isRegularButtonChecked = false
                        regular_radio_button.isChecked = false
                        DialogCreator.showDialog(DialogCreator.DialogType.MEMBER_CHANGE_ROLE_TO_ADMIN_WARNING, this, this)
                    }
                R.id.regular_radio_button ->
                    if (checked) {
                        isRegularButtonChecked = true
                        isAdminButtonChecked = false
                        admin_radio_button.isChecked = false
                        DialogCreator.showDialog(DialogCreator.DialogType.MEMBER_CHANGE_ROLE_TO_REGULAR_WARNING, this, this)
                    }
            }
        }
    }



    fun hideAdminPanel(user: User, itemView: View){
        itemView.admin_settings_opener.setImageResource(R.drawable.ic_settings_white)
        itemView.admin_settings_container.visibility = View.GONE
    }

    override fun controlAdminPanel(user: User, itemView: View) {
        if (isAdminSettingsOpen == false){
            // Setting up variables
            isAdminSettingsOpen = true
            currentItemViewSelected = itemView
            currentUserSelected = user
            // Showing the panel
            showAdminPanel(user, itemView)
        } else {
            if (currentItemViewSelected!! == itemView){
                isAdminSettingsOpen = false
                hideAdminPanel(currentUserSelected!!, currentItemViewSelected!!)
                currentUserSelected = null
                currentItemViewSelected = null
            } else {
                isAdminSettingsOpen = true
                hideAdminPanel(currentUserSelected!!, currentItemViewSelected!!)
                currentItemViewSelected = itemView
                currentUserSelected = user
                showAdminPanel(user, itemView)
            }
        }
    }

    fun enableAdminMode(){
        isAdminModeEnabled = true
        Toast.makeText(this, "Admin mode enabled", Toast.LENGTH_SHORT).show()
        admin_tool_icon.setImageResource(R.drawable.admin_tool_icon_green)
        admin_settings_opener.visibility = View.VISIBLE
        inviteButton?.show()
    }

    fun disableAdminMode(){
        isAdminModeEnabled = false
        Toast.makeText(this, "Admin mode disabled", Toast.LENGTH_SHORT).show()
        admin_tool_icon.setImageResource(R.drawable.ic_admin_tool)
        admin_settings_opener.visibility = View.GONE
        inviteButton?.hide()

        if (isAdminSettingsOpen == true){
            isAdminSettingsOpen = false
            hideAdminPanel(currentUserSelected!!, currentItemViewSelected!!)
            currentUserSelected = null
            currentItemViewSelected = null
        }
    }

    fun createRedeemCode(){
        codeString = Utils.generateCodeString(Constants.REDEEM_CODE_LENGTH)
        FirebaseDataWriter().generateRedeemCode(codeString.toString(), userId, pageId.toString())
    }

    fun setupDialog(){
        dialogText?.setText(codeString.toString())
        Utils.softDisableEditText(dialogText)

        dialogCopyButton?.setOnClickListener {
            // Setting up copying
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied redeem code", codeString.toString())
            clipboard.primaryClip = clip
            Toast.makeText(this, "Code copied!", Toast.LENGTH_SHORT).show()
        }

        dialogCloseButton?.setOnClickListener { inviteDialog?.hide() }

        inviteDialog?.show()
    }

    fun userItemClicked(user : User){
        // Dialog setup
        setupUserDetailsDialog(user)
        userDetailsDialog?.show()
    }

    fun setupUserDetailsDialog(user : User){
        // Views initialization
        userDetailsDialog = Dialog(this)
        userDetailsDialog?.setContentView(R.layout.dialog_user_details)
        userDetailsDialogCloseX = userDetailsDialog?.findViewById(R.id.dialog_close_x) as TextView?
        userDetailsDialogDisplayName = userDetailsDialog?.findViewById(R.id.user_details_name)
        userDetailsDialogRole = userDetailsDialog?.findViewById(R.id.user_details_role) as TextView?
        userAvatar = userDetailsDialog?.findViewById(R.id.circle_avatar_background) as CircleImageView?
        userDetailsDialog!!.user_acronym.text = user.getAcronym()
        //statsCounterLeft = userDetailsDialog?.findViewById(R.id.counter_1) as TextView?
        //statsCounterCenter = userDetailsDialog?.findViewById(R.id.counter_2) as TextView?
        //statsCounterRight = userDetailsDialog?.findViewById(R.id.counter_3) as TextView?

        // Views setup with data
        userDetailsDialogDisplayName?.text = "${user.firstName.toString()} ${user.lastName.toString()}"
        userDetailsDialogRole?.text = user.artistRole.toString()

        // OnClicks
        userDetailsDialogCloseX?.setOnClickListener {
            userDetailsDialog?.hide()
        }

    }

    override fun updateList(usersOutput: ArrayList<User>) {
        adapter?.updateItems(usersOutput)
    }

    override fun onAccept(option: DialogCreator.DialogControllerCallback.CallbackOption?) {
        when (option){
            DialogCreator.DialogControllerCallback.CallbackOption.ADMIN_RIGHTS_OBTAIN_ATTEMPT -> {
                showProgressAdminMode()
                FirebaseUsersManager.changeUserAccessRights()
            }
            DialogCreator.DialogControllerCallback.CallbackOption.REGULAR_RIGHTS_OBTAIN_ATTEMPT -> {
                showProgressAdminMode()
                FirebaseUsersManager.changeUserAccessRights()
            }
            DialogCreator.DialogControllerCallback.CallbackOption.MEMBER_REMOVED -> {
                showProgressAdminMode()
                FirebaseArtistPagesHelper.removeMember()
            }
        }
    }

    fun showProgressAdminMode(){
        admin_mode_progress_bar.visibility = View.VISIBLE
    }

    override fun onDismiss() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShown() {

    }

    override fun onDismissWithOption(option: DialogCreator.DialogControllerCallback.DismissCalbackOption) {
        when (option){

        }
    }

    override fun onCallInvalid() {

    }

    override fun onCodeRedeemed(pageId: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    interface TeamManagementPanelController{

    }

}
