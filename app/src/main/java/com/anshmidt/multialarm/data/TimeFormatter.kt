package com.anshmidt.multialarm.data

import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

object TimeFormatter {

    @JvmStatic
    fun getDisplayableTime(localTime: LocalTime): String {
        val format = DateTimeFormatter.ofPattern("HH:mm")
        val displayableTime = localTime.format(format)
        return displayableTime
    }

    fun getDisplayableTimeLeft(alarmTimeMillis: Long, currentTimeMillis: Long): TimeLeft {
        val timeLeftDuration = Duration.ofMillis(alarmTimeMillis - currentTimeMillis)
        return timeLeftDuration.toTimeLeft()
    }

    private fun Duration.toTimeLeft(): TimeLeft {
        val hours = this.toHours().toInt()
        val minutes = this.toMinutes().toInt() % 60
        return TimeLeft(hours = hours, minutes = minutes)
    }

    fun getAlarmMillisWithin24Hours(alarmTime: LocalTime): Long {
        val localDate = LocalDate.now()
        val localTimeDate = LocalDateTime.of(localDate, alarmTime)
        val zoneId = ZoneId.systemDefault()
        val zonedDateTime = localTimeDate.atZone(zoneId)
        val normalizedZonedDateTime = normalizeAlarmTimeByAddingOrSubtractingDays(zonedDateTime)
        val millis = normalizedZonedDateTime.getMillis()
        return millis
    }

    fun getAlarmMillisWithin24Hours(alarmMillis: Long): Long {
        val alarmTime = getLocalTime(alarmMillis)
        return getAlarmMillisWithin24Hours(alarmTime)
    }

    fun getLocalTime(timeMillis: Long, zoneId: ZoneId = ZoneId.systemDefault()): LocalTime {
        return Instant.ofEpochMilli(timeMillis)
            .atZone(zoneId)
            .toLocalTime()
    }

    fun getLocalDateTime(timeMillis: Long): LocalDateTime {
        return Instant.ofEpochMilli(timeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    private fun ZonedDateTime.getMillis(): Long {
        val seconds = this.toEpochSecond()
        val millis = seconds * 1000
        return millis
    }

    private fun normalizeAlarmTimeByAddingOrSubtractingDays(zonedDateTime: ZonedDateTime): ZonedDateTime {
        var normalizedZonedDateTime = zonedDateTime
        // alarm is always in the future
        while (normalizedZonedDateTime < ZonedDateTime.now()) {
            normalizedZonedDateTime = normalizedZonedDateTime.plusDays(1)
        }
        // alarm must not be more than 24 hours in the future
        while (normalizedZonedDateTime > ZonedDateTime.now().plusDays(1)) {
            normalizedZonedDateTime = normalizedZonedDateTime.minusDays(1)
        }
        return normalizedZonedDateTime
    }




}