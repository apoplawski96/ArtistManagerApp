package com.example.artistmanagerapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.artistmanagerapp.R
import com.example.artistmanagerapp.interfaces.TaskDetailPresenter
import com.example.artistmanagerapp.models.Task
import com.example.artistmanagerapp.utils.TaskHelper
import kotlinx.android.synthetic.main.activity_task_details.*

class TaskDetailsActivity : BaseActivity(), TaskDetailPresenter {

    override fun showTask(taskOutput: Task) {
        taskName.setText(taskOutput.title)
    }

    private var taskData : Task = Task("dummy", "dummy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)
        val taskId = intent.getStringExtra("task_id")
        TaskHelper.getTaskData(perfectArtistPagePath.collection("tasks"),taskId ,this)
    }
}
