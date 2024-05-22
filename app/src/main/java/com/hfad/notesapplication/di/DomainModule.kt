package com.hfad.notesapplication.di

import com.hfad.domain.repository.NoteRepository
import com.hfad.domain.usecases.AddNoteUseCase
import com.hfad.domain.usecases.DeleteNoteUseCase
import com.hfad.domain.usecases.GetNoteByIdUseCase
import com.hfad.domain.usecases.GetNotesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetNotesUseCase(noteRepository: NoteRepository): GetNotesUseCase {
        return GetNotesUseCase(noteRepository = noteRepository)
    }

    @Provides
    fun provideAddNoteUseCase(noteRepository: NoteRepository): AddNoteUseCase {
        return AddNoteUseCase(noteRepository = noteRepository)
    }

    @Provides
    fun provideGetNoteByIdUseCase(noteRepository: NoteRepository): GetNoteByIdUseCase {
        return GetNoteByIdUseCase(noteRepository = noteRepository)
    }

    @Provides
    fun provideDeleteNoteUseCase(noteRepository: NoteRepository): DeleteNoteUseCase {
        return DeleteNoteUseCase(noteRepository = noteRepository)
    }


}