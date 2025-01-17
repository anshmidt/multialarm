package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.repository.LogRepository
import kotlinx.coroutines.launch

class LogViewModel(
    private val logRepository: LogRepository
) : ViewModel() {

    private var _logText = MutableLiveData<List<String>>()
    val logText: MutableLiveData<List<String>> = _logText

    fun onViewCreated() {
        viewModelScope.launch {
            val log = logRepository.getLog()
            _logText.postValue(log)
        }
    }
}