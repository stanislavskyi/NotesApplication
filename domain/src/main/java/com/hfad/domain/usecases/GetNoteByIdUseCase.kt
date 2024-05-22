package com.hfad.domain.usecases

import com.hfad.domain.models.Note
import com.hfad.domain.repository.NoteRepository

class GetNoteByIdUseCase(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note {
        return noteRepository.getNoteById(id)
    }
}