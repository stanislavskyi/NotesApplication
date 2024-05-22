package com.hfad.notesapplication.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hfad.domain.models.Note
import com.hfad.notesapplication.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteActivity : AppCompatActivity(), NoteFragment.OnEditingFinishedListener {

    private var screenMode = MODE_UNKNOWN
    private var noteId = Note.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        parseIntent()

        if(savedInstanceState == null) {
            launchRightMode()
        }


    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_EDIT -> NoteFragment.newInstanceEditNote(noteId)
            MODE_ADD -> NoteFragment.newInstanceAddNote()
            else -> throw RuntimeException("Unknown screen mode $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.note_container, fragment)
            .commit()
    }


    private fun parseIntent() {
        if(!intent.hasExtra(SCREEN_MODE)){
            throw RuntimeException("Param screen mode is absent")
        }

        val mode = intent.getStringExtra(SCREEN_MODE)
        if(mode != MODE_EDIT && mode != MODE_ADD){
            throw RuntimeException("Unknown screen mode $mode")
        }

        screenMode = mode
        if (screenMode == MODE_EDIT){
            if (!intent.hasExtra(NOTE_ID)){
                throw RuntimeException("Param note id is absent")
            }
            noteId = intent.getIntExtra(NOTE_ID, Note.UNDEFINED_ID)
        }
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val NOTE_ID = "note_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddNote(context: Context): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditNote(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(SCREEN_MODE, MODE_EDIT)
            intent.putExtra(NOTE_ID, shopItemId)
            return intent
        }
    }

    override fun onEditingFinished() {
        finish()
    }
}