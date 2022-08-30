package com.anshmidt.multialarm.view.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.services.MusicService
import com.anshmidt.multialarm.view.activities.DismissAlarmActivity
import com.anshmidt.multialarm.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SettingsFragment : PreferenceFragmentCompat() {
    private val viewModel: SettingsViewModel by sharedViewModel()

    private val ringtoneFilenamePreference: Preference? by lazy {
        val ringtonePreferenceKey = getString(R.string.key_ringtone_filename)
        findPreference(ringtonePreferenceKey)
    }

    private val ringtoneDurationPreference: ListPreference? by lazy {
        val ringtoneDurationKey = getString(R.string.key_duration)
        findPreference(ringtoneDurationKey) as ListPreference?
    }

    private val testAlarmPreference: Preference? by lazy {
        val testAlarmPreferenceKey = getString(R.string.key_test_alarm)
        findPreference(testAlarmPreferenceKey)
    }

    private val nightModePreference: SwitchPreference? by lazy {
        val nightModeKey = getString(R.string.key_night_mode)
        findPreference(nightModeKey)
    }

    private val openAudioFileResult = registerForActivityResult(
            ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.onAudioFileChosen(it) }
    }

    private fun openAudioFileChooser() {
        openAudioFileResult.launch("audio/*")
    }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        ringtoneFilenamePreference?.setOnPreferenceClickListener {
            onRingtonePreferenceClicked()
            return@setOnPreferenceClickListener true
        }

        ringtoneDurationPreference?.setOnPreferenceChangeListener { preference, newValue ->
            val newRingtoneDurationSeconds = (newValue as String).toInt()
            onRingtoneDurationChosen(newRingtoneDurationSeconds)
            return@setOnPreferenceChangeListener true
        }

        testAlarmPreference?.setOnPreferenceClickListener {
            onTestAlarmPreferenceClicked()
            return@setOnPreferenceClickListener true
        }

        nightModePreference?.setOnPreferenceChangeListener { preference, newValue ->
            val isNightModeOn = newValue as Boolean
            onNightModeSelected(isNightModeOn)
            return@setOnPreferenceChangeListener true
        }
    }

    private fun onTestAlarmPreferenceClicked() {
        viewModel.onTestAlarmPreferenceClicked()
    }

    private fun onNightModeSelected(isNightModeOn: Boolean) {
        viewModel.onNightModeSelectedByUser(isNightModeOn)
    }

    private fun onRingtoneDurationChosen(ringtoneDurationSeconds: Int) {
        viewModel.onRingtoneDurationChosen(ringtoneDurationSeconds)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        viewModel.chosenRingtoneName.observe(viewLifecycleOwner, {
            displayRingtoneName(it)
        })

        viewModel.ringtoneDurationSeconds.observe(viewLifecycleOwner, {
            displayRingtoneDurationSeconds(it)
        })

        viewModel.openDismissAlarmScreen.observe(viewLifecycleOwner, {
            openDismissAlarmScreen()
        })

        viewModel.startMusicService.observe(viewLifecycleOwner, {
            startMusicService()
        })

        viewModel.isNightModeOn.observe(viewLifecycleOwner, {
            displayNightModeSwitchState(it)
        })

        viewModel.onViewCreated()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun displayRingtoneDurationSeconds(ringtoneDurationSeconds: Int) {
        ringtoneDurationPreference?.value = ringtoneDurationSeconds.toString()
    }

    private fun displayNightModeSwitchState(nightModeSwitchState: Boolean) {
        nightModePreference?.setDefaultValue(nightModeSwitchState)
    }

    private fun onRingtonePreferenceClicked() {
        // request permissions if user didn't grant them before
        if (areStoragePermissionsGranted()) {
            Log.d(TAG, "Permission already granted")
            onStoragePermissionsGranted()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE)
        }
    }

    private fun areStoragePermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission granted")
                    onStoragePermissionsGranted()
                } else {
                    Log.d(TAG, "Permission denied")
                }
            }
        }
    }

    private fun onStoragePermissionsGranted() {
        openAudioFileChooser()
    }

    private fun displayRingtoneName(ringtoneName: String?) {
        if (ringtoneName.isNullOrEmpty()) {
            displayDefaultRingtoneName()
        } else {
            ringtoneFilenamePreference?.summary = ringtoneName
        }
    }

    private fun displayDefaultRingtoneName() {
        val defaultRingtoneName = getString(R.string.preferences_default_ringtone_name)
        ringtoneFilenamePreference?.summary = defaultRingtoneName
    }

    private fun openDismissAlarmScreen() {
        val activityIntent = Intent(context, DismissAlarmActivity::class.java)
        requireContext().startActivity(activityIntent)
    }

    private fun startMusicService() {
        // We don't show notification since we the whole DismissAlarm screen is opened
        val intent = Intent(context, MusicService::class.java)
        val shouldShowNotification = false
        intent.putExtra(MusicService.INTENT_KEY_SHOULD_SHOW_NOTIFICATION, shouldShowNotification)
        requireContext().startService(intent)
    }

    companion object {
        val TAG = SettingsFragment::class.java.simpleName
        private const val PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE = 23
    }









}


