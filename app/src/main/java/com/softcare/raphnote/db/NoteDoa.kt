package com.softcare.raphnote.db

import androidx.room.*
import com.softcare.raphnote.model.Note
import kotlinx.coroutines.flow.Flow
import java.nio.ByteOrder


@Dao
interface NoteDoa {
    @Query("SELECT * FROM  notes  WHERE id = :id")
    fun getNotesById(id:Long ):Flow <Note>
    @Query("SELECT * FROM  notes  ORDER BY :orderColumn ASC")
    fun getAllNotesASC(orderColumn:String ): Flow<List<Note>>
    @Query("SELECT * FROM  notes  ORDER BY :orderColumn DESC")
    fun getAllNotesDESC(orderColumn:String ): Flow<List<Note>>
    @Query("SELECT * FROM  notes  WHERE text LIKE '% :textContain  %' ORDER BY :orderColumn ASC")
    fun getNotesSearchASC(textContain:String,orderColumn:String): Flow<List<Note>>
    @Query("SELECT * FROM  notes  WHERE text LIKE '% :textContain  %' ORDER BY :orderColumn DESC")
    fun getNotesSearchDESC(textContain:String,orderColumn:String): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertNote(note:Note)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertNoteMany(note:List<Note>)
    @Update(  onConflict = OnConflictStrategy.ABORT)
    fun updateNote(note:Note)

}