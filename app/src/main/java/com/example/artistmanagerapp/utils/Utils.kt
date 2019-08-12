package com.example.artistmanagerapp.utils

import android.content.Intent
import android.graphics.Color
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.models.RedeemCode
import java.util.*
import kotlin.collections.ArrayList

object Utils : BaseActivity (){

    private val const = Constants

    fun generateCodeString(lenght : Int) : String{
        val sb = StringBuilder(lenght)
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

    fun selectImageFromDevice(){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, const.GALLERY)
    }

    fun validateFirstName(textInput : String?) : Boolean{

        return true
    }

    fun validateLastName(textInput : String?) : Boolean{

        return true
    }

    fun validatePageName (textInput : String?) : Boolean{
        return (textInput != null) && (textInput.length>2)
    }

    fun switchActivity(activity : AppCompatActivity){
        //val intent = Intent(this, CreateOrJoinActivity::class.java)
        startActivity(intent)
    }

    fun compareLists(listA : ArrayList<Any>, listB : ArrayList<Any>) : Boolean {
        return listA == listB
    }

    fun disableEditText(editText: EditText?){
        editText?.isFocusable = false
        //editText?.isEnabled = false
        editText?.isCursorVisible = false
        editText?.keyListener = null
        editText?.setBackgroundColor(Color.TRANSPARENT)
    }

    fun enableEditText(editText: EditText?){
        editText?.isFocusable = true
        editText?.isEnabled = true
        editText?.isCursorVisible = true
        editText?.keyListener = null
        editText?.setBackgroundColor(Color.TRANSPARENT)
    }

    fun createNameAcronym(firstName : String, lastName : String) : String{
        return "${firstName[0]}${lastName[0]}".toUpperCase()
    }

}