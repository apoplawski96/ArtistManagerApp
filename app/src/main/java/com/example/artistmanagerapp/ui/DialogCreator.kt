package com.example.artistmanagerapp.ui

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.utils.ConstMessages
import kotlinx.android.synthetic.main.dialog_standard.view.*

class DialogCreator {

    enum class DialogType {
        CONNECTION_ERROR,
        TASK_DELETE_WARNING
    }

    companion object {
        val msg = ConstMessages

        fun showDialog (type : DialogType, context: Context, dialogControllerCallback: DialogControllerCallback){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.dialog_standard ,null)
            var dialog = Dialog(context)
            dialog.setContentView(view)

            val dialogHeader = view.findViewById(R.id.dialog_header) as TextView
            val dialogMessage = view.findViewById(R.id.dialog_message) as TextView
            val acceptButton = view.findViewById(R.id.dialog_accept_button) as TextView
            val cancelButton = view.findViewById(R.id.dialog_cancel_button) as TextView

            when (type) {
                DialogType.TASK_DELETE_WARNING -> {
                    dialogHeader.text = msg.TASK_REMOVE_WARNING_HEADER
                    dialogMessage.text = msg.TASK_REMOVE_WARNING_BODY
                }
            }

            acceptButton.setOnClickListener {
                dialogControllerCallback.onAccept()
            }

            cancelButton.setOnClickListener {
                dialog.hide()
                dialogControllerCallback.onDismiss()
            }

            dialog.show()
        }

    }

    interface DialogControllerCallback{
        fun onAccept()
        fun onDismiss()
        fun onShown()
    }

}