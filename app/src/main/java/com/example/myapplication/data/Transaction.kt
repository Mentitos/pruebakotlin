package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "transactions_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val description: String = "",
    val accountType: AccountType,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    fun getFormattedAmount(): String {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "AR"))
        return format.format(amount)
    }
}

enum class TransactionType {
    INCOME,  // Depósito
    EXPENSE  // Retiro
}

enum class AccountType {
    PHYSICAL,  // Dinero Físico
    DIGITAL    // Dinero Digital
}