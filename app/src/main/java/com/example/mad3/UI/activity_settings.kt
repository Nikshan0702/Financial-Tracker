package com.example.mad3.UI

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mad3.R

class activity_settings : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Back button
        findViewById<ImageButton>(R.id.backBtn).setOnClickListener {
            finish()
        }

        // Display user name
        val userName = sharedPreferences.getString("userName", "User")
        findViewById<TextView>(R.id.userNameDisplay).text = "Welcome, $userName"

        // Logout button
        findViewById<Button>(R.id.logoutBtn).setOnClickListener {
            with(sharedPreferences.edit()) {
                clear()
                apply()
            }
            val intent = Intent(this, activity_login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}