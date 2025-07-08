package com.example.mad3.UI

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mad3.R

class activity_signup : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        val etFullName = findViewById<EditText>(R.id.etFullNamee)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPasswordd)
        val btnSignUp = findViewById<Button>(R.id.btnSignUpp)
        val tvLoginHere = findViewById<TextView>(R.id.tvLoginHeree)

        btnSignUp.setOnClickListener {
            val fullName = etFullName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            when {
                fullName.isEmpty() -> etFullName.error = "Full name is required"
                email.isEmpty() -> etEmail.error = "Email is required"
                password.isEmpty() -> etPassword.error = "Password is required"
                confirmPassword.isEmpty() -> etConfirmPassword.error = "Confirm password is required"
                password != confirmPassword -> Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
                else -> {
                    with(sharedPreferences.edit()) {
                        putString("userName", fullName)
                        putString("userEmail", email)
                        putBoolean("isLoggedIn", true)
                        apply()
                    }
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, activity_dashboard::class.java))
                    finish()
                }
            }
        }

        tvLoginHere.setOnClickListener {
            startActivity(Intent(this, activity_login::class.java))
            finish()
        }
    }
}