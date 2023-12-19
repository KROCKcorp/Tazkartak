package com.example.movieui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth

class RegisterScreenActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)

        auth = FirebaseAuth.getInstance()

        val registerButton = findViewById<AppCompatButton>(R.id.register_confirm)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                registerUser(email, password)
            } else {
                Toast.makeText(
                    this,
                    "Please fill in all fields and ensure passwords match",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val loginButton = findViewById<AppCompatButton>(R.id.login)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginScreenActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Registration successful",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Navigate to the login screen after successful registration
                    val intent = Intent(this, LoginScreenActivity::class.java)
                    startActivity(intent)
                    finish() // Finish the current activity to prevent going back to registration
                } else {
                    Toast.makeText(
                        this,
                        "Registration failed. Please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
