package com.example.artistmanagerapp.activities

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.adapters.PageTeamAdapter
import com.example.artistmanagerapp.firebase.FirebaseDataReader
import com.example.artistmanagerapp.firebase.FirebaseDataWriter
import com.example.artistmanagerapp.interfaces.UsersListListener
import com.example.artistmanagerapp.models.User
import com.example.artistmanagerapp.ui.DialogCreator
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.UsersHelper
import com.example.artistmanagerapp.utils.Utils
import org.w3c.dom.Text
import android.R.attr.label
import android.content.ClipData
import android.content.Context.CLIPBOARD_SERVICE
import android.content.ClipboardManager
import android.content.Context
import de.hdodenhof.circleimageview.CircleImageView


class ManageTeamActivity : BaseActivity(), UsersListListener{

    // Collections
    var teamMembersList : ArrayList<User> = ArrayList()

    // Bundled page data
    var pageId : String? = null
    var pageName : String? = null
    var epkShareCode : String? = null

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

    // Adapter
    var adapter : PageTeamAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_team)

        // Views
        inviteButton = findViewById(R.id.invite_member_button)
        teamMembersRecyclerView = findViewById(R.id.team_members_recycler_view)

        // Views - invite dialog
        inviteDialog = Dialog(this)
        inviteDialog?.setContentView(R.layout.dialog_invite_member)
        dialogText = inviteDialog?.findViewById(R.id.dialog_redeem_code_text)
        dialogCloseButton = inviteDialog?.findViewById(R.id.dialog_close_x)
        dialogCopyButton = inviteDialog?.findViewById(R.id.dialog_copy_button)

        // Views - user details dialog

        // Getting bundled page data
        pageId = intent.getStringExtra(Constants.PAGE_ID_BUNDLE)
        pageName = intent.getStringExtra(Constants.ARTIST_NAME_BUNDLE)
        epkShareCode = intent.getStringExtra(Constants.EPK_SHARE_CODE_BUNDLE)

        // Getting team members list
        UsersHelper.parseUsers(artistPagesCollectionPath.document(pageId.toString()).collection("pageMembers"), this)

        Toast.makeText(this, "Name:$pageName+ ID:$pageId+ ShareCode:$epkShareCode", Toast.LENGTH_SHORT).show()

        teamMembersRecyclerView?.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        adapter = PageTeamAdapter(this, this, teamMembersList) { item : User -> userItemClicked(item)}
        teamMembersRecyclerView?.adapter = adapter

        inviteButton?.setOnClickListener {
            createRedeemCode()
            setupDialog()
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
        userDetailsDialogCloseX = userDetailsDialog?.findViewById(R.id.user_details_x_button) as TextView?
        userDetailsDialogDisplayName = userDetailsDialog?.findViewById(R.id.user_details_name)
        userDetailsDialogRole = userDetailsDialog?.findViewById(R.id.user_details_role) as TextView?
        userAvatar = userDetailsDialog?.findViewById(R.id.user_details_avatar) as CircleImageView?
        statsCounterLeft = userDetailsDialog?.findViewById(R.id.counter_1) as TextView?
        statsCounterCenter = userDetailsDialog?.findViewById(R.id.counter_2) as TextView?
        statsCounterRight = userDetailsDialog?.findViewById(R.id.counter_3) as TextView?

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

}
