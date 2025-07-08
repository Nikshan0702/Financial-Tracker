package com.example.mad3.UI

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mad3.R

class activity_login : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvSignUp = findViewById<TextView>(R.id.tvSignUp)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            when {
                email.isEmpty() -> {
                    etEmail.error = "Email is required"
                    etEmail.requestFocus()
                    Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    etPassword.error = "Password is required"
                    etPassword.requestFocus()
                    Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                }
                !isValidEmail(email) -> {
                    etEmail.error = "Enter a valid email"
                    etEmail.requestFocus()
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                }
                password.length < 6 -> {
                    etPassword.error = "Password must be at least 6 characters"
                    etPassword.requestFocus()
                    Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // In a real app, verify credentials with database
                    if (email == "Naveeth@gmail.com" && password == "123456") {
                        with(sharedPreferences.edit()) {
                            putString("userName", "Naveeth")
                            putString("userEmail", email)
                            putBoolean("isLoggedIn", true)
                            apply()
                        }
                        startActivity(Intent(this, activity_dashboard::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        tvSignUp.setOnClickListener {
            startActivity(Intent(this, activity_signup::class.java))
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}