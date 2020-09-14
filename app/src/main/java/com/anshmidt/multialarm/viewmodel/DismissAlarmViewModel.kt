package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.repository.IAlarmSettingsRepository

class DismissAlarmViewModel (
        private val repository: IAlarmSettingsRepository
) : ViewModel() {

}