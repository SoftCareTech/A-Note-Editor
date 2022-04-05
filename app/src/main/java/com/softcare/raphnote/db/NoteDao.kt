package com.softcare.raphnote.db

import androidx.room.*
import com.softcare.raphnote.model.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY  id   ASC "  )
    suspend  fun getAllNotesID_A( ): List<Note>   /// Flow<List<Note>>
    @Query("SELECT * FROM notes ORDER BY  id  DESC "  )
    suspend  fun getAllNotesID_D( ): List<Note>   /// Flow<List<Note>>
    @Query("SELECT * FROM  notes  WHERE text LIKE  :textContain   ORDER BY id ASC "  )
    suspend   fun getNotesSearchID_A(textContain:String ):  List<Note>   /// Flow<List<Note>>
    @Query("SELECT * FROM  notes  WHERE text LIKE  :textContain   ORDER BY id DESC "  )
    suspend   fun getNotesSearchID_D(textContain:String ):  List<Note>   /// Flow<List<Note>>

    @Query("SELECT * FROM notes ORDER BY  time   ASC "  )
    suspend  fun getAllNotesTIME_A( ): List<Note>   /// Flow<List<Note>>
    @Query("SELECT * FROM notes ORDER BY  time  DESC "  )
    suspend  fun getAllNotesTIME_D( ): List<Note>   /// Flow<List<Note>>
    @Query("SELECT * FROM  notes  WHERE text LIKE  :textContain   ORDER BY time ASC "  )
    suspend   fun getNotesSearchTIME_A(textContain:String):  List<Note>   /// Flow<List<Note>>
    @Query("SELECT * FROM  notes  WHERE text LIKE  :textContain   ORDER BY time DESC "  )
    suspend   fun getNotesSearchTIME_D(textContain:String ):  List<Note>   /// Flow<List<Note>>







    @Query("SELECT * FROM  notes  WHERE id = :id")
     suspend fun getNoteById(id:Long ): Note


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