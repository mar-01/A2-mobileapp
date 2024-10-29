package com.example.a2_mobileapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class TeilnehmerListeActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var teilnehmerRecyclerView: RecyclerView
    private lateinit var adapter: TeilnehmerAdapter
    private var listenerRegistration: ListenerRegistration? = null
    private val teilnehmerList = mutableListOf<Teilnehmer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teilnehmer_liste)

        val backToEntryButton = findViewById<Button>(R.id.backToEntryButton)
        backToEntryButton.setOnClickListener {
            val intent = Intent(this, TeilnehmerRegistrierungActivity::class.java)
            startActivity(intent)
            finish() // Schließt die aktuelle Activity
        }

        db = FirebaseFirestore.getInstance()
        teilnehmerRecyclerView = findViewById(R.id.teilnehmerRecyclerView)
        adapter = TeilnehmerAdapter(teilnehmerList)

        // RecyclerView-Setup
        teilnehmerRecyclerView.layoutManager = LinearLayoutManager(this)
        teilnehmerRecyclerView.adapter = adapter

        // Echtzeit-Listener starten
        listenToTeilnehmerUpdates()
    }

    private fun listenToTeilnehmerUpdates() {
        listenerRegistration = db.collection("teilnehmer")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("TeilnehmerListeActivity", "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (docChange in snapshots!!.documentChanges) {
                    val documentId = docChange.document.id
                    val teilnehmer = docChange.document.toObject(Teilnehmer::class.java).apply {
                        id = documentId
                    }

                    when (docChange.type) {
                        DocumentChange.Type.ADDED -> {
                            Log.d("TeilnehmerListeActivity", "Eintrag hinzugefügt: ${teilnehmer.id}")
                            teilnehmerList.add(teilnehmer)
                            adapter.notifyItemInserted(teilnehmerList.size - 1)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            Log.d("TeilnehmerListeActivity", "Eintrag geändert: ${teilnehmer.id}")
                            val index = teilnehmerList.indexOfFirst { it.id == documentId }
                            if (index != -1) {
                                teilnehmerList[index] = teilnehmer
                                adapter.notifyItemChanged(index)
                            }
                        }
                        DocumentChange.Type.REMOVED -> {
                            Log.d("TeilnehmerListeActivity", "Eintrag entfernt: ${teilnehmer.id}")
                            val index = teilnehmerList.indexOfFirst { it.id == documentId }
                            if (index != -1) {
                                teilnehmerList.removeAt(index)
                                adapter.notifyItemRemoved(index)
                            }
                        }
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        listenerRegistration?.remove()
    }
}
