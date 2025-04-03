package com.example.notesapp.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.notesapp.data.Note
import com.example.notesapp.data.NoteDatabase
import com.example.notesapp.data.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    // Repository (lấy từ database)
    private val repository: NoteRepository

    // LiveData cho danh sách ghi chú
    val allNotes: LiveData<List<Note>>

    init {
        val dao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes.asLiveData()
    }

    // Thêm ghi chú mới

    fun insert(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(note)
        }
    }

    // Xoá ghi chú
    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }
    // loc hien thi theo category
    fun getNotesByCategory(category: String): LiveData<List<Note>> {
        return if (category == "All") {
            repository.allNotes.asLiveData()
        } else {
            repository.getNotesByCategory(category).asLiveData()
        }
    }

}
