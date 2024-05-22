package com.hfad.notesapplication.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.hfad.domain.models.Note

class NoteListDiffCallback(
    private val oldList: List<Note>,
    private val newList: List<Note>
) : DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newList = newList[newItemPosition]
        return oldItem.id == newList.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newList = newList[newItemPosition]
        return oldItem == newList
    }

}