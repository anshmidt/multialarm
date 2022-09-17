package com.anshmidt.multialarm.view.activities

import android.os.Bundle
import android.view.MenuItem
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

    /**
     * Overriding pressing Up button in order to change transition animation
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}