package com.anshmidt.multialarm.view.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.view.fragments.SettingsFragment
import com.anshmidt.multialarm.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setTheme(R.style.SettingsFragmentTheme)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.framelayout_settings, SettingsFragment())
                .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 2f

        viewModel.isNightModeOn.observe(this, {
            setStatusBarTextColor(it)
        })
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

    private fun setStatusBarTextColor(isNightModeOn: Boolean) {
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = isNightModeOn.not()
    }

}