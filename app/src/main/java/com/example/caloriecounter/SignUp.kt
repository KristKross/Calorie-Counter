package com.example.caloriecounter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // transitions to ActivityLevelFragment
        val fragment = ActivityLevelFragment()

        val fragmentManager = supportFragmentManager

        val transaction = fragmentManager.beginTransaction()

        transaction.add(R.id.signUpFrame, fragment)

        transaction.addToBackStack(null)

        transaction.commit()
    }
}