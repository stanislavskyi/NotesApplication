package com.hfad.notesapplication.presentation.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.hfad.domain.models.Note
import com.hfad.notesapplication.R
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
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

        val uri = Uri.parse(note.mathModelUri)
//        Picasso.get()
//            .load(uri)
//            .into(holder.mathModel);


        Glide.with(holder.itemView.context)
            .load(uri)
            .into(holder.mathModel)

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