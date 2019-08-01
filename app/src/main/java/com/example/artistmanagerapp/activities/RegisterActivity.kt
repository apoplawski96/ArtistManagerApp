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

class RegisterActivity : AppCompatActivity() {

    // Firebase stuff
    private lateinit var auth: FirebaseAuth

    // View components
    private lateinit var registerEmailEditText : EditText
    private lateinit var registerPasswordEditText : EditText
    private lateinit var registerPasswordRepeatEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Firebase stuff
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        val registerButton : Button = findViewById(R.id.register_button) as Button
        registerEmailEditText = findViewById(R.id.register_email_edit_text) as EditText
        registerPasswordEditText = findViewById(R.id.register_password_edit_text) as EditText
        registerPasswordRepeatEditText = findViewById(R.id.register_passwordrepeat_edittext) as EditText

        registerButton.setOnClickListener {
            var loadAuthFieldsResult = loadAuthFields(registerEmailEditText, registerPasswordEditText, registerPasswordRepeatEditText)

            if(loadAuthFieldsResult.password.equals(loadAuthFieldsResult.passwordRepeat)){
                signUp(loadAuthFieldsResult.email, loadAuthFieldsResult.password)
            } else {
                Toast.makeText(this, "Password fields do not equal", Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun signUp(email : String, password : String){

        if (!email.isEmpty() && !password.isEmpty()){

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed." + task.exception.toString(), Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }

        } else {
            Toast.makeText(this, "Email and password can't be empty!", Toast.LENGTH_SHORT).show()
        }

    }


    fun updateUI(user : FirebaseUser?){

    }

    data class LoadAuthFieldsResult(var email : String, var password : String, var passwordRepeat : String)

    fun loadAuthFields (emailField : EditText?, passwordField : EditText?, passwordRepeatField : EditText?) : LoadAuthFieldsResult {
        var email = emailField?.text.toString()
        var password = passwordField?.text.toString()
        var passwordRepeat = passwordRepeatField?.text.toString()
        var result = LoadAuthFieldsResult(email, password, passwordRepeat)

        return result
    }

    companion object {
        private const val TAG = "EmailPasswordSignUp"
    }

}
