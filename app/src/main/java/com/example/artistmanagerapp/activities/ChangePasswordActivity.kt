package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : BaseActivity() {

    val TAG = "ChangePasswordActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        change_password_button.setOnClickListener {
            change_password_progress_bar.visibility = View.VISIBLE
            changeUserPassword()
        }

        return_button.setOnClickListener {
            onBackPressed()
        }
    }

    fun changeUserPassword(){
        if (old_password_input.text.isNotEmpty() && new_password_input.text.isNotEmpty() && new_password_repeat_input.text.isNotEmpty()){

            if (new_password_input.text.toString() == new_password_repeat_input.text.toString()){

                val user = auth.currentUser

                if (user != null && user.email != null){
                    val credential = EmailAuthProvider.getCredential(user.email!!, old_password_input.text.toString())

                    // Prompt the user to re-provide their sign-in credentials
                    user?.reauthenticate(credential)
                        ?.addOnCompleteListener {

                            if (it.isSuccessful){
                                user?.updatePassword(new_password_input.text.toString())
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            change_password_progress_bar.visibility = View.INVISIBLE
                                            Log.d(TAG, "User password updated.")
                                            Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show()
                                            auth.signOut()
                                            startActivity(Intent(this, TransitionActivity::class.java))
                                            finish()
                                        } else {
                                            change_password_progress_bar.visibility = View.INVISIBLE
                                            Log.d(TAG, task.toString())
                                        }
                                    }
                            } else {
                                change_password_progress_bar.visibility = View.INVISIBLE
                                Toast.makeText(this, "Your current password is incorrect", Toast.LENGTH_SHORT).show()
                            }


                        }

                } else{
                    change_password_progress_bar.visibility = View.INVISIBLE
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

            } else {
                change_password_progress_bar.visibility = View.INVISIBLE
                Toast.makeText(this, "Passwords mismatching", Toast.LENGTH_SHORT).show()
            }

        } else {
            change_password_progress_bar.visibility = View.INVISIBLE
            Toast.makeText(this, "Password fields cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

}
