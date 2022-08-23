package com.anshmidt.multialarm.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.ActivityMainBinding
import com.anshmidt.multialarm.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()

        mainViewModel.displayAlarmSwitchChangedMessage.observe(this@MainActivity, {
            displayAlarmSwitchChangedToast(it)
        })

        mainViewModel.displayAlarmsResetMessage.observe(this@MainActivity, {
            displayAlarmsResetToast()
        })
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.onViewStarted()
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.onViewStopped()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.onViewDestroyed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                openSettingsScreen()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
    }

    private fun openSettingsScreen() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun displayAlarmsOnToast() {
        Toast.makeText(
                this@MainActivity,
                getString(R.string.main_alarm_turned_on_toast),
                Toast.LENGTH_SHORT
        ).show()
    }

    private fun displayAlarmsOffToast() {
        Toast.makeText(
                this@MainActivity,
                getString(R.string.main_alarm_turned_off_toast),
                Toast.LENGTH_SHORT
        ).show()
    }

    private fun displayAlarmsResetToast() {
        Toast.makeText(
                this@MainActivity,
                getString(R.string.main_alarm_reset_toast),
                Toast.LENGTH_SHORT
        ).show()
    }

    private fun displayAlarmSwitchChangedToast(switchState: Boolean) {
        if (switchState) {
            displayAlarmsOnToast()
        } else {
            displayAlarmsOffToast()
        }
    }




}