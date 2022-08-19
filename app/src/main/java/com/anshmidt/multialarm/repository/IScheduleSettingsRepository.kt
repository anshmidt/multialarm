package com.anshmidt.multialarm.repository

import com.anshmidt.multialarm.data.AlarmSettings
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalTime

interface IScheduleSettingsRepository {

    var alarmTurnedOn: Boolean
    var firstAlarmTime: LocalTime
    var minutesBetweenAlarms: Int
    var numberOfAlarms: Int
    val alarmsListObservable: Observable<List<LocalTime>>
    val alarmTurnedOnObservable: Observable<Boolean>

    fun clearAll()
    fun getSettings(): AlarmSettings

    fun subscribeOnChangeListener()
    fun unsubscribeOnChangeListener()

    suspend fun saveAlarmSwitchState(switchState: Boolean)
    fun getAlarmSwitchState(): Flow<Boolean>
}