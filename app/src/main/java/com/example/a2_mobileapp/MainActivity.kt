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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    lateinit var mainbinding: ActivityMainBinding //Main UI zum Kontakte Speichern
    lateinit var loginbinding: ActivityLoginBinding //Login UI
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
            // Benutzer ist eingeloggt, zur nächsten Activity weiterleiten
            val intent = Intent(this, TeilnehmerRegistrierungActivity::class.java)
            startActivity(intent)
            finish() // MainActivity beenden
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
                    // Login erfolgreich
                    // Hier können Sie die nächste Activity starten oder eine Erfolgsmeldung anzeigen
                    val intent = Intent(this, TeilnehmerRegistrierungActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Fehlermeldung anzeigen
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
                    // Registrierung erfolgreich
                    // Weiterleitung zur nächsten Seite oder Erfolgsnachricht
                    showToast("Registrierung erfolgreich!")
                } else {
                    // Fehlermeldung anzeigen
                    task.exception?.message?.let { showToast(it) }
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


}





