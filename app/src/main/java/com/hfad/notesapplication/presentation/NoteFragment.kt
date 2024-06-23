package com.hfad.notesapplication.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.hfad.domain.models.Note
import com.hfad.notesapplication.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var screenMode: String = MODE_UNKNOWN
    private var noteId: Int = Note.UNDEFINED_ID

    private lateinit var tilTitle: TextInputLayout
    private lateinit var tilDescription: TextInputLayout
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var buttonSave: FloatingActionButton

    private lateinit var floatingActionButtonAddMathModel: FloatingActionButton

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener){
            onEditingFinishedListener = context
        }else{
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("NoteFragment", "onCreate")
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_note, container, false)
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initViews(view)

        addTextChangeListeners()
        launchRightMode()
        observeViewModel()

        floatingActionButtonAddMathModel.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data ?: throw IllegalStateException("Image URI is null")
            viewModel.mathModelLiveData.value = selectedImageUri

            Log.d("MATH MODEL 1", selectedImageUri.toString() )
            // Теперь у вас есть URI выбранного изображения, который вы можете использовать дальше.
            // Например, вы можете загрузить его с помощью библиотеки Picasso или Glide.
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getNoteById(noteId)
        viewModel.note.observe(viewLifecycleOwner) {
            etTitle.setText(it.title)
            etDescription.setText(it.description)
        }
        buttonSave.setOnClickListener {
            viewModel.editNote(etTitle.text?.toString(), etDescription.text?.toString())
        }


    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addNote(etTitle.text?.toString(), etDescription.text?.toString())
        }
    }


    private fun observeViewModel() {
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_description)
            } else {
                null
            }
            tilDescription.error = message
        }

        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_title)
            } else {
                null
            }
            tilTitle.error = message
        }

        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            //activity?.onBackPressed()
            //(activity as MainActivity).onEditingFinished()
            onEditingFinishedListener?.onEditingFinished()

        }
    }

    private fun addTextChangeListeners() {
        etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                viewModel.resetErrorInputTitle()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputDescription()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun initViews(view: View) {
        tilTitle = view.findViewById(R.id.til_title)
        tilDescription = view.findViewById(R.id.til_description)
        etTitle = view.findViewById(R.id.et_title)
        etDescription = view.findViewById(R.id.et_description)
        buttonSave = view.findViewById(R.id.save_button)
        floatingActionButtonAddMathModel = view.findViewById(R.id.floatingActionButtonAddMathModel)
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)){
            throw RuntimeException("Args screen mode is absent")
        }

        val mode = args.getString(SCREEN_MODE)
        if(mode != MODE_EDIT && mode != MODE_ADD){
            throw RuntimeException("Unknown screen mode $mode")
        }

        screenMode = mode
        if (screenMode == MODE_EDIT){
            if(!args.containsKey(NOTE_ID)){
                throw RuntimeException("Args note id is absent")
            }
            noteId = args.getInt(NOTE_ID, Note.UNDEFINED_ID)
        }
    }


    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val NOTE_ID = "note_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""
        private const val REQUEST_IMAGE_PICK = 1

        fun newInstanceAddNote(): NoteFragment {
            return NoteFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditNote(noteId: Int): NoteFragment {
            return NoteFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(NOTE_ID, noteId)
                }
            }
        }
    }
}