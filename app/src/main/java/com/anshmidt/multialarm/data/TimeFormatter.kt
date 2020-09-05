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


    fun getTimeLeft(alarmTime: LiveData<LocalTime>, currentTime: LocalTime): LiveData<Duration> {
        return Transformations.map(alarmTime) {
            /**
             * Time left (before the alarm) must be always > 0 and no more that 24 h.
             * Since alarmTime and currentTime are LocalTime and not LocalDateTime,
             * normalization might be needed after performing between()
             */
            normalizeDurationByAddingOrSubtractingDays(Duration.between(currentTime, it))
        }
    }

    fun normalizeDurationByAddingOrSubtractingDays(duration: Duration): Duration {
        var normalizedDuration = duration
        while (normalizedDuration.isNegative) {
            normalizedDuration = normalizedDuration.plusDays(1)
        }
        while (normalizedDuration.compareTo(Duration.ofDays(1)) > 0) {
            normalizedDuration = normalizedDuration.minusDays(1)
        }
        return normalizedDuration
    }

    fun getHours(duration: LiveData<Duration>): LiveData<Int> {
        return Transformations.map(duration) {
            it.toHours().toInt()
        }
    }

    fun getMinutesPart(duration: LiveData<Duration>): LiveData<Int> {
        return Transformations.map(duration) {
            val allDurationInMinutes = it.toMinutes()
            val minutesPart = allDurationInMinutes % 60
            minutesPart.toInt()
        }
    }

}