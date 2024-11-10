package com.example.a2_mobileapp
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TeilnehmerRegistrierungActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var matrikelnummerEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var logoutButton: Button
    private lateinit var viewListButton: Button
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teilnehmer_registrierung)

        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        matrikelnummerEditText = findViewById(R.id.matrikelnummerEditText)
        submitButton = findViewById(R.id.submitButton)
        logoutButton = findViewById(R.id.logoutButton)
        viewListButton = findViewById(R.id.viewListButton)

        setupClickListeners()
        submitButton.setOnClickListener {
            saveUserData()
        }
    }

    private fun setupClickListeners() {
        submitButton.setOnClickListener {
            saveUserData()
        }
        logoutButton.setOnClickListener {
            logoutUser()
        }
        viewListButton.setOnClickListener {
            val intent = Intent(this, TeilnehmerListeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveUserData() {
        if (!isInputValid()) {
            return
        }

        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val matrikelnummer = matrikelnummerEditText.text.toString()
        val user = hashMapOf(
            "name" to name,
            "email" to email,
            "matrikelnummer" to matrikelnummer
        )

        db.collection("teilnehmer").add(user)
            .addOnSuccessListener {
                showToast("Daten erfolgreich gespeichert")
                clearFields()
            }
            .addOnFailureListener { e ->
                showToast("Fehler: ${e.message}")
            }
    }
    private fun logoutUser() {
        auth.signOut()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isInputValid(): Boolean {
        val email = emailEditText.text.toString()
        val name = nameEditText.text.toString()
        val matrikelnummer = matrikelnummerEditText.text.toString()

        if (email.isEmpty()) {
            showToast("Das Feld E-Mail darf nicht leer sein.")
            return false
        }
        if (name.isEmpty()) {
            showToast("Das Feld Name darf nicht leer sein.")
            return false
        }
        if (matrikelnummer.isEmpty()) {
            showToast("Das Feld Matrikelnummer darf nicht leer sein.")
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Bitte geben Sie eine gültige E-Mail-Adresse ein.")
            return false
        }

        if (!name.all { it.isLetter() || it.isWhitespace() }) {
            showToast("Bitte geben Sie einen gültigen Namen ein (nur Buchstaben).")
            return false
        }

        if (!matrikelnummer.all { it.isDigit() }) {
            showToast("Bitte geben Sie eine gültige Matrikelnummer ein (nur Zahlen).")
            return false
        }

        return true
    }

    private fun clearFields() {
        nameEditText.text.clear()
        emailEditText.text.clear()
        matrikelnummerEditText.text.clear()
    }

}