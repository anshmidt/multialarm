package com.anshmidt.multialarm.repository

import android.net.Uri
import com.anshmidt.multialarm.data.AlarmSettings
import com.anshmidt.multialarm.data.SingleLiveEvent
import io.reactivex.Observable
import org.threeten.bp.LocalTime
import java.io.File

interface ISettingsRepository {
    var alarmTurnedOn: Boolean
    var firstAlarmTime: LocalTime
    var minutesBetweenAlarms: Int
    var numberOfAlarms: Int
    var songDurationSeconds: Int
    fun clearAll()
    var songUri: Uri
    fun getSettings(): AlarmSettings
    fun getRingtoneFileName(): String?
    fun getRingtoneFileName(uri: Uri): String?

    val alarmsListObservable: Observable<List<LocalTime>>
    val alarmTurnedOnObservable: Observable<Boolean>
    fun subscribeOnChangeListener()
    fun unsubscribeOnChangeListener()
}