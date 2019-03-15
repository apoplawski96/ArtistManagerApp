package com.example.artistmanagerapp.utils

import android.content.Intent
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.activities.LoginActivity

object Utils : BaseActivity (){

    fun exitIfUserNotLogged(){
        if (user == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    fun checkIfParsingWorks(){

    }

}