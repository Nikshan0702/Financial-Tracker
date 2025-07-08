package com.example.mad3.Data

class TransactionSet {
    private val budgetList = mutableListOf<Budget>()

    fun addBudget(budget: Budget) {
        budgetList.add(budget)
    }

    fun getAllBudgets(): List<Budget> {
        return budgetList
    }

    fun deleteBudget(budget: Budget) {
        budgetList.remove(budget)
    }

    fun clearAllBudgets() {
        budgetList.clear()
    }
}
