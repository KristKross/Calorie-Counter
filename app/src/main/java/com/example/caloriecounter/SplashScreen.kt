package com.example.caloriecounter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val icon = findViewById<ImageView>(R.id.dumbbell)

        android.os.Handler().postDelayed({
            fadeIn(icon)

            val text = findViewById<TextView>(R.id.splashScreenTextView)
            fadeIn(text)
        }, 600L)

        android.os.Handler().postDelayed({
            icon.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce))
        }, 700L)

        android.os.Handler().postDelayed({
            startActivity(Intent(this, MainMenu::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }

    private fun fadeIn(view: View) {
        val fadeIn = AlphaAnimation(0.0f, 1.0f)
        fadeIn.duration = 1000L
        view.startAnimation(fadeIn)
        view.visibility = View.VISIBLE
    }
}