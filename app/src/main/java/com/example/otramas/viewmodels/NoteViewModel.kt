package com.example.otramas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.otramas.data.Note
import com.example.otramas.data.NoteDatabase
import com.example.otramas.data.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: NoteRepository
    val allNotes: LiveData<List<Note>>
    
    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.allNotes
    }
    
    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }
    
    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }
    
    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }
    
    suspend fun getNoteById(noteId: Int): Note? {
        return repository.getNoteById(noteId)
    }
}