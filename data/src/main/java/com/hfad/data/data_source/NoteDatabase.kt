package com.hfad.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hfad.domain.models.Note

@Database(
    entities = [Note::class],
    version = 3
)

abstract class NoteDatabase : RoomDatabase(){

    abstract val noteDao: NoteDao

    companion object{
        const val DATABASE_NAME = "notes_db"
    }
}