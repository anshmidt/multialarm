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

//    /**
//     * The precision of AlarmManager is ±2-3 minutes. That's why sometimes first alarm is 2-3 minutes
//     * late. In these cases, the expected behavior is to show "0 minutes left" (and not a negative duration).
//     */
//    private fun normalizeDurationForDisplaying(duration: Duration): Duration {
//        return if (duration.isNegative) {
//            Duration.ZERO
//        } else {
//            duration
//        }
//    }

    private fun Duration.toTimeLeft(): TimeLeft {
        val hours = this.toHours().toInt()
        val minutes = this.toMinutes().toInt() % 60
        return TimeLeft(hours = hours, minutes = minutes)
    }

    fun getAlarmTimeWithin24HoursMillis(alarmTime: LocalTime): Long {
        val localDate = LocalDate.now()
        val localTimeDate = LocalDateTime.of(localDate, alarmTime)
        val zoneId = ZoneId.systemDefault()
        val zonedDateTime = localTimeDate.atZone(zoneId)
        val normalizedZonedDateTime = normalizeAlarmTimeByAddingOrSubtractingDays(zonedDateTime)
        val millis = normalizedZonedDateTime.getMillis()
        return millis
    }

    fun getLocalTime(timeMillis: Long, zoneId: ZoneId = ZoneId.systemDefault()): LocalTime {
        return Instant.ofEpochMilli(timeMillis)
            .atZone(zoneId)
            .toLocalTime()
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