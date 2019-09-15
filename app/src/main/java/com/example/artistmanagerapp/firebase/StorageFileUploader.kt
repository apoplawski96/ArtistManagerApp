package com.example.artistmanagerapp.firebase

import android.graphics.Bitmap
import android.util.Log
import com.example.artistmanagerapp.activities.BaseActivity
import com.example.artistmanagerapp.interfaces.UserInterfaceUpdater
import com.example.artistmanagerapp.constants.Constants
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

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