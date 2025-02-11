package com.anshmidt.multialarm.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.repository.IAppSettingRepository
import com.anshmidt.multialarm.repository.IRingtoneSettingRepository
import com.anshmidt.multialarm.view.helpers.AppThemeSelector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(
        private val ringtoneSettingRepository: IRingtoneSettingRepository,
        private val appSettingRepository: IAppSettingRepository,
        private val appThemeSelector: AppThemeSelector
) : ViewModel() {

    private var _chosenRingtoneName = MutableLiveData<String?>()
    val chosenRingtoneName: LiveData<String?> = _chosenRingtoneName

    private var _ringtoneDurationSeconds = MutableLiveData<Int>()
    val ringtoneDurationSeconds: LiveData<Int> = _ringtoneDurationSeconds

    private var _musicVolumePercents = MutableLiveData<Int>()
    val musicVolumePercents: LiveData<Int> = _musicVolumePercents

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
        viewModelScope.launch {
            ringtoneSettingRepository.clearAllRingtones() // no need to store previously copied files
            val destinationFileUri = ringtoneSettingRepository.copyRingtoneToAppDirectory(sourceFileUri)
            ringtoneSettingRepository.saveRingtoneUri(destinationFileUri)
        }
    }

    fun onViewCreated() {
        viewModelScope.launch {
            ringtoneSettingRepository.getRingtoneFileName()
                .first()
                .let { ringtoneFileName ->
                    _chosenRingtoneName.postValue(ringtoneFileName)
                }
        }

        viewModelScope.launch {
            ringtoneSettingRepository.getRingtoneDurationSeconds()
                .first()
                .let { ringtoneDurationSeconds ->
                    _ringtoneDurationSeconds.postValue(ringtoneDurationSeconds)
                }
        }

        viewModelScope.launch {
            appSettingRepository.getNightModeSwitchState()
                .first()
                .let { nightModeSwitchState ->
                    _isNightModeOn.postValue(nightModeSwitchState)
                }
        }

        viewModelScope.launch {
            ringtoneSettingRepository.getMusicVolumePercents()
                .first()
                .let { musicVolumePercents ->
                    _musicVolumePercents.postValue(musicVolumePercents)
                }
        }
    }

    fun onRingtoneDurationChosen(ringtoneDurationSeconds: Int) {
        viewModelScope.launch {
            _ringtoneDurationSeconds.postValue(ringtoneDurationSeconds)
            ringtoneSettingRepository.saveRingtoneDurationSeconds(ringtoneDurationSeconds)
        }
    }

    fun onTestAlarmPreferenceClicked() {
        _openDismissAlarmScreen.call()
        _startMusicService.call()
    }

    fun onNightModeSelectedByUser(isNightModeOn: Boolean) {
        appThemeSelector.showTheme(isNightModeOn)
        viewModelScope.launch {
            appSettingRepository.saveNightModeSwitchState(isNightModeOn)
        }
    }

    fun onMusicVolumeChosen(musicVolumePercents: Int) {
        viewModelScope.launch {
            ringtoneSettingRepository.saveMusicVolume(musicVolumePercents)
        }
    }


}