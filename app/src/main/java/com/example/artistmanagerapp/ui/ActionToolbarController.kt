package com.example.artistmanagerapp.ui

import android.content.Context
import android.view.ViewGroup

class ActionToolbarController {

    fun activateActionToolbar(type : ActionType, context : Context, parentView : ViewGroup){

    }

    enum class ActionType{
        TD_DUE_DATE_CONFIRM,
        TD_ASSIGNEES_CONFIRM,
        TL_DELETE_TASK,
        DISMISS
    }

    interface ToolbarControllerCallback{
        fun onDueDateConfirm()
        fun onAssigneesConfirm()
        fun onDismiss()
    }

}