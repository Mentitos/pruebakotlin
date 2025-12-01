package com.example.myapplication.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)
    
    @Update
    suspend fun update(transaction: Transaction)
    
    @Delete
    suspend fun delete(transaction: Transaction)
    
    @Query("SELECT * FROM transactions_table ORDER BY timestamp DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>
    
    @Query("SELECT * FROM transactions_table WHERE id = :transactionId")
    suspend fun getTransactionById(transactionId: Int): Transaction?
    
    @Query("SELECT SUM(amount) FROM transactions_table WHERE type = 'INCOME' AND accountType = :accountType")
    fun getTotalIncome(accountType: AccountType): LiveData<Double?>
    
    @Query("SELECT SUM(amount) FROM transactions_table WHERE type = 'EXPENSE' AND accountType = :accountType")
    fun getTotalExpense(accountType: AccountType): LiveData<Double?>
    
    @Query("SELECT SUM(CASE WHEN type = 'INCOME' THEN amount ELSE -amount END) FROM transactions_table WHERE accountType = :accountType")
    fun getBalance(accountType: AccountType): LiveData<Double?>
    
    @Query("SELECT SUM(CASE WHEN type = 'INCOME' THEN amount ELSE -amount END) FROM transactions_table")
    fun getTotalBalance(): LiveData<Double?>
}