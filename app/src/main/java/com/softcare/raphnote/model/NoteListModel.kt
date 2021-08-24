package com.softcare.raphnote.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.softcare.raphnote.db.Repos
import com.softcare.raphnote.db.Schema
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.InputStream

class NoteListModel(private val repo: Repos) : ViewModel() {
    private val _noteList = MutableStateFlow<List<Note>>(arrayListOf())
    val noteList: StateFlow<List<Note>> = _noteList
    private val _note = MutableStateFlow<Note>(Note(0,0,""))
    val note: StateFlow<Note> = _note
    fun getNoteList(ascending: Boolean, orderColumn: String) = viewModelScope.launch {
        if (ascending)
            _noteList.value = repo.getAllNotesASC(orderColumn)
        else
            _noteList.value = repo.getAllNotesDESC(orderColumn)
    }

    fun searchNotes(ascending: Boolean, orderColumn: String, query: String) =
        viewModelScope.launch {
            if (ascending)
                _noteList.value = repo.getNotesSearchASC(query, orderColumn)
            else
                _noteList.value = repo.getNotesSearchDESC(query, orderColumn)
        }

    sealed class NoteUiState {
        object Empty : NoteUiState   ()
        data class NoteOpened( var note:Note) : NoteUiState   ()
        object Opening : NoteUiState ()
        data class FileOpened(val text:String) : NoteUiState ()
        data class Error(val message: String) : NoteUiState  ()
        object Deleting : NoteUiState ()
        object NoteDeleted : NoteUiState ()
        object Exporting : NoteUiState ()
        data class Exported(val path:String) : NoteUiState ()



    }
    }

class NoteListModelFactory(
    private val repo: Repos
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteListModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteListModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
