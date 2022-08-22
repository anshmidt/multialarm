package com.anshmidt.multialarm.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.repository.IRingtoneSettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SettingsViewModel(
        private val ringtoneSettingRepository: IRingtoneSettingRepository
) : ViewModel() {

    private var _chosenRingtoneName = MutableLiveData<String?>()
    val chosenRingtoneName: LiveData<String?> = _chosenRingtoneName

    fun onAudioFileChosen(sourceFileUri: Uri) {
        val sourceFileName = ringtoneSettingRepository.getRingtoneFileName(sourceFileUri)
        _chosenRingtoneName.value = sourceFileName

        //copy file to app folder
        viewModelScope.launch(Dispatchers.IO) {
            ringtoneSettingRepository.clearAllRingtones() // no need to store previously copied files
            val destinationFileUri = ringtoneSettingRepository.copyRingtoneToAppDirectory(sourceFileUri)
            ringtoneSettingRepository.saveRingtoneUri(destinationFileUri)
        }
    }

    fun onViewCreated() {

        viewModelScope.launch(Dispatchers.IO) {
            ringtoneSettingRepository.getRingtoneFileName().collect { ringtoneFileName ->
                _chosenRingtoneName.postValue(ringtoneFileName)
            }
        }
    }

}