package com.example.foodrecipesapp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.foodrecipesapp.R
import com.example.foodrecipesapp.databinding.ActivityAuthBinding
import com.example.foodrecipesapp.ui.auth.login.OnLoginSuccessListener
import com.example.foodrecipesapp.ui.main.MainActivity

class AuthActivity : AppCompatActivity(), OnLoginSuccessListener {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_login)
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.login_navigation)
        navController.graph = graph
    }
    override fun onUserLoginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}