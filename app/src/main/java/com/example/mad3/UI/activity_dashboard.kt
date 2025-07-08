package com.example.mad3.UI

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mad3.Data.Budget
import com.example.mad3.Data.ManagerPre
import com.example.mad3.Data.TransactionType
import com.example.mad3.R
import java.text.NumberFormat
import java.util.Locale

class activity_dashboard : AppCompatActivity() {
    private lateinit var prefsManager: ManagerPre

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        prefsManager = ManagerPre(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnAddTransactionn).setOnClickListener {
            startActivity(Intent(this, activity_add_transaction::class.java))
        }

        findViewById<Button>(R.id.btnedittransaction).setOnClickListener {
            startActivity(Intent(this, activity_transaction_history::class.java))
        }

        findViewById<Button>(R.id.btnSetBudget).setOnClickListener {
            startActivity(Intent(this, monthly_budget_setup::class.java))
        }

        findViewById<Button>(R.id.btnbackupp).setOnClickListener {
            startActivity(Intent(this, activity_backup_restore::class.java))
        }

        findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
            R.id.bottomNavigationView
        ).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard -> true
                R.id.nav_category_analysis -> {
                    startActivity(Intent(this, Chart::class.java))
                    true
                }
                R.id.nav_monthly_budget -> {
                    startActivity(Intent(this, monthly_budget_setup::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, activity_settings::class.java))
                    true
                }
                else -> false
            }
        }

        updateFinancialData()
    }

    override fun onResume() {
        super.onResume()
        updateFinancialData()
    }

    private fun updateFinancialData() {
        val budgets = prefsManager.getBudgetList()

        val income = budgets.filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
        val expenses = budgets.filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
        val balance = income - expenses

        val numberFormat = NumberFormat.getNumberInstance(Locale("en", "LK"))
        numberFormat.maximumFractionDigits = 0

        val formattedIncome = "LKR " + numberFormat.format(income)
        val formattedExpenses = "LKR " + numberFormat.format(expenses)
        val formattedBalance = "LKR " + numberFormat.format(balance)

        findViewById<TextView>(R.id.tvBalanceAmountt).text = formattedBalance
        findViewById<TextView>(R.id.tvIncomee).text = formattedIncome
        findViewById<TextView>(R.id.tvExpensess).text = formattedExpenses
    }

}