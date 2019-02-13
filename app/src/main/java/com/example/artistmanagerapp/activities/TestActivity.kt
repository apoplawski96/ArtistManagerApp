package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.google.firebase.FirebaseApp
import android.support.design.widget.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class TestActivity : AppCompatActivity() {

    // WRITE TO DB stuff
    var userData : HashMap <String,Any> = HashMap()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        FirebaseApp.initializeApp(this)

        // WRITE TO DB stuff
        val auth = FirebaseAuth.getInstance()
        var user = auth.currentUser
        val dbButton = findViewById(R.id.write_to_db_button) as Button
        val logoutButton = findViewById(R.id.logout_button) as Button

        Toast.makeText(this, user?.uid.toString(), Toast.LENGTH_SHORT).show()

        dbButton.setOnClickListener {
            writeToDb(user)
        }

        logoutButton.setOnClickListener {
            logout(auth)
        }

    }

    fun writeToDb(user : FirebaseUser?){

        var userId = user?.uid.toString()
        var userEmail = user?.email.toString()

        userData.put("email_address", userEmail)
        userData.put("profileCompletionStatus", "started")
        db.collection("testUsers").document(userId).set(userData).addOnSuccessListener {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Chuj", Toast.LENGTH_SHORT).show()
        }
    }

    fun logout(auth : FirebaseAuth){
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java).apply {
            //putExtra("asdasd", "asda")
        }
        startActivity(intent)
    }

    fun showMessage(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
    }

}
