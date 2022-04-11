package com.example.chat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.chat.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChatActivity : AppCompatActivity() {
//    private lateinit var binding: LoginRegActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = LoginRegActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_chat)
//
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.fragmentContainerView3) as NavHostFragment
//        val navController = navHostFragment.navController
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavBar)
//        bottomNavigationView.setupWithNavController(navController)

    }
}