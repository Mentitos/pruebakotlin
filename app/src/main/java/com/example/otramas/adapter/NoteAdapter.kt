package com.example.otramas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.otramas.R
import com.example.otramas.data.Note

class NoteAdapter(
    private val onNoteClick: (Note) -> Unit,
    private val onNoteLongClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    
    private var notes = emptyList<Note>()
    
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tvNoteTitle)
        val contentTextView: TextView = itemView.findViewById(R.id.tvNoteContent)
        val dateTextView: TextView = itemView.findViewById(R.id.tvNoteDate)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notes[position]
        
        holder.titleTextView.text = currentNote.title
        holder.contentTextView.text = currentNote.content
        holder.dateTextView.text = currentNote.getFormattedDate()
        
        holder.itemView.setOnClickListener {
            onNoteClick(currentNote)
        }
        
        holder.itemView.setOnLongClickListener {
            onNoteLongClick(currentNote)
            true
        }
    }
    
    override fun getItemCount() = notes.size
    
    fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }
}