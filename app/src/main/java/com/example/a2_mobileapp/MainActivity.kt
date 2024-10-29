package com.example.a2_mobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.a2_mobileapp.databinding.ActivityLoginBinding
import com.example.a2_mobileapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

var useruid = ""

class MainActivity : ComponentActivity() {

    lateinit var mainbinding : ActivityMainBinding //Main UI zum Kontakte Speichern
    lateinit var loginbinding : ActivityLoginBinding //Login UI
    private lateinit var  firebaseRefknt : DatabaseReference
    private lateinit var  firebaseRefuser : DatabaseReference
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainbinding = ActivityMainBinding.inflate(layoutInflater)
        loginbinding = ActivityLoginBinding.inflate(layoutInflater)
        if(auth.currentUser != null) {
            setContentView(mainbinding.root) //Main UI anzeigen wenn User eingeloggt
        } else {
            setContentView(loginbinding.root) //Login UI anzeigen wenn noch kein User eingeloggt
        }

        firebaseRefuser = FirebaseDatabase.getInstance("https://a2-mobileapp-default-rtdb.europe-west1.firebasedatabase.app").getReference("User")
        loginbinding.btnRegistration.setOnClickListener {
            newuser()
        }

        loginbinding.btnLogin.setOnClickListener {
            signIn() { uid ->
                if(uid != null) {
                    useruid = uid
                    Log.d("Authstatus", "Anmeldung erfolgt, UID: $useruid")
                } else {
                    Log.d("Authstatus", "Anmeldung fehlgeschlagen")
                }
            }
        }

        firebaseRefknt = FirebaseDatabase.getInstance("https://a2-mobileapp-default-rtdb.europe-west1.firebasedatabase.app").getReference("Kontakte")
        mainbinding.btnSenddata.setOnClickListener {
            senddata(useruid)
        }
    }

    //User anmelden
    private fun signIn(callback: (String?) -> Unit){
        val  username = loginbinding.textUsername.text.toString() //Eingabefeld auslesen
        val  password = loginbinding.textPassword.text.toString() //Eingabefeld auslesen

        //Eingabefelder prüfen ob sie ausgefüllt sind
        if(username.isEmpty()) {
            loginbinding.textUsername.error = "Username angeben"
            if(password.isEmpty()) loginbinding.textPassword.error = "Passwort angeben"
            Toast.makeText(this, "Fehler bei der Anmeldung", Toast.LENGTH_SHORT).show()
            callback(null)
        }

        if(password.isEmpty()) {
            loginbinding.textPassword.error = "Passwort angeben"
            Toast.makeText(this, "Fehler bei der Anmeldung", Toast.LENGTH_SHORT).show()
            callback(null)
        }

        val email = "$username@example.com"
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this, "Anmeldung erfolgreich!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java)) //Main UI aufrufen
                    finish() //aktuelle Activity beenden
                    val uid = auth.currentUser?.uid
                    callback(uid)
                } else {
                    Toast.makeText(this, "Anmeldung fehlgeschlagen!", Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }
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
                        Toast.makeText(this, "Registrierung erfolgreich!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Registrierung fehlgeschlagen", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //Kontakte an die Datenbank schicken
    private fun senddata(uid:String){
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

        if(uid != null){
            val kontaktId = firebaseRefknt.push().key!!
            val kontakt = Kontakte(kontaktId, name, nummer)

            firebaseRefknt.child(uid).child(kontaktId).setValue(kontakt)
                .addOnCompleteListener {
                    Toast.makeText(this, "Daten hochgeladen", Toast.LENGTH_SHORT).show()
                }
        }else {
            Toast.makeText(this, "Fehler: Daten nicht hochgeladen", Toast.LENGTH_SHORT).show()
        }
    }

    // Benutzer automatisch abmelden, wenn die App geschlossen wird
    override fun onDestroy() {
        super.onDestroy()
        auth.signOut()
    }
}
