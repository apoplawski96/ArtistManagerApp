package com.example.artistmanagerapp.firebase

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.utils.Constants
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class StorageFileUploader : BaseActivity() {

    val c = Constants

    fun saveImage(myBitmap : Bitmap?, storagePath : StorageReference, presenter : UserInterfaceUpdater?) {
        val bytes = ByteArrayOutputStream()
        myBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val data = bytes.toByteArray()

        var uploadTask = storagePath.putBytes(data)
        uploadTask.addOnSuccessListener {
            presenter?.updateUI(c.IMAGE_SUCCESSFULLY_UPLOADED, null)
            presenter?.hideProgress()
        }.addOnFailureListener {
            Log.d(FIREBASE_ERROR, "Failure: $it")
        }
    }
}