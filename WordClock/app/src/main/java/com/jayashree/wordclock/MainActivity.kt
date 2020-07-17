package com.jayashree.wordclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var selectedFragment: Fragment

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                selectedFragment = DashboardFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
        }

        when (item.itemId) {
            R.id.navigation_settings -> {
                selectedFragment = SettingsFragment()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        //Set default to dashboard fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, DashboardFragment()).commit()

    }
}