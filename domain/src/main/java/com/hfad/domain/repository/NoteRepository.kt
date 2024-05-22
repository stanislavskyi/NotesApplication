package com.hfad.domain.repository

import com.hfad.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note
    suspend fun addNote(note: Note)
    suspend fun deleteNote(note: Note)
}