package com.example.artistmanagerapp.utils

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.activities.CreateOrJoinActivity
import com.example.artistmanagerapp.activities.LoginActivity

object Utils : BaseActivity (){

    fun validateFirstName(textInput : String) : Boolean{

        return true
    }

    fun validateLastName(textInput : String) : Boolean{

        return true
    }

    fun switchActivity(activity : AppCompatActivity){
        val intent = Intent(this, CreateOrJoinActivity::class.java)
        startActivity(intent)
    }


}