package com.example.mad3.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad3.Data.Budget
import com.example.mad3.R
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionsAdapter(
    private val items: MutableList<Budget>,
    private val onEditClick: (Budget) -> Unit,
    private val onDeleteClick: (Budget) -> Unit
) : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val budget = items[position]
        holder.tvTitle.text = budget.title
        holder.tvCategory.text = budget.category
        holder.tvDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(budget.date)
        holder.tvAmount.text = "₹%.2f".format(budget.amount)

        holder.btnEdit.setOnClickListener { onEditClick(budget) }
        holder.btnDelete.setOnClickListener { onDeleteClick(budget) }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTransactionTitle)
        val tvCategory: TextView = view.findViewById(R.id.tvTransactionCategory)
        val tvDate: TextView = view.findViewById(R.id.tvTransactionDate)
        val tvAmount: TextView = view.findViewById(R.id.tvTransactionAmount)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }
}
