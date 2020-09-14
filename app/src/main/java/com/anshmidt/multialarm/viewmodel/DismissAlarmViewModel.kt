package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.IMusicPlayer
import com.anshmidt.multialarm.repository.IAlarmSettingsRepository

class DismissAlarmViewModel (
        private val repository: IAlarmSettingsRepository,
        private val musicPlayer: IMusicPlayer
) : ViewModel() {

    fun onViewCreated() {
        musicPlayer.play()
    }

    fun onDismissButtonClicked() {
        musicPlayer.stop()
    }

}