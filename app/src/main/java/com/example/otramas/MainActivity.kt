package com.example.otramas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otramas.adapter.NoteAdapter
import com.example.otramas.data.Note
import com.example.otramas.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddNote: FloatingActionButton
    private lateinit var tvEmptyState: TextView
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerView)
        fabAddNote = findViewById(R.id.fabAddNote)
        tvEmptyState = findViewById(R.id.tvEmptyState)

        // Configurar RecyclerView
        setupRecyclerView()

        // Inicializar ViewModel
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        // Observar cambios en las notas
        noteViewModel.allNotes.observe(this) { notes ->
            noteAdapter.setNotes(notes)
            updateEmptyState(notes.isEmpty())
        }

        // Configurar FAB
        fabAddNote.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(
            onNoteClick = { note ->
                // Abrir nota para editar
                val intent = Intent(this, AddEditNoteActivity::class.java)
                intent.putExtra("NOTE_ID", note.id)
                startActivity(intent)
            },
            onNoteLongClick = { note ->
                // Mostrar diálogo para eliminar
                showDeleteDialog(note)
            }
        )

        recyclerView.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }

    private fun showDeleteDialog(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar nota")
            .setMessage("¿Estás seguro de que quieres eliminar '${note.title}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                noteViewModel.delete(note)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            recyclerView.visibility = View.GONE
            tvEmptyState.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            tvEmptyState.visibility = View.GONE
        }
    }
}