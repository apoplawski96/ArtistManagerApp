package com.example.artistmanagerapp.utils

import android.content.Intent
import android.graphics.Color
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.activities.ManageTeamActivity
import com.example.artistmanagerapp.models.RedeemCode
import com.example.artistmanagerapp.models.User
import java.util.*
import kotlin.collections.ArrayList
import com.google.common.io.Files.isFile
import java.io.File
import java.text.DateFormat
import android.support.v4.graphics.drawable.DrawableCompat
import android.graphics.drawable.Drawable
import android.R
import android.content.Context
import android.support.v7.content.res.AppCompatResources
import kotlin.collections.HashMap

object Utils : BaseActivity (){

    private val const = Constants

    fun getCurrentDate() : String {
        val calendar : Calendar = Calendar.getInstance()
        val currentDate : String = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
        return currentDate
    }

    fun getCurrentDateShort() : String{
        val calendar : Calendar = Calendar.getInstance()
        val currentDate : String = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN).format(calendar.time)
        return currentDate
    }

    fun getCurrentTimeShort() : String{
        val calendar : Calendar = Calendar.getInstance()
        val currentTime : String = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.GERMAN).format(calendar.time)
        return currentTime
    }

    fun generateCodeString(lenght : Int) : String{
        val sb = StringBuilder(lenght)
        val rand = Random()

        for (i in 0 until sb.capacity()){
            val index = rand.nextInt(const.ALPHABET.length)
            sb.append(const.ALPHABET[index])
        }

        return sb.toString()
    }

    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir!!.isDirectory()) {
            val children = dir!!.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            return dir!!.delete()
        } else return if (dir != null && dir!!.isFile()) {
            dir!!.delete()
        } else {
            false
        }
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

    fun hardDisableEditText(editText: EditText?){
        editText?.isFocusable = false
        editText?.isEnabled = false
        editText?.isCursorVisible = false
        editText?.keyListener = null
        editText?.setBackgroundColor(Color.TRANSPARENT)
    }

    fun softDisableEditText(editText: EditText?){
        editText?.isFocusable = false
        editText?.isEnabled = false
        //editText?.isCursorVisible = false
        //editText?.keyListener = null
        editText?.setBackgroundColor(Color.TRANSPARENT)
    }

    fun enableEditText(editText: EditText?){
        editText?.isFocusable = true
        editText?.isEnabled = true
        editText?.isCursorVisible = true
        //editText?.keyListener = null
        editText?.setBackgroundColor(Color.TRANSPARENT)
    }

    fun createNameAcronym(firstName : String, lastName : String) : String{
        return "${firstName[0]}${lastName[0]}".toUpperCase()
    }

    fun getUserAccess(currentUserId : String, membersAndRoles : HashMap<String, Any?>?) : ManageTeamActivity.AccessMode{
        lateinit var accessMode : ManageTeamActivity.AccessMode

        if (membersAndRoles != null) {
            for ((memberId, role) in membersAndRoles){
                if (memberId == currentUserId) {
                    when (role as String){
                       "admin" -> accessMode = ManageTeamActivity.AccessMode.ADMIN
                        "regular" -> accessMode = ManageTeamActivity.AccessMode.REGULAR
                    }
                }
            }
        } else {
            accessMode = ManageTeamActivity.AccessMode.ERROR
        }

        return accessMode
    }

    fun setDrawableColor(context : Context, drawableId : Int, color : Int){
        val unwrappedDrawable = AppCompatResources.getDrawable(context, drawableId)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, color)
    }

}