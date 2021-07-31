package com.softcare.raphnote.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteModel : ViewModel() {
    private val _noteUiState = MutableStateFlow<NoteUiState>(NoteUiState.Empty)
    val noteUiState: StateFlow<NoteUiState> = _noteUiState
    private val _note = MutableStateFlow<Note>(Note(0,"",""))
    val note: StateFlow<Note> = _note
    fun save(id: Long ,time: Long,text: String) = viewModelScope.launch {
        _noteUiState.value = NoteUiState.Saving
        if (text.isNotEmpty() ) {
            _noteUiState.value = NoteUiState.Saved(id,time,text)
        }else{
            _noteUiState.value= NoteUiState.Error(" empty")
        }
    }

    fun start(id: Long?) {
        if (id != null) {
            openNote(id)
        }

    }

    fun start( ) {
        _noteUiState.value = NoteUiState.Empty

    }

    private fun openNote(id: Long) = viewModelScope.launch {
        val noteText = "opened note"
        val time =  67L
        _noteUiState.value = NoteUiState.Open(id,time,noteText)
    }

    data class Note(var id: Long, var time: String, var note: String) {
    }
    sealed class NoteUiState {
        object Empty : NoteUiState   ()
        data class Open( val id: Long ,val time: Long,val text: String) : NoteUiState   ()
        object Unsaved : NoteUiState  ()
        object Saving : NoteUiState ()
        data class Saved ( val id: Long ,val time: Long,val text: String) : NoteUiState   ()
        data class Error(val message: String) : NoteUiState  ()



}}