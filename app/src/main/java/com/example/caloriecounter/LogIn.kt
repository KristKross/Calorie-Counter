package com.example.caloriecounter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class LogIn : AppCompatActivity() {
    private lateinit var back: ImageView
    private lateinit var logInButton: Button

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

        val email = sharedPreferences.getString("email", "")
        val password = sharedPreferences.getString("password", "")

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        Log.d("SharedPreferences", "Email: $email, Password: $password")

        val currentTime = System.currentTimeMillis()
        val counter = currentTime - (24 * 60 * 60 * 1000)
        if (currentTime >= counter) {
            resetCalorieData(this)
        }

        back = findViewById(R.id.backToMainMenu)
        logInButton = findViewById(R.id.logInButton)

        back.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        logInButton.setOnClickListener {
            if (emailEditText.text.toString() != email) {
                Toast.makeText(this, "Email is incorrect", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passwordEditText.text.toString() != password) {
                Toast.makeText(this, "Password is incorrect", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    companion object {
        private fun resetCalorieData(context: Context) {
            val dataFile = "calorie_data.txt"
            val file = File(context.filesDir, dataFile)
            file.writeText("")
        }
    }
}