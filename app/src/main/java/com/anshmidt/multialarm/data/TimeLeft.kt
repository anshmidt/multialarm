package com.anshmidt.multialarm.data

/**
 * Represents time left from current moment to the time of the alarm.
 * For that reason, it represents a duration > 0, and < 24 hours.
 */
data class TimeLeft(
        val hours: Int,
        val minutes: Int
)