package com.example.myapplication.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }
    
    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }
    
    @TypeConverter
    fun fromAccountType(value: AccountType): String {
        return value.name
    }
    
    @TypeConverter
    fun toAccountType(value: String): AccountType {
        return AccountType.valueOf(value)
    }
}