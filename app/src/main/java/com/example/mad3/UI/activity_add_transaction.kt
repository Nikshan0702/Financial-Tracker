package com.example.mad3.UI

import android.app.DatePickerDialog
import android.content.Intent // <--- Add this import
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mad3.R
import com.example.mad3.Data.TransactionType
import com.example.mad3.Data.Budget
import com.example.mad3.Data.ManagerPre
import java.text.SimpleDateFormat
import java.util.*

class activity_add_transaction : AppCompatActivity() {

    private lateinit var etTransactionTitle: EditText
    private lateinit var etAmount: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var etDate: EditText
    private lateinit var rgType: RadioGroup
    private lateinit var btnSaveTransaction: Button

    private var selectedDate: Date = Date()
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        etTransactionTitle = findViewById(R.id.etTransactionTitlee)
        etAmount = findViewById(R.id.etAmountt)
        spinnerCategory = findViewById(R.id.spinnerCategoryy)
        etDate = findViewById(R.id.etDatee)
        rgType = findViewById(R.id.rgTypee)
        btnSaveTransaction = findViewById(R.id.btnSaveTransactionn)

        // 🔁 Connect back arrow ImageView
        val imageView = findViewById<ImageView>(R.id.ivBack)
        imageView.setOnClickListener {
            val intent = Intent(this, activity_transaction_history::class.java)
            startActivity(intent)
        }

        // Get categories from strings.xml
        val incomeCategories = resources.getStringArray(R.array.income_categories)
        val expenseCategories = resources.getStringArray(R.array.expense_categories)

        // Function to update spinner with categories
        fun updateCategorySpinner(items: Array<String>) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter
        }

        // Set default categories to Expense
        updateCategorySpinner(expenseCategories)

        // Switch category list based on selected transaction type
        rgType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbIncomee -> updateCategorySpinner(incomeCategories)
                R.id.rbExpensee -> updateCategorySpinner(expenseCategories)
            }
        }

        etDate.setOnClickListener { showDatePicker() }
        updateDateDisplay()

        btnSaveTransaction.setOnClickListener {
            if (saveTransaction()) {
                finish()
            }
        }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                selectedDate = calendar.time
                updateDateDisplay()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateDisplay() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        etDate.setText(dateFormat.format(selectedDate))
    }

    private fun saveTransaction(): Boolean {
        val title = etTransactionTitle.text.toString()
        val amountText = etAmount.text.toString()
        val category = spinnerCategory.selectedItem.toString()

        val type = when (rgType.checkedRadioButtonId) {
            R.id.rbIncomee -> TransactionType.INCOME
            R.id.rbExpensee -> TransactionType.EXPENSE
            else -> {
                Toast.makeText(this, "Please select transaction type", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        if (title.isBlank()) {
            etTransactionTitle.error = "Title is required"
            return false
        }

        val amount = try {
            amountText.toDouble()
        } catch (e: NumberFormatException) {
            etAmount.error = "Invalid amount"
            return false
        }

        if (amount <= 0) {
            etAmount.error = "Amount must be positive"
            return false
        }

        val newBudget = Budget(
            title = title,
            amount = amount,
            category = category,
            date = selectedDate,
            type = type
        )

        val prefs = ManagerPre(this)
        val list = prefs.getBudgetList().toMutableList()
        list.add(newBudget)
        prefs.setBudgetList(list)

        Toast.makeText(this, "Transaction Saved!", Toast.LENGTH_SHORT).show()
        return true
    }
}
