package com.anshmidt.multialarm.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

object TimeFormatter {

    fun getDisplayableTime(localTime: LocalTime): String {
        val format = DateTimeFormatter.ofPattern("HH:mm")
        val displayableTime = localTime.format(format)
        return displayableTime
    }

    fun getTimeLeft(alarmTime: LocalTime): Duration {
        val currentTime = LocalTime.now()
        val durationFromNowToAlarmTime = Duration.between(currentTime, alarmTime)
        return durationFromNowToAlarmTime
    }


    fun getTimeLeft(alarmTime: LocalTime, currentTime: LiveData<LocalTime>): LiveData<Duration> {
        return Transformations.map(currentTime) {
            Duration.between(it, alarmTime)
        }
    }

    fun getTimeLeft(alarmTime: LiveData<LocalTime>, currentTime: LocalTime): LiveData<Duration> {
        return Transformations.map(alarmTime) {
            Duration.between(currentTime, it)
        }
    }

}