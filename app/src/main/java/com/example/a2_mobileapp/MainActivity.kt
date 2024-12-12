package com.example.a2_mobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.a2_mobileapp.databinding.ActivityLoginBinding
import com.example.a2_mobileapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class MainActivity : ComponentActivity() {

    lateinit var mainbinding: ActivityMainBinding
    lateinit var loginbinding: ActivityLoginBinding
    private lateinit var firebaseRefknt: DatabaseReference
    private lateinit var firebaseRefuser: DatabaseReference
    val auth = FirebaseAuth.getInstance()
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainbinding = ActivityMainBinding.inflate(layoutInflater)
        loginbinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginbinding.root) //Login UI anzeigen
        emailEditText = findViewById(R.id.text_email)
        passwordEditText = findViewById(R.id.text_password)
        loginButton = findViewById(R.id.btn_login)
        registerButton = findViewById(R.id.btn_registration)
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val intent = Intent(this, TeilnehmerRegistrierungActivity::class.java)
            startActivity(intent)
            finish()
        }
        setupClickListeners()
    }

    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            loginUser()
        }

        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (!isValidEmail(email)) {
            showToast("Bitte geben Sie eine gültige E-Mail-Adresse ein.")
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val intent = Intent(this, TeilnehmerRegistrierungActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {

                    task.exception?.message?.let { showToast(it) }
                }
            }
    }

    private fun registerUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (!isValidEmail(email)) {
            showToast("Bitte geben Sie eine gültige E-Mail-Adresse ein.")
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Registrierung erfolgreich!")
                } else {
                    task.exception?.message?.let { showToast(it) }
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    public fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


}





