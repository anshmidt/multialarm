package com.anshmidt.multialarm.view.activities

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.databinding.ActivityMainCardsBinding
import com.anshmidt.multialarm.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel




class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    private val binding: ActivityMainCardsBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainCardsBinding>(this, R.layout.activity_main_cards)
    }

    // Workaround to make onCheckedChanged not to trigger when switch state is set programmatically
    private var doListenForSwitch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        mainViewModel.onViewCreated()

        mainViewModel.displayAlarmSwitchChangedMessage.observe(this@MainActivity, {
            displayAlarmSwitchChangedToast(it)
        })

        mainViewModel.displayAlarmsResetMessage.observe(this@MainActivity, {
            displayAlarmsResetToast()
        })

        mainViewModel.alarmSwitchState.observe(this@MainActivity, {
            setSwitchState(it)
        })

        mainViewModel.isNightModeOn.observe(this@MainActivity, {
            setStatusBarTextColor(it)
        })

        binding.switchMain.setOnCheckedChangeListener { switchView, isChecked ->
            if (doListenForSwitch) {
                mainViewModel.onAlarmSwitchChanged(switchView, isChecked)
            }
        }
    }

    private fun addTintToBackgroundImage() {
        val myDrawable = ContextCompat.getDrawable(this, R.drawable.white_mountain_7)
        myDrawable?.setColorFilter(getColor(R.color.backgroundImageTint), PorterDuff.Mode.SRC_OVER)
    }

    private fun setSwitchState(switchState: Boolean) {
        doListenForSwitch = false
        binding.switchMain.isChecked = switchState
        doListenForSwitch = true
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
        binding.viewModel = mainViewModel
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

    private fun setStatusBarTextColor(isNightModeOn: Boolean) {
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = isNightModeOn.not()
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}