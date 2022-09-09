package com.anshmidt.multialarm.data

import org.threeten.bp.LocalTime

data class Alarm(
    val time: LocalTime,
    val isEnabled: Boolean
)