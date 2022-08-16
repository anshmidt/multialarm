package com.anshmidt.multialarm.view.fragments

import android.Manifest
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.anshmidt.multialarm.R
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts

import com.anshmidt.multialarm.repository.ISettingsRepository
import org.koin.android.ext.android.inject
import com.anshmidt.multialarm.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SettingsFragment : PreferenceFragmentCompat() {
    lateinit var sharedPreferences: SharedPreferences
    private val viewModel: SettingsViewModel by sharedViewModel()

    private val ringtoneFilenamePreference: Preference? by lazy {
        val ringtonePreferenceKey = getString(R.string.key_ringtone_filename)
        findPreference(ringtonePreferenceKey)
    }

    private val openAudioFileResult = registerForActivityResult(
            ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.onAudioFileChosen(it) }
    }

    /**\
     * TODO: I should have RingtoneRepository, IRingtoneRepository, SettingsViewModel,
     * SharedPreferencesDataSource, FileDataSource
     *
     * Maybe I should also migrate to Jetpack DataStore from Shared Preferences
     *
     * Naming is bad: SettingsRepository vs SettingsFragment
     *
     * Idea of naming:
     * RingtoneSettingRepository, AlarmSettingRepository
     * or
     * ScheduleSettingRepository, RingtoneSettingRepository (duration, filename)
     * RingtoneStorage, SettingStorage  (data sources)
     * And if I need to: SharedPreferencesManager, FileManager
     */

    private fun openAudioFileChooser() {
        openAudioFileResult.launch("audio/*")
    }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        setPreferencesFromResource(R.xml.preferences, rootKey)

        ringtoneFilenamePreference?.setOnPreferenceClickListener {
            onRingtonePreferenceClicked()
            return@setOnPreferenceClickListener true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel.chosenRingtoneName.observe(viewLifecycleOwner, {
            displayRingtoneName(it)
        })

        viewModel.onViewCreated()

        return super.onCreateView(inflater, container, savedInstanceState)
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

    companion object {
        val TAG = SettingsFragment::class.java.simpleName
        private const val PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE = 23
    }









}


