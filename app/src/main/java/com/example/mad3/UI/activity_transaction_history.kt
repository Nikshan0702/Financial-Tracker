package com.example.mad3.UI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad3.Adapter.TransactionsAdapter
import com.example.mad3.Data.Budget
import com.example.mad3.Data.ManagerPre
import com.example.mad3.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class activity_transaction_history : AppCompatActivity() {

    private lateinit var rvTransactions: RecyclerView
    private lateinit var adapter: TransactionsAdapter
    private val transactionList = mutableListOf<Budget>()
    private lateinit var managerPre: ManagerPre


    private val editTransactionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updated = result.data
                ?.getParcelableExtra<Budget>("updated_budget")
            val pos = result.data
                ?.getIntExtra("edited_position", -1) ?: -1

            if (updated != null && pos != -1) {
                // replace at that exact position
                transactionList[pos] = updated
                adapter.notifyItemChanged(pos)
                managerPre.setBudgetList(transactionList)
            }
        }
    }


    private val addTransactionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getParcelableExtra<Budget>("new_budget")?.let { newBudget ->
                transactionList.add(newBudget)
                adapter.notifyItemInserted(transactionList.lastIndex)
                rvTransactions.scrollToPosition(transactionList.lastIndex)
                managerPre.setBudgetList(transactionList)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)
        managerPre = ManagerPre(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sysBars.left, sysBars.top, sysBars.right, sysBars.bottom)
            insets
        }


        transactionList.clear()
        transactionList.addAll(managerPre.getBudgetList())

        // setup RecyclerView
        rvTransactions = findViewById(R.id.recyclerViewTransactions)
        adapter = TransactionsAdapter(
            transactionList,
            onEditClick = { budget ->
                val position = transactionList.indexOf(budget)
                val intent = Intent(this, activity_edit_transaction::class.java).apply {
                    putExtra("edit_budget", budget)
                    putExtra("edit_position", position)
                }
                editTransactionLauncher.launch(intent)
            },
            onDeleteClick = { budget ->
                val idx = transactionList.indexOf(budget)
                if (idx != -1) {
                    transactionList.removeAt(idx)
                    adapter.notifyItemRemoved(idx)
                    managerPre.setBudgetList(transactionList)
                }
            }
        )
        rvTransactions.layoutManager = LinearLayoutManager(this)
        rvTransactions.adapter = adapter


        findViewById<FloatingActionButton>(R.id.fabAddTransaction).setOnClickListener {
            addTransactionLauncher.launch(
                Intent(this, activity_add_transaction::class.java)
            )
        }

        findViewById<ImageView>(R.id.arrow2).setOnClickListener {
            startActivity(Intent(this, activity_dashboard::class.java))
        }
    }
}
