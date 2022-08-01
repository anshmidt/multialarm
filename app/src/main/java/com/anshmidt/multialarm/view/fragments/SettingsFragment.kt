package com.anshmidt.multialarm.view.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.anshmidt.multialarm.R

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)

        setPreferencesFromResource(R.xml.preferences, rootKey)
        val context = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(context)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
//
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
    }



}


