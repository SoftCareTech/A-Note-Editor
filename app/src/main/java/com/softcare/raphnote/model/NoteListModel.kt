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
    fun getNotes(ascending: Boolean, orderById: Boolean) = viewModelScope.launch {
            _noteList.value = repo.getAllNotes(ascending,orderById)
    }

    fun searchNotes(ascending: Boolean, columnId: Boolean, query: String) =
        viewModelScope.launch {
                _noteList.value = repo.getNotesSearch(ascending,columnId,query)
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
