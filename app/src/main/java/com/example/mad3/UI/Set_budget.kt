package com.example.mad3.UI

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mad3.Data.ManagerPre
import com.example.mad3.R

class set_budget : AppCompatActivity() {

    private lateinit var prefsManager: ManagerPre

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_set_budget)

        prefsManager = ManagerPre(this)

        val etBudgetAmount = findViewById<EditText>(R.id.etBudgetAmount)
        val btnSaveBudget = findViewById<Button>(R.id.btnSaveBudget)

        btnSaveBudget.setOnClickListener {
            val amountText = etBudgetAmount.text.toString()
            if (amountText.isNotEmpty()) {
                val budgetAmount = amountText.toDoubleOrNull()
                if (budgetAmount != null) {
                    prefsManager.setMonthlyBudget(budgetAmount)
                    Toast.makeText(this, "Budget saved successfully!", Toast.LENGTH_SHORT).show()
                    finish()  // go back to Monthly Budget screen
                } else {
                    Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
