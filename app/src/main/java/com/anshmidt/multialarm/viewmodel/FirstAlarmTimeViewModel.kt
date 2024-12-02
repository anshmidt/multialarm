package com.anshmidt.multialarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.data.TimeFormatter
import com.anshmidt.multialarm.data.TimeLeft
import com.anshmidt.multialarm.logging.Log
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import org.threeten.bp.LocalTime
import java.util.concurrent.TimeUnit

class FirstAlarmTimeViewModel(
        private val scheduleSettingsRepository: IScheduleSettingsRepository,
        private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private var _firstAlarmTime = MutableLiveData<LocalTime>()
    val firstAlarmTime: LiveData<LocalTime> = _firstAlarmTime

    private var _timeLeft = MutableLiveData<TimeLeft>()
    val timeLeft: LiveData<TimeLeft> = _timeLeft

    private var _shouldShowTimeLeftOnMainScreen = MutableLiveData<Boolean>()
    val shouldShowTimeLeftOnMainScreen: LiveData<Boolean> = _shouldShowTimeLeftOnMainScreen

    private var _isTimeLeftEnabled = MutableLiveData<Boolean>()
    val isTimeLeftEnabled: LiveData<Boolean> = _isTimeLeftEnabled

    private val firstAlarmMillisSelectedByUserFlow = MutableStateFlow<Long?>(null)

    private val _openFirstAlarmTimeDialog = SingleLiveEvent<Any>()
    val openFirstAlarmTimeDialog: LiveData<Any>
        get() = _openFirstAlarmTimeDialog

    fun onViewResumed() {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSettings().first { alarmSettings ->
                val firstAlarmTimeMillis = alarmSettings.firstAlarmTimeMillis
                val firstAlarmTime = TimeFormatter.getLocalTime(timeMillis = firstAlarmTimeMillis)
                _firstAlarmTime.postValue(firstAlarmTime)
                return@first true
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val firstAlarmMillisFlow: Flow<Long> = merge(
                scheduleSettingsRepository.getAlarmSettings().map { it.firstAlarmTimeMillis },
                firstAlarmMillisSelectedByUserFlow.filterNotNull()
            )
            timeLeftFlow(
                firstAlarmMillisFlow = firstAlarmMillisFlow,
                tickerFlow = tickerEverySecondFlow()
            ).collect { timeLeft ->
                _timeLeft.postValue(timeLeft)
            }
        }

        /**
         * Time left to the first alarm doesn't make sense if it's already off. That's why we hide
         * time left in this case.
         */
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSettings().collect { alarmSettings ->
                val shouldShowTimeLeftOnMainScreen = shouldShowTimeLeftOnMainScreen(
                    numberOfAlreadyRangAlarms = alarmSettings.numberOfAlreadyRangAlarms,
                    switchState = alarmSettings.areOn
                )
                _shouldShowTimeLeftOnMainScreen.postValue(shouldShowTimeLeftOnMainScreen)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSettings().collect { alarmSettings ->
                _isTimeLeftEnabled.postValue(alarmSettings.areOn)
            }
        }
    }

    private fun shouldShowTimeLeftOnMainScreen(numberOfAlreadyRangAlarms: Int, switchState: Boolean): Boolean {
        return if ((numberOfAlreadyRangAlarms > 0) && (switchState == true)) {
            false
        } else {
            true
        }
    }

    fun onFirstAlarmTimeClicked() {
        _openFirstAlarmTimeDialog.call()
    }

    fun onOkButtonClickInFirstAlarmDialog() {
        firstAlarmMillisSelectedByUserFlow.value?.let { firstAlarmMillisSelectedByUser ->
            val firstAlarmTime = TimeFormatter.getLocalTime(timeMillis = firstAlarmMillisSelectedByUser)
            _firstAlarmTime.postValue(firstAlarmTime)

            viewModelScope.launch(Dispatchers.IO) {
                scheduleSettingsRepository.getAlarmSettings().first { alarmSettings ->
                    val newAlarmSettings = alarmSettings.copy(firstAlarmTimeMillis = firstAlarmMillisSelectedByUser)
                    Log.d(TAG, "Rescheduling alarm because first alarm time changed by user")
                    alarmScheduler.rescheduleAlarms(newAlarmSettings)
                    scheduleSettingsRepository.saveAlarmSettings(newAlarmSettings)
                    return@first true
                }
            }
        }
    }

    fun onCancelButtonClickInFirstAlarmDialog() {
        // Clicking Cancel effectively means that we ignore any value user set, and use a value saved
        // in repository instead.
        viewModelScope.launch(Dispatchers.IO) {
            scheduleSettingsRepository.getAlarmSettings()
                .map { it.firstAlarmTimeMillis }
                .first { firstAlarmTimeFromRepository ->
                    firstAlarmMillisSelectedByUserFlow.emit(firstAlarmTimeFromRepository)
                    return@first true
                }
        }
    }

    fun onFirstAlarmTimeSelectedOnPicker(hour: Int, minute: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val selectedMillis = TimeFormatter.getAlarmTimeWithin24HoursMillis(
                alarmTime = LocalTime.of(hour, minute)
            )
            firstAlarmMillisSelectedByUserFlow.emit(selectedMillis)
        }
    }

    private fun tickerEverySecondFlow() = flow {
        val initialDelay = 0L
        val period = TimeUnit.SECONDS.toMillis(1)
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

    private fun timeLeftFlow(firstAlarmMillisFlow: Flow<Long>, tickerFlow: Flow<Unit>): Flow<TimeLeft> {
        return firstAlarmMillisFlow.combine(tickerFlow) { firstAlarmMillis, _ ->
            firstAlarmMillis
        }.map { firstAlarmMillis ->
            TimeFormatter.getDisplayableTimeLeft(
                alarmTimeMillis = firstAlarmMillis,
                currentTimeMillis = System.currentTimeMillis()
            )
        }
    }

    companion object {
        private val TAG = FirstAlarmTimeViewModel::class.java.simpleName
    }

}