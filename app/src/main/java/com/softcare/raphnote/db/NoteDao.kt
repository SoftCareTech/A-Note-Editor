package com.softcare.raphnote.db

import androidx.room.*
import com.softcare.raphnote.model.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {
    @Query("SELECT * FROM  notes  WHERE id = :id")
     suspend fun getNoteById(id:Long ): Note

    @Query("SELECT * FROM  notes  ORDER BY :orderColumn ASC")
    suspend  fun getAllNotesASC(orderColumn:String ):  List<Note>   /// Flow<List<Note>>
    @Query("SELECT * FROM  notes  ORDER BY :orderColumn DESC")
    suspend  fun getAllNotesDESC(orderColumn:String ): List<Note>   /// Flow<List<Note>>


    @Query("SELECT * FROM  notes  WHERE text LIKE  :textContain   ORDER BY :orderColumn ASC")
    suspend   fun getNotesSearchASC(textContain:String,orderColumn:String):  List<Note>   /// Flow<List<Note>>
    @Query("SELECT * FROM  notes  WHERE text LIKE  :textContain   ORDER BY :orderColumn DESC")
    suspend   fun getNotesSearchDESC(textContain:String,orderColumn:String):  List<Note>   /// Flow<List<Note>>
/*
if return type is to monitor  using flow
then all query with flow type will not be suspend functions
 it automatically use non ui thread
 */



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createOrUpdate (note:Note)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertNoteMany(note:List<Note>)
    @Delete
    suspend fun deleteNote(note: Note)


}