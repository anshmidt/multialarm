package com.anshmidt.multialarm.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.repository.IAppSettingRepository
import com.anshmidt.multialarm.repository.IRingtoneSettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(
        private val ringtoneSettingRepository: IRingtoneSettingRepository,
        private val appSettingRepository: IAppSettingRepository
) : ViewModel() {

    private var _chosenRingtoneName = MutableLiveData<String?>()
    val chosenRingtoneName: LiveData<String?> = _chosenRingtoneName

    private var _ringtoneDurationSeconds = MutableLiveData<Int>()
    val ringtoneDurationSeconds: LiveData<Int> = _ringtoneDurationSeconds

    private var _openDismissAlarmScreen = SingleLiveEvent<Any>()
    val openDismissAlarmScreen: LiveData<Any> = _openDismissAlarmScreen

    private var _startMusicService = SingleLiveEvent<Any>()
    val startMusicService: LiveData<Any> = _startMusicService

    private var _isNightModeOn = MutableLiveData<Boolean>()
    val isNightModeOn: LiveData<Boolean> = _isNightModeOn

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
            ringtoneSettingRepository.getRingtoneFileName().first { ringtoneFileName ->
                _chosenRingtoneName.postValue(ringtoneFileName)
                return@first true
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            ringtoneSettingRepository.getRingtoneDurationSeconds().first { ringtoneDurationSeconds ->
                _ringtoneDurationSeconds.postValue(ringtoneDurationSeconds)
                return@first true
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            appSettingRepository.getNightModeSwitchState().first { nightModeSwitchState ->
                _isNightModeOn.postValue(nightModeSwitchState)
                return@first true
            }
        }
    }

    fun onRingtoneDurationChosen(ringtoneDurationSeconds: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _ringtoneDurationSeconds.postValue(ringtoneDurationSeconds)
            ringtoneSettingRepository.saveRingtoneDurationSeconds(ringtoneDurationSeconds)
        }
    }

    fun onTestAlarmPreferenceClicked() {
        _openDismissAlarmScreen.call()
        _startMusicService.call()
    }

    fun onNightModeSelected(isNightModeOn: Boolean) {
        _isNightModeOn.postValue(isNightModeOn)
        viewModelScope.launch(Dispatchers.IO) {
            appSettingRepository.saveNightModeSwitchState(isNightModeOn)
        }
    }


}