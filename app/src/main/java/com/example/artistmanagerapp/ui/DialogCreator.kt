package com.example.artistmanagerapp.ui

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.interfaces.DataReceiver
import com.example.artistmanagerapp.utils.ConstMessages
import com.example.artistmanagerapp.utils.ElectronicPressKitHelper
import com.example.artistmanagerapp.utils.Utils
import kotlinx.android.synthetic.main.dialog_standard.view.*

class DialogCreator{

    enum class DialogType {
        CONNECTION_ERROR,
        TASK_DELETE_WARNING,
        MISSING_EPK_DATA,
        SHARE_EPK_DIALOG,
        REDEEM_EPK_DIALOG,
        EPK_NOT_GENERATED,
        INVITE_CODE_GENERATED,
        EPK_PHOTO_NOT_FOUND,
        MEMBER_DELETE_WARNING,
        MEMBER_CHANGE_ROLE_TO_ADMIN_WARNING,
        MEMBER_CHANGE_ROLE_TO_REGULAR_WARNING
    }

    companion object : DataReceiver {
        val msg = ConstMessages
        var callbackOption : DialogControllerCallback.CallbackOption? = null

        // Standard Dialog setup
        fun showDialog (type : DialogType, context: Context, dialogControllerCallback: DialogControllerCallback){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.dialog_standard ,null)
            var dialog = Dialog(context)
            dialog.setContentView(view)
            var option : DialogControllerCallback.CallbackOption? = null

            val dialogHeader = view.findViewById(R.id.dialog_header) as TextView
            val dialogMessage = view.findViewById(R.id.dialog_message) as TextView
            val acceptButton = view.findViewById(R.id.dialog_accept_button) as TextView
            val cancelButton = view.findViewById(R.id.dialog_cancel_button) as TextView

            when (type) {
                DialogType.TASK_DELETE_WARNING -> {
                    dialogHeader.text = msg.TASK_REMOVE_WARNING_HEADER
                    dialogMessage.text = msg.TASK_REMOVE_WARNING_BODY
                }
                DialogType.MISSING_EPK_DATA -> {
                    dialogHeader.text = msg.MISSING_EPK_DATA_HEADER
                    dialogMessage.text = msg.MISSING_EPK_DATA_BODY
                    cancelButton.visibility = View.GONE
                    acceptButton.text = "I understand"
                }
                DialogType.EPK_NOT_GENERATED -> {
                    dialogHeader.text = msg.SHARE_MISSING_EPK_DATA_HEADER
                    dialogMessage.text = msg.SHARE_MISSING_EPK_DATA_BODY
                    cancelButton.visibility = View.GONE
                    acceptButton.text = "I understand"
                }
                DialogType.EPK_PHOTO_NOT_FOUND -> {
                    dialogHeader.text = msg.MISSING_EPK_PHOTO_HEADER
                    dialogMessage.text = msg.MISSING_EPK_PHOTO_BODY
                    cancelButton.visibility = View.GONE
                    acceptButton.text = "I understand"
                }
                DialogType.MEMBER_CHANGE_ROLE_TO_ADMIN_WARNING -> {
                    dialogHeader.text = msg.ROLE_CHANGE_HEADER_A
                    dialogMessage.text = msg.ROLE_CHANGE_BODY_A
                    option = DialogControllerCallback.CallbackOption.ADMIN_RIGHTS_OBTAIN_ATTEMPT
                    acceptButton.text = "I understand"
                }
                DialogType.MEMBER_CHANGE_ROLE_TO_REGULAR_WARNING -> {
                    dialogHeader.text = msg.ROLE_CHANGE_HEADER_R
                    dialogMessage.text = msg.ROLE_CHANGE_BODY_R
                    option = DialogControllerCallback.CallbackOption.REGULAR_RIGHTS_OBTAIN_ATTEMPT
                    acceptButton.text = "I understand"
                }
                DialogType.MEMBER_DELETE_WARNING -> {
                    dialogHeader.text = msg.MEMBER_REMOVE_HEADER
                    dialogMessage.text = msg.MEMBER_REMOVE_BODY
                    option = DialogControllerCallback.CallbackOption.MEMBER_REMOVED
                    acceptButton.text = "I understand"
                }
            }

            acceptButton.setOnClickListener {
                dialog.hide()
                dialogControllerCallback.onAccept(option)
            }

            cancelButton.setOnClickListener {
                dialog.hide()
                dialogControllerCallback.onDismiss()
            }

            dialog.show()
        }

        // Code Dialog setup
        fun showCodeDialog (type : DialogType, context: Context, dialogControllerCallback: DialogControllerCallback, textContent : String?){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.dialog_mod_dynamic ,null)
            var dialog = Dialog(context)
            dialog.setContentView(view)

            val dialogText : EditText = view.findViewById(R.id.dialog_mod_text)
            val dialogButton : Button = view.findViewById(R.id.dialog_mod_btn)
            val dialogCloseX : TextView = view.findViewById(R.id.dialog_mod_close_x)
            val dialogProgressBar : ProgressBar = view.findViewById(R.id.dialog_mod_progress_bar)

            when (type) {
                DialogType.SHARE_EPK_DIALOG -> {
                    dialogButton.text = msg.COPY_TEXT
                    Utils.hardDisableEditText(dialogText)
                    dialogText.setText(textContent.toString())
                }
                DialogType.REDEEM_EPK_DIALOG -> {
                    dialogButton.text = "REDEEM EPK CODE"
                    callbackOption = DialogControllerCallback.CallbackOption.CODE_REDEEMED
                }
                DialogType.INVITE_CODE_GENERATED -> {
                    dialogButton.text = msg.COPY_TEXT
                    Utils.hardDisableEditText(dialogText)
                    dialogText.setText(textContent.toString())
                }
            }

            dialogButton.setOnClickListener {
                if (callbackOption == DialogControllerCallback.CallbackOption.CODE_REDEEMED){
                    dialogProgressBar.visibility = View.VISIBLE
                    ElectronicPressKitHelper.redeemShareCode(dialogText.text.toString(), this, dialogControllerCallback) //tutaj receiver dostaje dane o pomyslnym lub niepomyslnym zreedemowaniu kodu
                } else{
                    dialog.hide()
                }
                dialogControllerCallback.onAccept(callbackOption)
            }

            dialogCloseX.setOnClickListener {
                dialog.hide()
                dialogControllerCallback.onDismiss()
            }

            dialog.show()
        }

        // DataReceiver inteface implementation
        override fun receiveData(data: Any?, mInterface: Any?) {
            if (data == null){
                //hideProgressBar()
                //showInvalidCodeMessage()
            } else{
                mInterface as DialogControllerCallback
                mInterface.onCodeRedeemed(data.toString())
            }
        }

    }

    interface DialogControllerCallback{

        enum class CallbackOption {
            CODE_REDEEMED,
            ADMIN_RIGHTS_OBTAIN_ATTEMPT,
            REGULAR_RIGHTS_OBTAIN_ATTEMPT,
            MEMBER_REMOVED
        }

        enum class DismissCalbackOption {
            ADMIN_RIGHTS_OBTAIN_ATTEMPT,
            REGULAR_RIGHTS_OBTAIN_ATTEMPT
        }

        fun onAccept(option : CallbackOption?)
        fun onDismiss()
        fun onDismissWithOption(option : DismissCalbackOption)
        fun onShown()
        fun onCallInvalid()
        fun onCodeRedeemed(pageId : String?)
    }

}