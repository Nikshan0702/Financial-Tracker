package com.example.mad3.UI

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mad3.Data.ManagerPre
import com.example.mad3.Data.TransactionType
import com.example.mad3.R
import java.text.NumberFormat
import java.util.Locale

class monthly_budget_setup : AppCompatActivity() {

    private lateinit var prefsManager: ManagerPre
    private var hasSentBudgetAlert = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_monthly_budget_setup)

        prefsManager = ManagerPre(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnSetBudget).setOnClickListener {
            startActivity(Intent(this, set_budget::class.java))
        }

        // 🆕 Arrow back to dashboard
        val imageView = findViewById<ImageView>(R.id.arrow)
        imageView.setOnClickListener {
            val intent = Intent(this, activity_dashboard::class.java)
            startActivity(intent)
        }

        // Request POST_NOTIFICATIONS permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
            }
        }
        findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
            R.id.bottomNavigationView
        ).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard ->{
                    startActivity(Intent(this, activity_dashboard::class.java))
                    true
                }
                R.id.nav_category_analysis -> {
                    startActivity(Intent(this, Chart::class.java))
                    true
                }
                R.id.nav_monthly_budget -> true

                R.id.nav_settings -> {
                    startActivity(Intent(this, activity_settings::class.java))
                    true
                }
                else -> false
            }
        }


        updateBudgetInfo()
    }

    private fun sendBudgetNotification(spentPercentage: Double) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "budget_alerts"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Budget Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies when your budget usage exceeds a set limit"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val formattedPercentage = String.format("%.1f", spentPercentage)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_warning)
            .setContentTitle("Budget Warning ⚠️")
            .setContentText("You've used $formattedPercentage% of your budget.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(1001, builder.build())
    }

    private fun updateBudgetInfo() {
        val monthlyBudget = prefsManager.getMonthlyBudget()
        val budgets = prefsManager.getBudgetList()

        val totalExpenses = budgets.filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }

        val remaining = monthlyBudget - totalExpenses
        val spentPercentage = if (monthlyBudget > 0) (totalExpenses / monthlyBudget) * 100 else 0.0

        val numberFormat = NumberFormat.getNumberInstance(Locale("en", "LK"))
        numberFormat.maximumFractionDigits = 2

        findViewById<TextView>(R.id.txtBudgetAmount).text = "LKR ${numberFormat.format(monthlyBudget)}"
        findViewById<TextView>(R.id.txtSpentAmount).text = "LKR ${numberFormat.format(totalExpenses)}"
        findViewById<TextView>(R.id.txtRemainingAmount).text = "LKR ${numberFormat.format(remaining)}"
        findViewById<TextView>(R.id.txtPercentage).text = String.format("%.1f%%", spentPercentage)

        findViewById<ProgressBar>(R.id.progressBudget).progress = spentPercentage.toInt().coerceAtMost(100)

        findViewById<TextView>(R.id.txtWarning).text =
            if (spentPercentage >= 90) "You're approaching your budget limit" else ""

        if (spentPercentage >= 90 && !hasSentBudgetAlert) {
            sendBudgetNotification(spentPercentage)
            hasSentBudgetAlert = true
        } else if (spentPercentage < 90) {
            hasSentBudgetAlert = false
        }
    }

    override fun onResume() {
        super.onResume()
        updateBudgetInfo()
    }
}
