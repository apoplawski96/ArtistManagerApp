//package com.example.artistmanagerapp.activities
//
//import android.content.Intent
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
//import android.provider.MediaStore
//import android.widget.Button
//import android.widget.Toast
//import com.example.artistmanagerapp.R
//import com.example.artistmanagerapp.firebase.StorageFileUploader
//import java.io.IOException
//
//class CreateArtistPageActivity : BaseActivity() {
//
//    // Views
//    var addAvatarButton : Button? = null
//
//    // Objects
//    var fileUploader : StorageFileUploader? = null
//
//    // Firebase stuff
//    var artistPageId : String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_create_artist_page)
//
//        // Objects
//        fileUploader = StorageFileUploader()
//
//        // Views
//        addAvatarButton = findViewById(R.id.select_image_button)
//        addAvatarButton?.setOnClickListener {
//            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(galleryIntent, 1)
//        }
//
//        // Firebase stuff
//        artistPageId = "123"
//
//        Toast.makeText(this, user?.uid.toString(), Toast.LENGTH_SHORT).show()
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        Toast.makeText(this, "kuraw wrucilem", Toast.LENGTH_SHORT).show()
//
//        if (requestCode == 1) {
//            if (data != null) {
//                val contentURI = data!!.data
//                try {
//                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                    val path = fileUploader?.saveImage(bitmap, storageRef.child("artistPagesAvatars/$artistPageId/avatar.jpg"))
//                }
//                catch (e: IOException) {
//                    e.printStackTrace()
//                    //Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//
//
//}
