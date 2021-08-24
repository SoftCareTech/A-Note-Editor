package com.softcare.raphnote.db

import androidx.annotation.WorkerThread
import com.softcare.raphnote.model.Note
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class Repos(private val noteDao: NoteDao) {

    @WorkerThread
    suspend fun createOrUpdate(note: Note) {
        noteDao.createOrUpdate(note)
    }

    @WorkerThread
    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
    @WorkerThread
    suspend fun getNoteById(id: Long):  Note = noteDao.getNoteById(id)
    @WorkerThread
    suspend fun getAllNotesASC(orderColumn: String):  List<Note> =
        noteDao.getAllNotesASC(orderColumn)

    @WorkerThread
    suspend fun getAllNotesDESC(orderColumn: String):  List<Note> =
        noteDao.getAllNotesDESC(orderColumn)


    @WorkerThread
    suspend fun getNotesSearchASC(textContain: String, orderColumn: String):  List<Note> =
        noteDao.getNotesSearchASC("%" + textContain + "%", orderColumn)

    @WorkerThread
    suspend fun getNotesSearchDESC(textContain: String, orderColumn: String):  List<Note> =
        noteDao.getNotesSearchDESC("%" + textContain + "%", orderColumn)

}