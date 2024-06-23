package com.hfad.notesapplication.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.domain.models.Note
import com.hfad.domain.usecases.AddNoteUseCase
import com.hfad.domain.usecases.DeleteNoteUseCase
import com.hfad.domain.usecases.GetNoteByIdUseCase
import com.hfad.domain.usecases.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,

    private val addNoteUseCase: AddNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase
) : ViewModel(){

    val notes = getNotesUseCase() //.asLiveData()

    val mathModelLiveData = MutableLiveData<Uri>()

    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO){
            deleteNoteUseCase(note)
        }
    }

    private val _errorInputTitle = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputTitle

    private val _errorInputDescription = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputDescription

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note>
        get() = _note


    fun getNoteById(noteId: Int){
        viewModelScope.launch(Dispatchers.IO){
            val note = getNoteByIdUseCase(noteId)
            _note.postValue(note)
        }
    }

    fun addNote(inputName: String?, inputCount: String?) {
        val name = parseTitle(inputName)
        val count = parseDescription(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            val note = Note(0,name, count,
                mathModelUri = mathModelLiveData.value.toString())
            viewModelScope.launch(Dispatchers.IO){
                addNoteUseCase(note)
                withContext(Dispatchers.Main) {
                    finishWork()
                }
            }

        }
    }

    fun editNote(inputName: String?, inputCount: String?) {
        val name = parseTitle(inputName)
        val count = parseDescription(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            _note.value?.let {
                val note = it.copy(title = name, description = count)
                viewModelScope.launch(Dispatchers.IO){
                    addNoteUseCase(note)
                    withContext(Dispatchers.Main) {
                        finishWork()
                    }
                }
            }
        }
    }

    private fun parseTitle(inputTitle: String?): String {
        return inputTitle ?: ""
    }

    private fun parseDescription(inputDescription: String?): String {
        return inputDescription ?: ""
    }

    private fun validateInput(title: String, description: String): Boolean {
        var result = true
        if (title.isBlank()) {
            _errorInputTitle.value = true
            result = false
        }
        if (description.isBlank()) {
            _errorInputDescription.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputTitle() {
        _errorInputTitle.value = false
    }

    fun resetErrorInputDescription() {
        _errorInputDescription.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}