package com.anshmidt.multialarm.repository

import android.net.Uri
import com.anshmidt.multialarm.data.AlarmSettings
import io.reactivex.Observable
import org.threeten.bp.LocalTime

interface ISettingsRepository {
    var alarmTurnedOn: Boolean
    var firstAlarmTime: LocalTime
    var minutesBetweenAlarms: Int
    var numberOfAlarms: Int
    var songDurationSeconds: Int
    fun clearAll()
    val songUri: Uri
    fun getSettings(): AlarmSettings

    fun firstAlarmTimeObservable(): Observable<LocalTime>
    fun subscribeOnChangeListener()
    fun unsubscribeOnChangeListener()
}