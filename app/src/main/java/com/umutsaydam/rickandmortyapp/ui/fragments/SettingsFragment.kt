package com.umutsaydam.rickandmortyapp.ui.fragments

import android.app.LocaleManager
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.umutsaydam.rickandmortyapp.R
import com.umutsaydam.rickandmortyapp.databinding.FragmentSettingBinding
import com.umutsaydam.rickandmortyapp.ui.MainActivity
import java.util.Locale


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var shared: SharedPreferences
    private lateinit var editor: Editor
    private lateinit var langs: Array<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)


        shared = activity!!.getSharedPreferences("MODE", Context.MODE_PRIVATE)
        editor = shared.edit()


        updateSwitch(shared.getBoolean("darkMode", false))
        Log.d("theme", shared.getBoolean("darkMode", false).toString())
        setupUI()

        return binding.root
    }

    private fun setupUI() {
        val currLang = shared.getString("lang", "EN")
        binding.tvCurrentLanguage.text = currLang

        binding.cvDarkTheme.setOnClickListener {
            val state = !shared.getBoolean("darkMode", false)
            commitShared(state)
            updateSwitch(state)
            changeTheme(state)
        }
        binding.switchDarkTheme.setOnCheckedChangeListener { _, p1 ->
            commitShared(p1)
            updateSwitch(p1)
            changeTheme(p1)
        }


        langs = resources.getStringArray(R.array.langs)

        val dialog = AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.select_a_language))
            .setItems(
                langs
            ) { _, lang ->
                changeLang(lang)
            }
            .create()

        binding.cvLanguage.setOnClickListener {
            dialog.show()
        }
    }

    private fun changeLang(lang: Int) {
        val targetLang = langs[lang]
        editor.putString("lang", targetLang)
        editor.commit()

        val config = resources.configuration
        val locale = Locale(targetLang)
        Locale.setDefault(locale)
        config.setLocale(locale)

        val newContext = context!!.createConfigurationContext(config)
        activity?.apply {
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
            recreate()
        }

    }

    private fun commitShared(state: Boolean) {
        editor.putBoolean("darkMode", state)
        editor.commit()
    }

    private fun changeTheme(state: Boolean) {
        AppCompatDelegate.setDefaultNightMode(if (state) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun updateSwitch(state: Boolean) {
        binding.switchDarkTheme.isChecked = state
    }


}