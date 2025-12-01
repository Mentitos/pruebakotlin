package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.TransactionAdapter
import com.example.myapplication.data.Transaction
import com.example.myapplication.data.TransactionType
import com.example.myapplication.viewmodel.FinanceViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var financeViewModel: FinanceViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddTransaction: FloatingActionButton
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var transactionAdapter: TransactionAdapter
    
    // Vistas de estadísticas
    private lateinit var tvTotalSavings: TextView
    private lateinit var tvPhysicalMoney: TextView
    private lateinit var tvDigitalMoney: TextView
    private lateinit var tvTotalRecords: TextView
    private lateinit var tvDeposits: TextView
    private lateinit var tvWithdrawals: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerView)
        fabAddTransaction = findViewById(R.id.fabAddNote)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        
        // Vistas de estadísticas
        tvTotalSavings = findViewById(R.id.tvTotalNotes)
        tvPhysicalMoney = findViewById(R.id.tvShortNotes)
        tvDigitalMoney = findViewById(R.id.tvLongNotes)
        tvTotalRecords = findViewById(R.id.tvTotalRegistros)
        tvDeposits = findViewById(R.id.tvTodayNotes)
        tvWithdrawals = findViewById(R.id.tvEditedNotes)

        // Configurar RecyclerView
        setupRecyclerView()

        // Inicializar ViewModel
        financeViewModel = ViewModelProvider(this)[FinanceViewModel::class.java]

        // Observar cambios en las transacciones
        financeViewModel.allTransactions.observe(this) { transactions ->
            transactionAdapter.setTransactions(transactions)
            updateEmptyState(transactions.isEmpty())
            updateStatistics(transactions)
        }
        
        // Observar balances
        financeViewModel.totalBalance.observe(this) { balance ->
            tvTotalSavings.text = formatCurrency(balance ?: 0.0)
        }
        
        financeViewModel.physicalBalance.observe(this) { balance ->
            tvPhysicalMoney.text = formatCurrency(balance ?: 0.0)
        }
        
        financeViewModel.digitalBalance.observe(this) { balance ->
            tvDigitalMoney.text = formatCurrency(balance ?: 0.0)
        }

        // Configurar FAB
        fabAddTransaction.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter(
            onTransactionClick = { transaction ->
                // Abrir transacción para editar
                val intent = Intent(this, AddTransactionActivity::class.java)
                intent.putExtra("TRANSACTION_ID", transaction.id)
                startActivity(intent)
            },
            onTransactionLongClick = { transaction ->
                // Mostrar diálogo para eliminar
                showDeleteDialog(transaction)
            }
        )

        recyclerView.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }

    private fun showDeleteDialog(transaction: Transaction) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar transacción")
            .setMessage("¿Estás seguro de que quieres eliminar esta transacción de ${transaction.getFormattedAmount()}?")
            .setPositiveButton("Eliminar") { _, _ ->
                financeViewModel.delete(transaction)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            recyclerView.visibility = View.GONE
            emptyStateLayout.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyStateLayout.visibility = View.GONE
        }
    }
    
    private fun updateStatistics(transactions: List<Transaction>) {
        // Total de registros
        tvTotalRecords.text = transactions.size.toString()
        
        // Total de depósitos (ingresos)
        val deposits = transactions.count { it.type == TransactionType.INCOME }
        tvDeposits.text = deposits.toString()
        
        // Total de retiros (gastos)
        val withdrawals = transactions.count { it.type == TransactionType.EXPENSE }
        tvWithdrawals.text = withdrawals.toString()
    }
    
    private fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "AR"))
        return format.format(amount)
    }
}