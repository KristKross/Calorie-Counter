package com.example.caloriecounter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LogIn : AppCompatActivity() {
    private lateinit var back: ImageView
    private lateinit var logInButton: Button

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // gets email and password from shared preferences
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

        val email = sharedPreferences.getString("email", "")
        val password = sharedPreferences.getString("password", "")

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        Log.d("SharedPreferences", "Email: $email, Password: $password")

        back = findViewById(R.id.backToMainMenu)
        logInButton = findViewById(R.id.logInButton)

        // back to MainMenu
        back.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }

        // log in the app
        logInButton.setOnClickListener {
            // returns if email does not match
            if (emailEditText.text.toString() != email) {
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCanceledOnTouchOutside(true)
                dialog.setContentView(R.layout.error_modal_popup)
                dialog.window?.setBackgroundDrawableResource(R.drawable.modal_bg)
                dialog.window?.setLayout(
                    900,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val textView = dialog.findViewById<TextView>(R.id.errorTextView)
                textView.text = "Email is incorrect"

                dialog.show()
                return@setOnClickListener
            }

            // returns if password does not match
            if (passwordEditText.text.toString() != password) {
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCanceledOnTouchOutside(true)
                dialog.setContentView(R.layout.error_modal_popup)
                dialog.window?.setBackgroundDrawableResource(R.drawable.modal_bg)
                dialog.window?.setLayout(
                    900,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val textView = dialog.findViewById<TextView>(R.id.errorTextView)
                textView.text = "Password is incorrect"

                dialog.show()
                return@setOnClickListener
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}