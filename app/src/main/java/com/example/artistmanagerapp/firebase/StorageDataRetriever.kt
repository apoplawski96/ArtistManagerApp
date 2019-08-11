package com.example.artistmanagerapp.firebase

import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.DataReceiver
import com.example.artistmanagerapp.utils.Constants

class StorageDataRetriever : BaseActivity() {

    val c = Constants

    fun loadEpkImage(pageId : String?, dataReceiver : DataReceiver){
        val pathString = "electronicPressKitPhotos/$pageId/cover.jpg"
        val imageRef = storageRef.child(pathString)

    }

}