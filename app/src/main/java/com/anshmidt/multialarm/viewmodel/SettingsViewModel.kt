package com.anshmidt.multialarm.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.repository.IRingtoneSettingRepository
import com.anshmidt.multialarm.repository.ISettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
        private val settingsRepository: ISettingsRepository,
        private val ringtoneSettingRepository: IRingtoneSettingRepository
) : ViewModel() {

    private var _chosenRingtoneName = MutableLiveData<String?>()
    val chosenRingtoneName: LiveData<String?> = _chosenRingtoneName

    fun onAudioFileChosen(sourceFileUri: Uri) {
        val sourceFileName = ringtoneSettingRepository.getRingtoneFileName(sourceFileUri)
        _chosenRingtoneName.value = sourceFileName

        //copy file to app folder
        CoroutineScope(Dispatchers.IO).launch {
            ringtoneSettingRepository.clearAllRingtones() // no need to store previously copied files
            val destinationFileUri = ringtoneSettingRepository.copyRingtoneToAppDirectory(sourceFileUri)
            ringtoneSettingRepository.songUri = destinationFileUri
        }
    }

    fun onViewCreated() {
        _chosenRingtoneName.value = ringtoneSettingRepository.getRingtoneFileName()
    }

}