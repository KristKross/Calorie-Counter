package com.example.caloriecounter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class MainMenu : AppCompatActivity() {
    private lateinit var logInButton: AppCompatButton
    private lateinit var signUpButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        logInButton = findViewById(R.id.logInButton)
        signUpButton = findViewById(R.id.signUpButton)

        // transitions to login page
        logInButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }

        // transitions to sign in page
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}