package com.anshmidt.multialarm.repository

import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.AlarmsConverter
import com.anshmidt.multialarm.datasources.SharedPreferencesStorage
import io.reactivex.subjects.BehaviorSubject
import org.threeten.bp.LocalTime

class ScheduleSettingsRepository(
        private val sharedPreferencesStorage: SharedPreferencesStorage
) : IScheduleSettingsRepository {

    override var alarmTurnedOn: Boolean
        get() = sharedPreferencesStorage.alarmTurnedOn
        set(alarmState) {
            alarmTurnedOnObservable.onNext(alarmState)
            sharedPreferencesStorage.alarmTurnedOn = alarmState
        }

    override var firstAlarmTime: LocalTime
        get() {
            val hours = sharedPreferencesStorage.firstAlarmHours
            val minutes = sharedPreferencesStorage.firstAlarmMinutes
            return LocalTime.of(hours, minutes)
        }
        set(value) {
            sharedPreferencesStorage.firstAlarmHours = value.hour
            sharedPreferencesStorage.firstAlarmMinutes = value.minute
        }

    override var minutesBetweenAlarms: Int
        get() = sharedPreferencesStorage.minutesBetweenAlarms
        set(value) {
            sharedPreferencesStorage.minutesBetweenAlarms = value
        }

    override var numberOfAlarms: Int
        get() = sharedPreferencesStorage.numberOfAlarms
        set(value) {
            sharedPreferencesStorage.numberOfAlarms = value
        }

    override val alarmsListObservable = sharedPreferencesStorage.alarmSettingsChangedObservable
            .map { getAlarmsList() }

    override val alarmTurnedOnObservable = BehaviorSubject.createDefault(alarmTurnedOn)

    override fun subscribeOnChangeListener() {
        sharedPreferencesStorage.subscribeOnChangeListener()
    }

    override fun unsubscribeOnChangeListener() {
        sharedPreferencesStorage.unsubscribeOnChangeListener()
    }

    override fun clearAll() {
        sharedPreferencesStorage.clearAll()
    }

    override fun getSettings(): AlarmSettings {
        return AlarmSettings(
                turnedOn = alarmTurnedOn,
                firstAlarmTime = firstAlarmTime,
                minutesBetweenAlarms = minutesBetweenAlarms,
                numberOfAlarms = numberOfAlarms
        )
    }

    private fun getAlarmsList(): List<LocalTime> {
        return AlarmsConverter.getAlarmsList(
                firstAlarmTime = firstAlarmTime,
                numberOfAlarms = numberOfAlarms,
                minutesBetweenAlarms = minutesBetweenAlarms
        )
    }



}