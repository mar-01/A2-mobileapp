package com.example.a2_mobileapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.a2_mobileapp.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : ComponentActivity() {

    lateinit var binding : ActivityMainBinding
    private lateinit var  firebaseRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseRef = FirebaseDatabase.getInstance("https://a2-mobileapp-default-rtdb.europe-west1.firebasedatabase.app").getReference("Kontakte")
        binding.btnSenddata.setOnClickListener {
            firebaseRef.setValue("test").addOnCompleteListener { task ->
                senddata()
            }
        }
    }

    private fun senddata(){
        val name = binding.textName.text.toString()
        val nummer = binding.textNummer.text.toString()

        if (name.isEmpty()) binding.textName.error = "Name einfügen"
        if (nummer.isEmpty()) binding.textNummer.error = "Nummer einfügen"

        val kontaktId = firebaseRef.push().key!!
        val kontakt = Kontakte(kontaktId, name, nummer)

        firebaseRef.child(kontaktId).setValue(kontakt)
            .addOnCompleteListener {
                Toast.makeText(this, "Daten hochgeladen", Toast.LENGTH_SHORT).show()
            }

    }
}
