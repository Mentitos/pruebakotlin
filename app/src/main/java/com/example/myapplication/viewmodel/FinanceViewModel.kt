package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.*
import kotlinx.coroutines.launch

class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: TransactionRepository
    val allTransactions: LiveData<List<Transaction>>
    val totalBalance: LiveData<Double?>
    val physicalBalance: LiveData<Double?>
    val digitalBalance: LiveData<Double?>
    
    init {
        val transactionDao = FinanceDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        allTransactions = repository.allTransactions
        totalBalance = repository.totalBalance
        physicalBalance = repository.getBalance(AccountType.PHYSICAL)
        digitalBalance = repository.getBalance(AccountType.DIGITAL)
    }
    
    fun insert(transaction: Transaction) = viewModelScope.launch {
        repository.insert(transaction)
    }
    
    fun update(transaction: Transaction) = viewModelScope.launch {
        repository.update(transaction)
    }
    
    fun delete(transaction: Transaction) = viewModelScope.launch {
        repository.delete(transaction)
    }
    
    suspend fun getTransactionById(transactionId: Int): Transaction? {
        return repository.getTransactionById(transactionId)
    }
}