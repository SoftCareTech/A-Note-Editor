package com.softcare.raphnote.db

import android.util.Log
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
    suspend fun getAllNotes(isAsc:Boolean ,orderByID:Boolean ):  List<Note> {
        Log.d(Schema().tag,"is Asc $isAsc  on  Column $orderByID")
        if(orderByID) {
            if(isAsc)
            return noteDao.getAllNotesID_A();
              else return noteDao.getAllNotesID_D();
        }  else {
            if(isAsc)
                return noteDao.getAllNotesTIME_A();
            else return noteDao.getAllNotesTIME_D();
        }

    }
    @WorkerThread
    suspend fun getNotesSearch(isAsc:Boolean ,orderByID:Boolean , textContain: String):  List<Note> {
        Log.d(Schema().tag,"is Asc $isAsc  on  Column $orderByID")
        if(orderByID) {
            if(isAsc)
                return noteDao.getNotesSearchID_A("%" + textContain + "%");
            else return noteDao.getNotesSearchID_D("%" + textContain + "%");
        }  else {
            if(isAsc)
                return noteDao.getNotesSearchTIME_A("%" + textContain + "%");
            else return noteDao.getNotesSearchTIME_D("%" + textContain + "%");
        }

    }

}