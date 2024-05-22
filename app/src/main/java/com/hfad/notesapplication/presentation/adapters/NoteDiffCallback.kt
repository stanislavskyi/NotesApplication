package com.hfad.notesapplication.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.hfad.domain.models.Note

class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
}