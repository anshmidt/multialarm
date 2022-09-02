package com.anshmidt.multialarm.data

import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter

object TimeFormatter {

    @JvmStatic
    fun getDisplayableTime(localTime: LocalTime): String {
        val format = DateTimeFormatter.ofPattern("HH:mm")
        val displayableTime = localTime.format(format)
        return displayableTime
    }

    fun getDisplayableTimeLeft(alarmTime: LocalTime, currentTime: LocalTime): TimeLeft {
        val timeLeftDuration = Duration.between(currentTime, alarmTime)
        /**
         * Time left (before the alarm) must be always > 0 and no more that 24 h.
         * Since alarmTime and currentTime are LocalTime and not LocalDateTime,
         * normalization might be needed after performing between()
         */
        val normalizedTimeLeftDuration = normalizeDurationByAddingOrSubtractingDays(timeLeftDuration)
        return normalizedTimeLeftDuration.toTimeLeft()
    }

    /**
     * The precision of AlarmManager is ±2-3 minutes. That's why sometimes first alarm is 2-3 minutes
     * late. In these cases, the expected behavior is to show "0 minutes left" (and not a negative duration).
     */
    private fun normalizeDurationForDisplaying(duration: Duration): Duration {
        return if (duration.isNegative) {
            Duration.ZERO
        } else {
            duration
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

    fun Duration.toTimeLeft(): TimeLeft {
        val hours = this.toHours().toInt()
        val minutes = this.toMinutes().toInt() % 60
        return TimeLeft(hours = hours, minutes = minutes)
    }

    fun getFirstAlarmTimeMillis(firstAlarmTime: LocalTime): Long {
        val localDate = LocalDate.now()
        val localTimeDate = LocalDateTime.of(localDate, firstAlarmTime)
        val zoneId = ZoneId.systemDefault()
        val zonedDateTime = localTimeDate.atZone(zoneId)
        val normalizedZonedDateTime = normalizeAlarmTimeByAddingOrSubtractingDays(zonedDateTime)
        val millis = normalizedZonedDateTime.getMillis()
        return millis
    }

    private fun ZonedDateTime.getMillis(): Long {
        val seconds = this.toEpochSecond()
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