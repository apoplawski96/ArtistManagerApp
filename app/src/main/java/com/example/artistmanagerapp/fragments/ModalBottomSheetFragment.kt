package com.example.artistmanagerapp.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.artistmanagerapp.R

class ModalBottomSheetFragment : BottomSheetDialogFragment() {

    private var listener : BottomSheetListener? = null
    var taskDisplayTitle : TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.layout_bottom_sheet, container, false)

        // Views
        taskDisplayTitle = rootView.findViewById(R.id.bottomsheet_task_title)

        // Getting data from TaskListActivity
        val taskTitle : String = arguments?.getString("TASK_TITLE").toString()

        taskDisplayTitle?.text = taskTitle

        return rootView
    }

    interface BottomSheetListener {

        fun sendDataBack(){

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        //listener = context as BottomSheetListener
    }

    companion object {
        @JvmStatic
        fun newInstance(taskTitle: String?) : ModalBottomSheetFragment {
            val fragment = ModalBottomSheetFragment()

            val bundle = Bundle().apply{
                putString ("TASK_TITLE", taskTitle)
            }

            fragment.arguments = bundle

            return fragment
        }
    }
}