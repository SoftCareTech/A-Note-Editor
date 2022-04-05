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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.*
import java.net.URI
import java.nio.charset.Charset
import java.util.concurrent.Executor

class NoteViewModel(private val resp: Repos) : ViewModel() {
    private val _noteUiState = MutableStateFlow<NoteUiState>(NoteUiState.Empty)
    val noteUiState: StateFlow<NoteUiState> = _noteUiState
    private val _note = MutableStateFlow<Note>(Note(0, 0, ""))
    val note: StateFlow<Note> = _note

    fun openNote(id: Long) = viewModelScope.launch {
        _noteUiState.value = NoteUiState.Opening
        _noteUiState.value = NoteUiState.NoteOpened(resp.getNoteById(id))
    }

  var stop=false
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



    fun openFile(inputStream: InputStream, mExecutor: Executor) {
        mExecutor.execute( Runnable {
            stop=false
            if(inputStream!=null){
                var s= System.currentTimeMillis()
                val st= System.currentTimeMillis()
                var counter =0;
                try {
                    var text = StringBuilder()
                    //val br =inputStream.bufferedReader()

                    _noteUiState.value = NoteViewModel.NoteUiState.Opening
                    var cn = inputStream.read()

                    while (cn != -1&&!stop&&counter<2) {
                        val c= Char(cn)
                        text.append(Char(cn))
                        cn = inputStream.read()
                        Log.d(Schema().tag,  " long on int $cn  charater $c   time spent"+(   System.currentTimeMillis()-st))
                        if(System.currentTimeMillis()-s>5000) {  // updating at 10 seconds
                            s=System.currentTimeMillis()
                            _noteUiState.value = NoteViewModel.NoteUiState.FileOpenUpdate(text.toString())
                            text = StringBuilder()
                            Log.d(Schema().tag,  " long on int $cn  charater $c   time spent" )
                        stop=true
                            counter++
                        }
                    }
                    inputStream.close()

                    if(stop) {
                        _noteUiState.value = NoteViewModel.NoteUiState.Stop(text.toString())

                    }else if(counter==2) {
                        _noteUiState.value = NoteViewModel.NoteUiState.FileTooLarge

                    }
                        _noteUiState.value = NoteViewModel.NoteUiState.FileOpened(text.toString())
                } catch (e: Exception) {
                    _noteUiState.value = NoteViewModel.NoteUiState.Error(e.localizedMessage)
                    e.printStackTrace()
                }
            }

        });


    }
    //private var _pages = MutableStateFlow<ArrayList<String>>(ArrayList())
   // val pages: StateFlow<ArrayList<String>> = _pages
    fun openFile(   inputStream: InputStream)  = CoroutineScope(Dispatchers.IO).launch {
        stop=false
        if(inputStream!=null){
            var s= System.currentTimeMillis()
            val st= System.currentTimeMillis()
            var counter =0
            var counterChar =0
            try {
                var text = StringBuilder()
                //val br =inputStream.bufferedReader()

                _noteUiState.value = NoteViewModel.NoteUiState.Opening
                var cn = inputStream.read()

                while (cn != -1&&!stop&&counter<2) {

                    val c= Char(cn)
                    text.append(Char(cn))
                    cn = inputStream.read()
                    Log.d(Schema().tag,  " long on int $cn  charater $c   time spent"+(   System.currentTimeMillis()-st))
                    if(System.currentTimeMillis()-s>7000) {  // updating at 10 seconds
                        s=System.currentTimeMillis()
                        _noteUiState.value = NoteViewModel.NoteUiState.FileOpenUpdate(text.toString())
                        text = StringBuilder()
                        Log.d(Schema().tag,  " long on int $cn  charater $c   time spent" )
                        stop=true
                        counter++
                    }
                    if(counterChar>40000) counter=2
                }


                inputStream.close()

                if(stop) {
                    _noteUiState.value = NoteViewModel.NoteUiState.Stop(text.toString())

                }else if(counter==2) {
                    _noteUiState.value = NoteViewModel.NoteUiState.FileTooLarge

                }
                _noteUiState.value = NoteViewModel.NoteUiState.FileOpened(text.toString())
            } catch (e: Exception) {
                _noteUiState.value = NoteViewModel.NoteUiState.Error(e.localizedMessage)
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
        data class FileOpenUpdate(val text: String) : NoteUiState()
        object FileTooLarge: NoteUiState()
        data class Stop(val text: String) : NoteUiState()
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
