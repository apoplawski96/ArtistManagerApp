package com.example.artistmanagerapp.activities

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    val CTX_TAG = "LoginActivity"

    var coverSolid : ConstraintLayout? = null
    var coverProgress : ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d(CTX_TAG, "Welcome to LoginActivity")

        // View components
        val loginButton : Button = findViewById(R.id.register_button) as Button
        val loginEmailEditText = findViewById(R.id.register_email_edit_text) as EditText
        val loginPasswordEditText = findViewById(R.id.login_password_edit_text) as EditText
        coverSolid = findViewById(R.id.cover_solid) as ConstraintLayout
        coverProgress = findViewById(R.id.cover_progress) as ProgressBar

        var auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            var loadAuthFieldsResult = loadAuthFields(loginEmailEditText, loginPasswordEditText)
            signIn(loadAuthFieldsResult.email, loadAuthFieldsResult.password, auth)
        }


        already_registered_button.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    fun signIn(email : String, password : String, auth : FirebaseAuth){
        if (!email.isEmpty() && !password.isEmpty()){
            showProgressOverlay()
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    Toast.makeText(baseContext, "Authenticated successfuly", Toast.LENGTH_SHORT).show()
                    // When login is successful - we go to TransitionActivity
                    val intent = Intent(this, TransitionActivity::class.java)
                    startActivity(intent)
                } else {
                    hideProgressOverlay()
                    Toast.makeText(baseContext, "${task.exception}", Toast.LENGTH_SHORT).show()
                }

            }

        } else {
            Toast.makeText(this, "Email and password can't be empty!", Toast.LENGTH_SHORT).show()
        }
    }

    data class LoadAuthFieldsResult(var email : String, var password : String)

    fun loadAuthFields (emailField : EditText?, passwordField : EditText?) : LoadAuthFieldsResult {
        var email = emailField?.text.toString()
        var password = passwordField?.text.toString()
        var result = LoadAuthFieldsResult(email, password)

        return result
    }

    fun showProgressOverlay(){
        coverSolid?.visibility = View.VISIBLE
        coverProgress?.visibility = View.VISIBLE
    }

    fun hideProgressOverlay(){
        coverSolid?.visibility = View.GONE
        coverProgress?.visibility = View.GONE
    }
}
