package com.example.artistmanagerapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.artistmanagerapp.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    // Firebase stuff
    private lateinit var auth: FirebaseAuth
    private lateinit var loginEmailEditText : EditText
    private lateinit var loginPasswordEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Firebase stuff
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        // View components
        val loginButton : Button = findViewById(R.id.login_button) as Button
        loginEmailEditText = findViewById(R.id.login_email_edit_text) as EditText
        loginPasswordEditText = findViewById(R.id.login_password_edit_text) as EditText

        //Variables


        loginButton.setOnClickListener {
            signIn(loadAuthFields(loginEmailEditText, loginPasswordEditText).email, loadAuthFields(loginEmailEditText, loginPasswordEditText).password)
        }

    }

    public override fun onStart() {
        super.onStart()
        FirebaseApp.initializeApp(this)

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun updateUI(user : FirebaseUser?){

    }

    fun signIn(email : String, password : String){

        if (!email.isEmpty() && !password.isEmpty()){

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->

                if (task.isSuccessful){
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Authentication successful.", Toast.LENGTH_SHORT).show()
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
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
