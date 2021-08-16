package com.softcare.raphnote.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.softcare.raphnote.db.Repos
import com.softcare.raphnote.db.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class NoteModel (private val resp: Repos) : ViewModel() {
    private val _noteUiState = MutableStateFlow<NoteUiState>(NoteUiState.Empty)
    val noteUiState: StateFlow<NoteUiState> = _noteUiState
    private val _note = MutableStateFlow<Note>(Note(0,0,""))
    val note: StateFlow<Note> = _note
    fun save(note:Note) = viewModelScope.launch {
        _noteUiState.value = NoteUiState.Saving
        if (note.text.isNotEmpty() ) {
            resp.createOrUpdate(note)
            _noteUiState.value = NoteUiState.Saved
        }else{
            _noteUiState.value= NoteUiState.Error(" empty")
        }
    }



      fun openNote(id: Long) = viewModelScope.launch {
        _noteUiState.value =  NoteUiState.NoteOpened(resp.getNoteById(id))
    }
    fun openFile(path: String) = viewModelScope.launch {
        val file = File(path)
        try {
            var text= StringBuilder()
            val br= BufferedReader(FileReader(file))
            var line:String?
            line=br.readLine()
            while (line!=null){
                text.append(line)
                text.append("\n")
                line=br.readLine()
            }
            _noteUiState.value =  NoteUiState.FileOpened(text.toString())
        }catch (e:Exception){
            _noteUiState.value =  NoteUiState.Error(e.localizedMessage)
        }

    }


    sealed class NoteUiState {
        object Empty : NoteUiState   ()
        data class NoteOpened( var note:Note) : NoteUiState   ()
        object Saving : NoteUiState ()
        object Opening : NoteUiState ()
        data class FileOpened(val text:String) : NoteUiState ()
        object Saved  : NoteUiState   ()
        data class Error(val message: String) : NoteUiState  ()



}}


class NoteModelFactory(
    private val resp: Repos
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteModel::class.java) ) {
            @Suppress("UNCHECKED_CAST")
            return NoteModel(resp) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
