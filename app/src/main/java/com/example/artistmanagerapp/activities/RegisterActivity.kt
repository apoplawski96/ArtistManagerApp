package com.example.artistmanagerapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.widget.*
import com.example.artistmanagerapp.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {

    // Views
    var registerEmailEditText : EditText? = null
    var registerPasswordEditText : EditText? = null
    var registerPasswordRepeatEditText : EditText? = null
    var registerButton : Button? = null
    var alreadyRegisteredButton : TextView? = null
    var coverSolid : ConstraintLayout? = null
    var coverProgress : ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Views
        registerButton = findViewById(R.id.register_button) as Button
        registerEmailEditText = findViewById(R.id.register_email_edit_text) as EditText
        registerPasswordEditText = findViewById(R.id.register_password) as EditText
        registerPasswordRepeatEditText = findViewById(R.id.register_password_repeat) as EditText
        alreadyRegisteredButton = findViewById(R.id.already_registered_button) as TextView
        coverSolid = findViewById(R.id.cover_solid) as ConstraintLayout
        coverProgress = findViewById(R.id.cover_progress) as ProgressBar

        // OnClicks handled
        registerButton?.setOnClickListener {
            // Parsing data from inputs
            var loadAuthFieldsResult = loadAuthFields(registerEmailEditText, registerPasswordEditText, registerPasswordRepeatEditText)

            // Checking if passwords are the same
            if(loadAuthFieldsResult.password.equals(loadAuthFieldsResult.passwordRepeat)){
                signUp(loadAuthFieldsResult.email, loadAuthFieldsResult.password)
            } else {
                Toast.makeText(this, "Passwords does not match, try again", Toast.LENGTH_SHORT).show()
            }
        }

        alreadyRegisteredButton?.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java )
            startActivity(intent)
            //overridePendingTransition(R.anim.slide_right, R.anim.slide_left)
        }
    }



    fun signUp(email : String, password : String){
        if (!email.isEmpty() && !password.isEmpty()){
            showProgressOverlay()
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    coverSolid?.visibility = View.GONE
                    coverProgress?.visibility = View.GONE

                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    hideProgressOverlay()
                    Toast.makeText(baseContext, "Authentication failed." + task.exception.toString(), Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        } else {
            Toast.makeText(this, "Email and password can't be empty!", Toast.LENGTH_SHORT).show()
        }

    }


    fun updateUI(user : FirebaseUser?){
        val intent = Intent(this, TransitionActivity::class.java)
        startActivity(intent)
    }

    data class LoadAuthFieldsResult(var email : String, var password : String, var passwordRepeat : String)

    fun loadAuthFields (emailField : EditText?, passwordField : EditText?, passwordRepeatField : EditText?) : LoadAuthFieldsResult {
        var email = emailField?.text.toString()
        var password = passwordField?.text.toString()
        var passwordRepeat = passwordRepeatField?.text.toString()
        var result = LoadAuthFieldsResult(email, password, passwordRepeat)

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
