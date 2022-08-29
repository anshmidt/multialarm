package com.anshmidt.multialarm.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.view.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setTheme(R.style.SettingsFragmentTheme)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.framelayout_settings, SettingsFragment())
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}