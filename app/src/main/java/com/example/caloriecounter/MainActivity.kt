package com.example.caloriecounter

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var frame: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        frame = findViewById(R.id.frameLayout)

        // set up the bottom navigation bar
        bottomNavigationView.setOnItemSelectedListener { menuItem ->

            // replace with the appropriate fragment
            when (menuItem.itemId) {
                R.id.dashboard -> {
                    replaceFragment(DashboardFragment())
                    true
                }

                R.id.logFood -> {
                    replaceFragment(LogFoodFragment())
                    true
                }

                R.id.settings -> {
                    replaceFragment(SettingsFragment())
                    true
                } else -> false
            }
        }

        // set the default fragment
        replaceFragment(DashboardFragment())

    }

    // function to replace the fragment
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }
}