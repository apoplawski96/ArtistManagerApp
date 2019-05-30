package com.example.artistmanagerapp.utils

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.activities.CreateOrJoinActivity
import com.example.artistmanagerapp.activities.LoginActivity
import com.example.artistmanagerapp.models.RedeemCode
import java.util.*
import kotlin.collections.ArrayList

object Utils : BaseActivity (){

    private val const = Constants

    fun generateRedeemCodeString() : String{
        val sb = StringBuilder(const.REDEEM_CODE_LENGTH)
        val rand = Random()

        for (i in 0 until sb.capacity()){
            val index = rand.nextInt(const.ALPHABET.length)
            sb.append(const.ALPHABET[index])
        }

        return sb.toString()
    }

    fun checkIfCodeAlreadyExists(redeemCodeString : String, codesList : ArrayList<RedeemCode>) : Boolean{
        var ifExists = false

        for (code in codesList){
            if (code.codeString == redeemCodeString){
                ifExists = true
            }
        }
        return ifExists
    }

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