package com.anshmidt.multialarm.view.fragments

import android.Manifest
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.MediaStore
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.anshmidt.multialarm.R
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts

import com.anshmidt.multialarm.repository.FileExtensions.copyToAppDir
import com.anshmidt.multialarm.repository.ISettingsRepository
import org.koin.android.ext.android.inject
import com.anshmidt.multialarm.repository.FileExtensions.getFileName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    lateinit var sharedPreferences: SharedPreferences
    private val settingsRepository: ISettingsRepository by inject()

    val ringtoneFilenamePreference: Preference? by lazy {
        val ringtonePreferenceKey = getString(R.string.key_ringtone_filename)
        findPreference(ringtonePreferenceKey)
    }

    companion object {
        val TAG = SettingsFragment::class.java.simpleName
        private const val PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE = 23
        private const val FILE_CHOOSER_REQUEST_CODE = 202
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
     * RingtoneStorage, SettingStorage  (data sources)
     * And if I need to: SharedPreferencesManager, FileManager
     */

    private fun openActivityForResult() {
        openAudioFileResult.launch("audio/*")
    }


    val openAudioFileResult = registerForActivityResult(
            ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onAudioFileChosen(it) }
    }

    private fun onAudioFileChosen(sourceFileUri: Uri) {
        val sourceFileName = sourceFileUri.getFileName(requireContext())
        displayRingtoneName(sourceFileName)

        //copy file to app folder
        CoroutineScope(Dispatchers.IO).launch {
            clearRingtonesDir() // no need to store previously copied files
            val destinationFileUri = sourceFileUri.copyToAppDir(requireContext())
            settingsRepository.songUri = destinationFileUri
        }
    }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)

        setPreferencesFromResource(R.xml.preferences, rootKey)

//        val ringtonePreferenceKey = getString(R.string.key_ringtone_filename)
//        val ringtoneFilenamePreference: Preference? = findPreference(ringtonePreferenceKey)
        ringtoneFilenamePreference?.setOnPreferenceClickListener {
            onRingtonePreferenceClicked()
            return@setOnPreferenceClickListener true
        }

        val ringtoneFileName = settingsRepository.songUri.getFileName(requireContext())
        displayRingtoneName(ringtoneFileName)

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

    private fun openFileChooser() {
        openActivityForResult()
    }

    private fun onRingtonePreferenceClicked() {
        // request permissions if user didn't grant them before
        if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE)
        } else {
            Log.d(TAG, "Permission already granted")
            openFileChooser()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE -> {

                // If request is cancelled, the result array is empty.
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission granted")
                    openFileChooser()
                } else {
                    Log.d(TAG, "Permission denied")
                }
            }
        }
    }

    private fun clearRingtonesDir() {
        requireContext().filesDir.deleteRecursively()
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

    private fun getMediaFileName(uri: Uri): String? {
        val projection = arrayOf(
                MediaStore.Audio.Media.DISPLAY_NAME)
        requireContext().contentResolver.query(
                uri, projection, null, null, null, null)?.use { cursor ->
            //cache column indices
            val nameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)

            //iterating over all of the found images
            while (cursor.moveToNext()) {
                return cursor.getString(nameColumn)
            }
            return null
        }
        return null
    }







}


