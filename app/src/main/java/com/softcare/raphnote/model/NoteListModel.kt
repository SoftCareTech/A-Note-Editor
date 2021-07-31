package com.softcare.raphnote.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteListModel : ViewModel() {
    private val _noteListUiState = MutableStateFlow<NoteListUiState>(NoteListUiState.Empty)
    val noteListUiState: StateFlow<NoteListUiState> = _noteListUiState
    fun start(id: Long?) {
        if (id != null) {
            openNote(id)
        }

    }


    private fun openNote(id: Long) = viewModelScope.launch {
        val noteText = "opened note"
        val titleText = "opened note"
        val list = arrayListOf<com.softcare.raphnote.model.Note>()
        _noteListUiState.value = NoteListUiState.OpenDatabase(list)
    }
    private fun openFolder(id: Long) = viewModelScope.launch {
        val noteText = "opened note"
        val titleText = "opened note"
        val list = arrayListOf<com.softcare.raphnote.model.Note>()
        _noteListUiState.value = NoteListUiState.OpenDatabase(list)
    }

    fun getNoteList() = viewModelScope.launch {
        var list: List<Note> =   listOf(Note(0, 88, "text1"),Note(0, 88, "text1"),
        Note(0, 45, "text1"),)
        _noteListUiState.value= NoteListUiState.OpenDatabase(list)


    }


    sealed class NoteListUiState {
        object Empty : NoteListUiState   ()
        data class OpenDatabase( val noteList: List<Note>) : NoteListUiState   ()
        data class OpenFolder( val noteList: List<Note>) : NoteListUiState   ()
        data class Error(val message: String) : NoteListUiState  ()



}}