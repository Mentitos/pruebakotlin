package com.example.otramas.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)
    
    @Update
    suspend fun update(note: Note)
    
    @Delete
    suspend fun delete(note: Note)
    
    @Query("SELECT * FROM notes_table ORDER BY timestamp DESC")
    fun getAllNotes(): LiveData<List<Note>>
    
    @Query("SELECT * FROM notes_table WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): Note?
}