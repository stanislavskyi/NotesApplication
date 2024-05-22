package com.hfad.domain.usecases

import com.hfad.domain.models.Note
import com.hfad.domain.repository.NoteRepository

class AddNoteUseCase(
    private val noteRepository: NoteRepository
) {

    suspend operator fun invoke(note: Note){
        noteRepository.addNote(note)
    }
}