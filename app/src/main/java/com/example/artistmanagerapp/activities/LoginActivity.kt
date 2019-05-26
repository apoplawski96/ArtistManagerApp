package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // View components
        val loginButton : Button = findViewById(R.id.login_button) as Button
        val loginEmailEditText = findViewById(R.id.login_email_edit_text) as EditText
        val loginPasswordEditText = findViewById(R.id.login_password_edit_text) as EditText

        var auth = FirebaseAuth.getInstance()

        // Variables
        var user = auth.currentUser

        // Firebase stuff

        loginButton.setOnClickListener {
            var loadAuthFieldsResult = loadAuthFields(loginEmailEditText, loginPasswordEditText)
            signIn(loadAuthFieldsResult.email, loadAuthFieldsResult.password, auth)
        }

    }

    fun signIn(email : String, password : String, auth : FirebaseAuth){

        if (!email.isEmpty() && !password.isEmpty()){

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->

                if (task.isSuccessful){
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(baseContext, "Authentication successful.", Toast.LENGTH_SHORT).show()
                    // When login is successful - we go to TransitionActivity
                    val intent = Intent(this, TransitionActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
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

    companion object {
        private const val TAG = "EmailPasswordSignIn"
    }

}
