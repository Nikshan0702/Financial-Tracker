package com.example.mad3.UI

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mad3.Data.Budget
import com.example.mad3.Data.ManagerPre
import com.example.mad3.Data.TransactionType
import com.example.mad3.R
import java.text.SimpleDateFormat
import java.util.*

class activity_edit_transaction : AppCompatActivity() {

    private lateinit var managerPre: ManagerPre
    private lateinit var selectedBudget: Budget
    private lateinit var etTitle: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDate: EditText
    private lateinit var rgType: RadioGroup
    private lateinit var spinnerCategory: Spinner
    private var selectedDate: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)

        managerPre = ManagerPre(this)
        selectedBudget = intent.getParcelableExtra("edit_budget")!!
        val editPosition = intent.getIntExtra("edit_position", -1)

        etTitle = findViewById(R.id.etTransactionTitlee)
        etAmount = findViewById(R.id.etAmountt)
        etDate = findViewById(R.id.etDatee)
        rgType = findViewById(R.id.rgTypee)
        spinnerCategory = findViewById(R.id.spinnerCategoryy)

        etTitle.setText(selectedBudget.title)
        etAmount.setText(selectedBudget.amount.toString())
        etDate.setText(
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).format(selectedBudget.date)
        )
        selectedDate = selectedBudget.date

        // Load category arrays
        val incomeCategories = resources.getStringArray(R.array.income_categories)
        val expenseCategories = resources.getStringArray(R.array.expense_categories)

        // Helper to load spinner
        fun updateCategorySpinner(items: Array<String>, selectedCategory: String) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter
            spinnerCategory.setSelection(items.indexOf(selectedCategory))
        }

        // Set selected transaction type
        if (selectedBudget.type == TransactionType.INCOME) {
            rgType.check(R.id.rbIncomee)
            updateCategorySpinner(incomeCategories, selectedBudget.category)
        } else {
            rgType.check(R.id.rbExpensee)
            updateCategorySpinner(expenseCategories, selectedBudget.category)
        }

        // Allow category switch on type change
        rgType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbIncomee -> updateCategorySpinner(incomeCategories, incomeCategories.first())
                R.id.rbExpensee -> updateCategorySpinner(
                    expenseCategories,
                    expenseCategories.first()
                )
            }
        }

        // Date Picker
        etDate.setOnClickListener {
            val cal = Calendar.getInstance().apply { time = selectedDate }
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    cal.set(y, m, d)
                    selectedDate = cal.time
                    etDate.setText(
                        SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                        ).format(cal.time)
                    )
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Update Button
        findViewById<Button>(R.id.btnUpdateTransactionn).setOnClickListener {
            val updated = Budget(
                title = etTitle.text.toString(),
                amount = etAmount.text.toString().toDoubleOrNull() ?: 0.0,
                category = spinnerCategory.selectedItem.toString(),
                date = selectedDate,
                type = if (rgType.checkedRadioButtonId == R.id.rbIncomee)
                    TransactionType.INCOME
                else
                    TransactionType.EXPENSE
            )

            val list = managerPre.getBudgetList().toMutableList()

            if (editPosition != -1) {
                list[editPosition] = updated
                managerPre.setBudgetList(list)

                val result = Intent()
                    .putExtra("updated_budget", updated)
                    .putExtra("edited_position", editPosition)
                setResult(Activity.RESULT_OK, result)
                finish()
            }
        }

        val imageView = findViewById<ImageView>(R.id.ivBack)
        imageView.setOnClickListener {
            val intent = Intent(this, activity_transaction_history::class.java)
            startActivity(intent)


            // Delete Button
            findViewById<Button>(R.id.btnDeleteTransactionn).setOnClickListener {
                val list = managerPre.getBudgetList().toMutableList()
                if (editPosition != -1) {
                    list.removeAt(editPosition)
                    managerPre.setBudgetList(list)
                }
                finish()
            }
        }
    }
}
