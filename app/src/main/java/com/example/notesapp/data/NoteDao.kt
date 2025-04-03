package com.example.notesapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface NoteDao {
    // lay data
    @Query("SELECT * FROM note_table ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>
    // add note
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)
    // loc theo catergoly
    @Query("SELECT * FROM note_table WHERE category = :category ORDER BY timestamp DESC")
    fun getNotesByCategory(category: String): Flow<List<Note>>
    // delete note
    @Delete
    suspend fun delete(note: Note)
    // edit note
    @Update
    suspend fun update(note: Note)

}