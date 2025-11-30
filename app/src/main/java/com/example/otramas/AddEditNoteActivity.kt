package com.example.otramas

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.otramas.data.Note
import com.example.otramas.viewmodel.NoteViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AddEditNoteActivity : AppCompatActivity() {
    
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var etTitle: TextInputEditText
    private lateinit var etContent: TextInputEditText
    private lateinit var btnSave: Button
    
    private var noteId: Int = -1
    private var isEditMode = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)
        
        // Inicializar ViewModel
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        
        // Inicializar vistas
        etTitle = findViewById(R.id.etNoteTitle)
        etContent = findViewById(R.id.etNoteContent)
        btnSave = findViewById(R.id.btnSaveNote)
        
        // Verificar si es modo edición
        noteId = intent.getIntExtra("NOTE_ID", -1)
        isEditMode = noteId != -1
        
        if (isEditMode) {
            title = "Editar Nota"
            loadNoteData()
        } else {
            title = "Nueva Nota"
        }
        
        // Configurar botón guardar
        btnSave.setOnClickListener {
            saveNote()
        }
    }
    
    private fun loadNoteData() {
        lifecycleScope.launch {
            val note = noteViewModel.getNoteById(noteId)
            note?.let {
                etTitle.setText(it.title)
                etContent.setText(it.content)
            }
        }
    }
    
    private fun saveNote() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()
        
        if (title.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa un título", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (content.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa contenido", Toast.LENGTH_SHORT).show()
            return
        }
        
        val note = Note(
            id = if (isEditMode) noteId else 0,
            title = title,
            content = content,
            timestamp = System.currentTimeMillis()
        )
        
        if (isEditMode) {
            noteViewModel.update(note)
            Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show()
        } else {
            noteViewModel.insert(note)
            Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show()
        }
        
        finish()
    }
}