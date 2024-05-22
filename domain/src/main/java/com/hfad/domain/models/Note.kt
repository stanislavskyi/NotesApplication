package com.hfad.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = UNDEFINED_ID,
    val title: String,
    val description: String

){
    companion object{
        const val UNDEFINED_ID = -1
    }
}