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

class ManageTeamActivity : BaseActivity(), UsersListListener{

    // Collections
    var teamMembersList : ArrayList<User> = ArrayList()

    // Bundled page data
    var pageId : String? = null
    var pageName : String? = null
    var epkShareCode : String? = null

    // Views
    var inviteButton : FloatingActionButton? = null
    var inviteDialog : Dialog? = null
    var dialogText : EditText? = null
    var dialogCloseButton : TextView? = null
    var dialogCopyButton : Button? = null
    var teamMembersRecyclerView : RecyclerView? = null

    // Variables
    var codeString : String? = null

    // Adapter
    var adapter : PageTeamAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_team)

        // Views
        inviteButton = findViewById(R.id.invite_member_button)
        inviteDialog = Dialog(this)
        inviteDialog?.setContentView(R.layout.dialog_invite_member)
        dialogText = inviteDialog?.findViewById(R.id.dialog_redeem_code_text)
        dialogCloseButton = inviteDialog?.findViewById(R.id.dialog_close_x)
        dialogCopyButton = inviteDialog?.findViewById(R.id.dialog_copy_button)
        teamMembersRecyclerView = findViewById(R.id.team_members_recycler_view)

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
        Utils.disableEditText(dialogText)

        dialogCopyButton?.setOnClickListener { Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show() }

        dialogCloseButton?.setOnClickListener { inviteDialog?.hide() }

        inviteDialog?.show()
    }

    fun userItemClicked(user : User){

    }

    override fun updateList(usersOutput: ArrayList<User>) {
        adapter?.updateItems(usersOutput)
    }

}
