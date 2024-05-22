package com.hfad.notesapplication.presentation.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.notesapplication.R

class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val title: TextView = view.findViewById(R.id.textViewTitle)
    val description: TextView = view.findViewById(R.id.textViewDescription)
}