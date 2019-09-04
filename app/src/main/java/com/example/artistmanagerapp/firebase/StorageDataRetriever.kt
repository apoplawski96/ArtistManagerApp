package com.example.artistmanagerapp.firebase

import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.DataReceiver
import com.example.artistmanagerapp.interfaces.MediaLoader
import com.example.artistmanagerapp.ui.DialogCreator
import com.example.artistmanagerapp.utils.Constants
import com.example.artistmanagerapp.utils.ElectronicPressKitHelper
import com.google.firebase.firestore.CollectionReference

class StorageDataRetriever : BaseActivity() {

    val c = Constants

    enum class DownloadOption{
        USER_AVATAR,
        PAGE_AVATAR,
        EPK_COVER_PHOTO
    }

    fun downloadImageViaId(id : String?, option : DownloadOption, mediaLoader: MediaLoader){
        var optionMask : String? = null
        var fileNameMask : String? = null

        when (option){
            DownloadOption.USER_AVATAR -> {
                optionMask = "avatars"
                fileNameMask = "avatar.jpg"
            }
            DownloadOption.PAGE_AVATAR -> {
                optionMask = "pageAvatars"
                fileNameMask = "avatar.jpg"
            }
            DownloadOption.EPK_COVER_PHOTO -> {
                optionMask = "electronicPressKitPhotos"
                fileNameMask = "cover.jpg"
            }
        }

        var pathReference = storageRef.child("$optionMask/$id/$fileNameMask")
        pathReference.getBytes(1024*1024*5).addOnSuccessListener { bitmapData ->
            val bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData?.size!!.toInt())
            mediaLoader.loadImage(bitmap, null)
        }.addOnSuccessListener {
            Log.d("Storage", "Image successfully loaded to a bitmap")
        }.addOnFailureListener{
            Log.d("Storage", it.toString())
        }

    }

}