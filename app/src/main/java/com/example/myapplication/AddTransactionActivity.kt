package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.AccountType
import com.example.myapplication.data.Transaction
import com.example.myapplication.data.TransactionType
import com.example.myapplication.viewmodel.FinanceViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    
    private lateinit var financeViewModel: FinanceViewModel
    private lateinit var toolbar: MaterialToolbar
    private lateinit var etAmount: TextInputEditText
    private lateinit var etCategory: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var spinnerType: Spinner
    private lateinit var spinnerAccount: Spinner
    private lateinit var btnSave: Button
    
    private var transactionId: Int = -1
    private var isEditMode = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        
        // Inicializar ViewModel
        financeViewModel = ViewModelProvider(this)[FinanceViewModel::class.java]
        
        // Inicializar vistas
        toolbar = findViewById(R.id.toolbar)
        etAmount = findViewById(R.id.etAmount)
        etCategory = findViewById(R.id.etCategory)
        etDescription = findViewById(R.id.etDescription)
        spinnerType = findViewById(R.id.spinnerType)
        spinnerAccount = findViewById(R.id.spinnerAccount)
        btnSave = findViewById(R.id.btnSaveNote)
        
        // Configurar toolbar
        toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // Configurar spinners
        setupSpinners()
        
        // Verificar si es modo edición
        transactionId = intent.getIntExtra("TRANSACTION_ID", -1)
        isEditMode = transactionId != -1
        
        if (isEditMode) {
            toolbar.title = "Editar Transacción"
            loadTransactionData()
        } else {
            toolbar.title = "Nueva Transacción"
        }
        
        // Configurar botón guardar
        btnSave.setOnClickListener {
            saveTransaction()
        }
    }
    
    private fun setupSpinners() {
        // Spinner de tipo
        val typeAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Depósito", "Retiro")
        )
        spinnerType.adapter = typeAdapter
        
        // Spinner de cuenta
        val accountAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Dinero Físico", "Dinero Digital")
        )
        spinnerAccount.adapter = accountAdapter
    }
    
    private fun loadTransactionData() {
        lifecycleScope.launch {
            val transaction = financeViewModel.getTransactionById(transactionId)
            transaction?.let {
                etAmount.setText(it.amount.toString())
                etCategory.setText(it.category)
                etDescription.setText(it.description)
                
                spinnerType.setSelection(if (it.type == TransactionType.INCOME) 0 else 1)
                spinnerAccount.setSelection(if (it.accountType == AccountType.PHYSICAL) 0 else 1)
            }
        }
    }

    private fun saveTransaction() {
        val amountStr = etAmount.text.toString().trim()
        val category = etCategory.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa un monto", Toast.LENGTH_SHORT).show()
            return
        }
        
        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Por favor ingresa un monto válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (category.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa una categoría", Toast.LENGTH_SHORT).show()
            return
        }
        
        val type = if (spinnerType.selectedItemPosition == 0) {
            TransactionType.INCOME
        } else {
            TransactionType.EXPENSE
        }
        
        val accountType = if (spinnerAccount.selectedItemPosition == 0) {
            AccountType.PHYSICAL
        } else {
            AccountType.DIGITAL
        }

        val transaction = Transaction(
            id = if (isEditMode) transactionId else 0,
            amount = amount,
            type = type,
            category = category,
            description = description,
            accountType = accountType,
            timestamp = System.currentTimeMillis()
        )

        if (isEditMode) {
            financeViewModel.update(transaction)
            Toast.makeText(this, "Transacción actualizada", Toast.LENGTH_SHORT).show()
        } else {
            financeViewModel.insert(transaction)
            Toast.makeText(this, "Transacción guardada", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}