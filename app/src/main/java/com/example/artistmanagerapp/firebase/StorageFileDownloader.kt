package com.example.artistmanagerapp.firebase

import android.graphics.BitmapFactory
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.MediaLoader
import com.example.artistmanagerapp.constants.Constants

class StorageFileDownloader : BaseActivity() {

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
                optionMask = "epkPhotos"
                fileNameMask = "cover.jpg"
            }
        }

        var pathReference = storageRef.child("$optionMask/$id/$fileNameMask")
        pathReference.getBytes(1024*1024*5).addOnSuccessListener { bitmapData ->
            val bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData?.size!!.toInt())
            mediaLoader.loadImage(bitmap, null)
        }.addOnFailureListener{
            mediaLoader.onLoadingFailed(it.toString())
        }
    }

}