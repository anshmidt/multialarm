package com.anshmidt.multialarm.data

import org.threeten.bp.LocalTime

data class AlarmListEntry(
    val time: LocalTime,
    val isEnabled: Boolean
)