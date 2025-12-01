package com.example.myapplication.data

import androidx.lifecycle.LiveData

class TransactionRepository(private val transactionDao: TransactionDao) {
    
    val allTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransactions()
    val totalBalance: LiveData<Double?> = transactionDao.getTotalBalance()
    
    fun getBalance(accountType: AccountType): LiveData<Double?> {
        return transactionDao.getBalance(accountType)
    }
    
    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }
    
    suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction)
    }
    
    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction)
    }
    
    suspend fun getTransactionById(transactionId: Int): Transaction? {
        return transactionDao.getTransactionById(transactionId)
    }
}