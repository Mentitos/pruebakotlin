package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Transaction
import com.example.myapplication.data.TransactionType

class TransactionAdapter(
    private val onTransactionClick: (Transaction) -> Unit,
    private val onTransactionLongClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    
    private var transactions = emptyList<Transaction>()
    
    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tvNoteTitle)
        val contentTextView: TextView = itemView.findViewById(R.id.tvNoteContent)
        val dateTextView: TextView = itemView.findViewById(R.id.tvNoteDate)
        val amountTextView: TextView = itemView.findViewById(R.id.tvNoteLength)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return TransactionViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentTransaction = transactions[position]
        
        // Título: Tipo de transacción
        val typeText = if (currentTransaction.type == TransactionType.INCOME) {
            "💰 Depósito"
        } else {
            "💸 Retiro"
        }
        holder.titleTextView.text = typeText
        
        // Contenido: Categoría y descripción
        val accountText = if (currentTransaction.accountType.name == "PHYSICAL") "Físico" else "Digital"
        holder.contentTextView.text = "${currentTransaction.category} · $accountText${
            if (currentTransaction.description.isNotEmpty()) " · ${currentTransaction.description}" else ""
        }"
        
        // Fecha
        holder.dateTextView.text = currentTransaction.getFormattedDate()
        
        // Monto
        holder.amountTextView.text = currentTransaction.getFormattedAmount()
        
        // Color del monto según tipo
        val color = if (currentTransaction.type == TransactionType.INCOME) {
            holder.itemView.context.getColor(R.color.stat_green)
        } else {
            holder.itemView.context.getColor(R.color.stat_red)
        }
        holder.amountTextView.setTextColor(color)
        
        holder.itemView.setOnClickListener {
            onTransactionClick(currentTransaction)
        }
        
        holder.itemView.setOnLongClickListener {
            onTransactionLongClick(currentTransaction)
            true
        }
    }
    
    override fun getItemCount() = transactions.size
    
    fun setTransactions(transactions: List<Transaction>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }
}