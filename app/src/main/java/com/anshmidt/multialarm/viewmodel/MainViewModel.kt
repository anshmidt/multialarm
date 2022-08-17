package com.anshmidt.multialarm.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anshmidt.multialarm.alarmscheduler.AlarmScheduler
import com.anshmidt.multialarm.data.SingleLiveEvent
import com.anshmidt.multialarm.repository.IScheduleSettingsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
        private val scheduleSettingsRepository: IScheduleSettingsRepository,
        private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    // used only for displaying the initial switch state when the view is created
    val alarmTurnedOn: Boolean
        get() = scheduleSettingsRepository.alarmTurnedOn

    private var _displayAlarmSwitchChangedMessage = SingleLiveEvent<Boolean>()
    val displayAlarmSwitchChangedMessage: LiveData<Boolean> = _displayAlarmSwitchChangedMessage

    private var _displayAlarmsResetMessage = SingleLiveEvent<Any>()
    val displayAlarmsResetMessage: LiveData<Any> = _displayAlarmsResetMessage

    private var subscriptions = CompositeDisposable()

    fun onAlarmSwitchChanged(switchView: View, switchState: Boolean) {
        _displayAlarmSwitchChangedMessage.value = switchState
        scheduleSettingsRepository.alarmTurnedOn = switchState
        alarmScheduler.reschedule(scheduleSettingsRepository.getSettings())
    }

    fun onViewStarted() {
        scheduleSettingsRepository.subscribeOnChangeListener()

        scheduleSettingsRepository.alarmsListObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onAlarmsListChanged()
                }, Throwable::printStackTrace)
                .let { subscriptions.add(it) }
    }

    fun onViewStopped() {
        scheduleSettingsRepository.unsubscribeOnChangeListener()
        if (!subscriptions.isDisposed) {
            subscriptions.clear()
        }
    }

    fun onViewDestroyed() {
        if (!subscriptions.isDisposed) {
            subscriptions.dispose()
        }
    }

    private fun onAlarmsListChanged() {
        _displayAlarmsResetMessage.call()
    }

}