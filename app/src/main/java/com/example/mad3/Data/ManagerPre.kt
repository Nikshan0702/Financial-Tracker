package com.example.mad3.Data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ManagerPre(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // Budget List functions
    fun setBudgetList(budgetList: List<Budget>) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(budgetList)
        editor.putString("budget_list", json)
        editor.apply()
    }

    fun getBudgetList(): List<Budget> {
        val gson = Gson()
        val json = sharedPreferences.getString("budget_list", null)
        val type = object : TypeToken<List<Budget>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    // Monthly Budget functions
    fun setMonthlyBudget(amount: Double) {
        val editor = sharedPreferences.edit()
        editor.putFloat("monthly_budget", amount.toFloat())
        editor.apply()
    }

    fun getMonthlyBudget(): Double {
        return sharedPreferences.getFloat("monthly_budget", 0f).toDouble()
    }
}
