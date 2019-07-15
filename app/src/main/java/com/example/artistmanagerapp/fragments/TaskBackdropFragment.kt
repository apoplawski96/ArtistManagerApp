package com.example.artistmanagerapp.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.interfaces.TaskDetailPresenter
import com.example.artistmanagerapp.models.Task
import kotlinx.android.synthetic.main.activity_task_details.*

class TaskBackdropFragment : Fragment(){

    // Views
    var taskTitleDisplay : TextView? = null

    // Variables
    var taskTitle : String? = null
    var taskId : String? = null

    // Gettin the arguments "Kotlin way"
    /*override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.getString("task_title")?.let { taskTitle = it }
        arguments?.getString("task_id")?.let { taskId = it }
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_backdrop, container, false)

        // Views
        taskTitleDisplay = rootView.findViewById(R.id.bottomsheet_task_title)

        // Getting the arguments
        val taskTitle : String? = arguments?.getString("TASK_TITLE")

        taskTitleDisplay?.text = taskTitle

        return rootView
    }

    fun getAndSetTaskInfo(){
        var args = getArguments()
        taskTitle = args?.getString("task_title")
        taskTitleDisplay?.text = taskTitle
    }

    companion object {
        @JvmStatic
        fun newInstance(taskTitle: String) : TaskBackdropFragment {
            val fragment = TaskBackdropFragment()

            val bundle = Bundle().apply{
                putString ("TASK_TITLE", taskTitle)
            }

            fragment.arguments = bundle

            return fragment
        }
    }

}
