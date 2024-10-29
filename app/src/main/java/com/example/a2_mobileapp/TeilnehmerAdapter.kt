package com.example.a2_mobileapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Teilnehmer(
    var id: String = "",
    val name: String = "",
    val email: String = "",
    val matrikelnummer: String = ""
)

class TeilnehmerAdapter(private val teilnehmerList: MutableList<Teilnehmer>) :
    RecyclerView.Adapter<TeilnehmerAdapter.TeilnehmerViewHolder>() {

    class TeilnehmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        val matrikelnummerTextView: TextView = itemView.findViewById(R.id.matrikelnummerTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeilnehmerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_teilnehmer, parent, false)
        return TeilnehmerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeilnehmerViewHolder, position: Int) {
        val teilnehmer = teilnehmerList[position]
        holder.nameTextView.text = teilnehmer.name
        holder.emailTextView.text = teilnehmer.email
        holder.matrikelnummerTextView.text = teilnehmer.matrikelnummer
    }

    override fun getItemCount(): Int {
        return teilnehmerList.size
    }

    fun addTeilnehmer(teilnehmer: Teilnehmer) {
        teilnehmerList.add(0, teilnehmer) // Neuer Eintrag wird an Position 0 hinzugefügt
        notifyItemInserted(0) // Benachrichtigen, dass an Position 0 ein neues Element eingefügt wurde
    }
}
