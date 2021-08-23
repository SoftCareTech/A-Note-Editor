package com.softcare.raphnote.model

import android.graphics.Color
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.softcare.raphnote.db.Repos
import com.softcare.raphnote.db.NoteDao
import com.softcare.raphnote.db.Schema
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.*
import java.net.URI

class NoteViewModel(private val resp: Repos) : ViewModel() {
    private val _noteUiState = MutableStateFlow<NoteUiState>(NoteUiState.Empty)
    val noteUiState: StateFlow<NoteUiState> = _noteUiState
    private val _note = MutableStateFlow<Note>(Note(0, 0, ""))
    val note: StateFlow<Note> = _note

    fun openNote(id: Long) = viewModelScope.launch {
        _noteUiState.value = NoteUiState.Opening
        _noteUiState.value = NoteUiState.NoteOpened(resp.getNoteById(id))
    }

    fun openFile(path: String) = viewModelScope.launch {
        _noteUiState.value =  NoteUiState.Opening
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
    fun openFile(   inputStream: InputStream) = viewModelScope.launch {

        if(inputStream!=null){
            try {
                var text = StringBuilder()
                val br =inputStream.bufferedReader()
                var line: String?
                line = br.readLine()
                while (line != null) {
                    Log.d(Schema().tag, line)
                    text.append(line)
                    text.append("\n")
                    line = br.readLine()

                }
                inputStream.close()
                _noteUiState.value = NoteUiState.FileOpened(text.toString())
            } catch (e: Exception) {
                _noteUiState.value = NoteUiState.Error(e.localizedMessage)
                e.printStackTrace()
            }
        }


    }

    fun deleteNote(id:Long) = viewModelScope.launch {
        _noteUiState.value = NoteUiState.Deleting
        try {
            resp.deleteNote(Note(id,0L,""))
            _noteUiState.value = NoteUiState.NoteDeleted
        } catch (e: Exception) {
            _noteUiState.value = NoteUiState.Error(e.localizedMessage)
        }

    }

    fun exportToFile(uri: Uri, text: String) = viewModelScope.launch {
        try {
            _noteUiState.value = NoteUiState.Exporting
            val file = File(uri.path)
            file.createNewFile()
            file.writeText(text, Charsets.UTF_32)
            _noteUiState.value = NoteUiState.Exported(file.absolutePath)
        } catch (e: Exception) {
            _noteUiState.value = NoteUiState.Error(e.localizedMessage)
        }


    }
    fun exportText(out: OutputStream, text: String) = viewModelScope.launch {

            _noteUiState.value = NoteUiState.Exporting
            if(out!=null){
                try {
                    val bw =out.bufferedWriter()
                     bw.write(text)
                    bw.flush()
                    out.flush()
                    bw.close()
                     out.close()
                    _noteUiState.value = NoteUiState.Exported(" ")
                } catch (e: Exception) {
                    _noteUiState.value = NoteUiState.Error(e.localizedMessage)
                    e.printStackTrace()
                }
            }

    }


    sealed class NoteUiState {
        object Empty : NoteUiState()
        data class NoteOpened(var note: Note) : NoteUiState()
        object Opening : NoteUiState()
        data class FileOpened(val text: String) : NoteUiState()
        data class Error(val message: String) : NoteUiState()
        object Deleting : NoteUiState()
        object NoteDeleted : NoteUiState()
        object Exporting : NoteUiState()
        data class Exported(val path: String) : NoteUiState()
    }



}


class NoteViewModelFactory(
    private val resp: Repos
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(resp) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
