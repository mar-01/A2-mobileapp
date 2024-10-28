package com.example.a2_mobileapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.a2_mobileapp.databinding.ActivityLoginBinding
import com.example.a2_mobileapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : ComponentActivity() {

    lateinit var mainbinding : ActivityMainBinding //Main UI zum Kontakte Speichern
    lateinit var loginbinding : ActivityLoginBinding //Login UI
    private lateinit var  firebaseRefknt : DatabaseReference
    private lateinit var  firebaseRefuser : DatabaseReference
    val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainbinding = ActivityMainBinding.inflate(layoutInflater)
        loginbinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginbinding.root) //Login UI anzeigen

        firebaseRefuser = FirebaseDatabase.getInstance("https://a2-mobileapp-default-rtdb.europe-west1.firebasedatabase.app").getReference("User")
        loginbinding.btnRegistration.setOnClickListener {
            newuser()
        }

        loginbinding.btnLogin.setOnClickListener {
            signIn()
        }

        firebaseRefknt = FirebaseDatabase.getInstance("https://a2-mobileapp-default-rtdb.europe-west1.firebasedatabase.app").getReference("Kontakte")
        mainbinding.btnSenddata.setOnClickListener {
            senddata()
        }
    }

    //User anmelden
    private fun signIn(){

    }

    //User registrieren
    private fun newuser(){
        val  username = loginbinding.textUsername.text.toString()
        val  password = loginbinding.textPassword.text.toString()

        if(username.isEmpty()) {
            loginbinding.textUsername.error = "Username angeben"
            if(password.isEmpty()) loginbinding.textPassword.error = "Passwort angeben"
            Toast.makeText(this, "Fehler beim erstellen des Accounts", Toast.LENGTH_SHORT).show()
            return
        }

        if(password.isEmpty()) {
            loginbinding.textPassword.error = "Passwort angeben"
            Toast.makeText(this, "Fehler beim erstellen des Accounts", Toast.LENGTH_SHORT).show()
            return
        }

        val email = "$username@example.com"
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    val usermap = hashMapOf("username" to username)

                    if(uid != null) {
                        firebaseRefuser.child(uid).setValue(usermap)
                    }
                } else {
                    Toast.makeText(this, "Registrierung fehlgeschlagen", Toast.LENGTH_SHORT).show()
                }
            }

    }

    //Kontakte an die Datenbank schicken
    private fun senddata(){
        val name = mainbinding.textName.text.toString()
        val nummer = mainbinding.textNummer.text.toString()

        if(name.isEmpty()) {
            mainbinding.textName.error = "Name einfügen"
            if(nummer.isEmpty()) mainbinding.textNummer.error = "Nummer einfügen"
            Toast.makeText(this, "Fehler beim Hochladen der Daten", Toast.LENGTH_SHORT).show()
            return
        }

        if (nummer.isEmpty()) {
            mainbinding.textNummer.error = "Nummer einfügen"
            Toast.makeText(this, "Fehler beim Hochladen der Daten", Toast.LENGTH_SHORT).show()
            return
        }

        val kontaktId = firebaseRefknt.push().key!!
        val kontakt = Kontakte(kontaktId, name, nummer)

        firebaseRefknt.child(kontaktId).setValue(kontakt)
            .addOnCompleteListener {
                Toast.makeText(this, "Daten hochgeladen", Toast.LENGTH_SHORT).show()
            }
    }
}
