package com.umutsaydam.rickandmortyapp.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.umutsaydam.rickandmortyapp.databinding.ActivityMainBinding
import com.umutsaydam.rickandmortyapp.utils.NetworkConnectivityChecker
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var shared: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shared = getSharedPreferences("MODE", Context.MODE_PRIVATE)

        setupTheme()
        setupLang()
        setupNavigation()
    }

    private fun setupLang() {
        val targetLang = shared.getString("lang", "EN")
        if (targetLang != "EN") {
            val config = resources.configuration
            val locale = Locale(targetLang!!)
            Locale.setDefault(locale)
            config.setLocale(locale)

            this.createConfigurationContext(config)
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }
    }

    private fun setupTheme() {
        if (shared.getBoolean("darkMode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun setupNavigation() {
        val nvHost =
            supportFragmentManager.findFragmentById(binding.rickAndMortyFragments.id) as NavHostFragment
        binding.btNavigation.setupWithNavController(nvHost.navController)
    }
}