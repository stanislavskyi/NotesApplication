package com.hfad.notesapplication.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.hfad.domain.models.Note
import com.hfad.notesapplication.R
import kotlin.random.Random

class NoteAdapter(private val context: Context) : ListAdapter<Note, NoteViewHolder>(NoteDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    var onNoteClickListener: ((Note) -> Unit)? = null

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.title.text = note.title
        holder.description.text = note.description

        val randomIntColor = Random.nextInt(1, 5)
        val color = when (randomIntColor){
            1 -> holder.itemView.context.getColor(android.R.color.holo_purple)
            2 -> holder.itemView.context.getColor(android.R.color.holo_red_light)
            3 -> holder.itemView.context.getColor(android.R.color.holo_orange_light)
            4 -> holder.itemView.context.getColor(android.R.color.holo_blue_light)
            else -> holder.itemView.context.getColor(android.R.color.white)
        }

        holder.itemView.setBackgroundColor(color)

        holder.itemView.setOnClickListener {
            onNoteClickListener?.invoke(note)
        }
    }
}