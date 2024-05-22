package com.hfad.notesapplication.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hfad.notesapplication.R
import com.hfad.notesapplication.presentation.adapters.NoteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NoteFragment.OnEditingFinishedListener {

    private lateinit var buttonAddNote: FloatingActionButton
    private lateinit var recyclerViewNotes: RecyclerView
    private lateinit var mainViewModel: MainViewModel
    private lateinit var noteAdapter: NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerViewNotes()
        mainViewModel = ViewModelProvider(this)[MainViewModel::class]

        lifecycleScope.launch {
            mainViewModel.notes.collect{
                noteAdapter.submitList(it)
            }
        }


        buttonAddNote = findViewById(R.id.buttonAddNote)
        buttonAddNote.setOnClickListener {
            val intent = NoteActivity.newIntentAddNote(this@MainActivity)
            startActivity(intent)
        }

    }

    private fun setupRecyclerViewNotes(){
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes)
        recyclerViewNotes.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        noteAdapter = NoteAdapter(this@MainActivity)
        recyclerViewNotes.adapter = noteAdapter
        setupSwipeListener(recyclerViewNotes)
        setupClickListener()
    }


    private fun setupSwipeListener(recyclerViewNote: RecyclerView) {
        val callback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //val item = noteAdapter.notes[viewHolder.adapterPosition]
                val note = noteAdapter.currentList[viewHolder.adapterPosition]
                mainViewModel.deleteNote(note)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerViewNote)
    }

    private fun setupClickListener() {
        noteAdapter.onNoteClickListener = {
            val intent = NoteActivity.newIntentEditNote(this@MainActivity, it.id)
            startActivity(intent)
            //Toast.makeText(this@MainActivity, "${it.id}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onEditingFinished(){
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }
}