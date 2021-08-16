package com.softcare.raphnote.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.softcare.raphnote.db.Repos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class NoteListModel(private val repo: Repos) : ViewModel() {
    private val _noteList = MutableStateFlow<List<Note>>(arrayListOf())
    val noteList: StateFlow<List<Note>> = _noteList
    private val _note = MutableStateFlow <NoteUiState>(NoteUiState.Empty)
    val note: StateFlow<NoteUiState> = _note
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
                _noteList.value = repo.getNotesSearchASC(query, orderColumn)
        }

    fun  openFile(path: String) = viewModelScope.launch {
        _note.value=NoteUiState.Opening
        try {

            val file = File(path);
            val id = file.lastModified();
            val time = file.lastModified()
            val text = file.readText(charset = Charsets.UTF_32)
            _note.value=NoteUiState.Opened(Note(id,time,text))
        } catch (e: Exception) {
            _note.value=NoteUiState.Error(e.localizedMessage)
        }

    }
    sealed class NoteUiState {
        object Empty : NoteUiState()
        object Opening : NoteUiState()
        data class Opened(val note:Note) : NoteUiState()
        data class Error(val message: String) : NoteUiState()
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
