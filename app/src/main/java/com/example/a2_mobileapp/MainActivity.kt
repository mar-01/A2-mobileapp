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

        firebaseRef = FirebaseDatabase.getInstance("https://a2-mobileapp-default-rtdb.europe-west1.firebasedatabase.app").getReference("test")
        binding.button.setOnClickListener {
            firebaseRef.setValue("test").addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Daten erfolgreich gespeichert", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Fehler: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
