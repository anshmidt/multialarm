package com.anshmidt.multialarm.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

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

    fun getFirstAlarmTimeMillis(firstAlarmTime: LocalTime): Long {
        val localDate = LocalDate.now()
        val localTimeDate = LocalDateTime.of(localDate, firstAlarmTime)
        val zoneId = ZoneId.systemDefault()
        val zonedDateTime = localTimeDate.atZone(zoneId)
        val normalizedZonedDateTime = normalizeAlarmTimeByAddingOrSubtractingDays(zonedDateTime)
        val millis = getMillisFromZonedDateTime(normalizedZonedDateTime)
        return millis
    }

    private fun getMillisFromZonedDateTime(zonedDateTime: ZonedDateTime): Long {
        val seconds = zonedDateTime.toEpochSecond()
        val millis = seconds * 1000
        return millis
    }

    private fun normalizeAlarmTimeByAddingOrSubtractingDays(zonedDateTime: ZonedDateTime): ZonedDateTime {
        var normalizedZonedDateTime = zonedDateTime
        while (normalizedZonedDateTime < ZonedDateTime.now()) { // alarm is always in the future
            normalizedZonedDateTime = normalizedZonedDateTime.plusDays(1)
        }
        while (normalizedZonedDateTime > ZonedDateTime.now().plusDays(1)) { // alarm must not be more than 24 hours in the future
            normalizedZonedDateTime = normalizedZonedDateTime.minusDays(1)
        }
        return normalizedZonedDateTime
    }




}