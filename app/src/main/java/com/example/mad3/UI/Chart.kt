package com.example.mad3.UI

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mad3.Data.Budget
import com.example.mad3.Data.ManagerPre
import com.example.mad3.Data.TransactionType
import com.example.mad3.R
import com.example.mad3.databinding.ActivityChartBinding
import java.util.Calendar
import java.util.Date

class Chart : AppCompatActivity() {
    private lateinit var binding: ActivityChartBinding
    private lateinit var preferences: ManagerPre

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize preferences
        preferences = ManagerPre(this)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
            R.id.bottomNavigationView
        ).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard -> {
                    startActivity(Intent(this, activity_dashboard::class.java))
                    true
                }
                R.id.nav_category_analysis -> true
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

        setupChart()
        setupButtons()
    }

    private fun setupChart() {
        // Load initial data (monthly by default)
        updateChartForTimeRange("monthly")
    }

    private fun setupButtons() {
        binding.weeklyButtonn.setOnClickListener {
            updateChartForTimeRange("weekly")
        }

        binding.monthlyButtonn.setOnClickListener {
            updateChartForTimeRange("monthly")
        }

        binding.customRangeButtonn.setOnClickListener {
            updateChartForTimeRange("all")
        }
    }

    private fun updateChartForTimeRange(range: String) {
        val allBudgets = preferences.getBudgetList()

        // Filter only EXPENSE transactions
        val expenseBudgets = allBudgets.filter { it.type == TransactionType.EXPENSE }

        // Filter budgets based on time range
        val filteredBudgets = when (range) {
            "weekly" -> getBudgetsForCurrentWeek(expenseBudgets)
            "monthly" -> getBudgetsForCurrentMonth(expenseBudgets)
            else -> expenseBudgets
        }

        // Group by category and sum amounts
        val categoryMap = mutableMapOf<String, Float>()
        filteredBudgets.forEach { budget ->
            val currentAmount = categoryMap[budget.category] ?: 0f
            categoryMap[budget.category] = currentAmount + budget.amount.toFloat()
        }

        // Convert to list of pairs for the chart
        val chartData = categoryMap.map { (category, amount) ->
            amount to category
        }

        // Update the chart
        binding.categoryPieChart.setData(chartData)

        // Calculate totals
        val total = chartData.sumOf { it.first.toDouble() }
        val monthlyBudget = preferences.getMonthlyBudget()

        // Update UI
        binding.totalSpendingTextVieww.text = "Total Spending: LKR ${"%.2f".format(total)}"

        if (monthlyBudget > 0) {
            val remaining = monthlyBudget - total
            val remainingColor = if (remaining >= 0) "#4CAF50" else "#F44336"
            binding.totalSpendingTextVieww.text =
                "Total Spending: LKR ${"%.2f".format(total)}\n\n"
//                        "<font color='#6200EE'>Monthly Budget: LKR${"%.2f".format(monthlyBudget)}</font>\n" +
//                        "<font color='$remainingColor'>Remaining: LKR${"%.2f".format(remaining)}</font>"
        }
    }

    private fun getBudgetsForCurrentWeek(budgets: List<Budget>): List<Budget> {
        val calendar = Calendar.getInstance()
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)

        return budgets.filter { budget ->
            calendar.time = budget.date
            calendar.get(Calendar.WEEK_OF_YEAR) == currentWeek &&
                    calendar.get(Calendar.YEAR) == currentYear
        }
    }

    private fun getBudgetsForCurrentMonth(budgets: List<Budget>): List<Budget> {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        return budgets.filter { budget ->
            calendar.time = budget.date
            calendar.get(Calendar.MONTH) == currentMonth &&
                    calendar.get(Calendar.YEAR) == currentYear
        }
    }
}