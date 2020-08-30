package com.anshmidt.multialarm.data

import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

object TimeFormatter {

    fun getDisplayableTime(localTime: LocalTime): String {
        val format = DateTimeFormatter.ofPattern("HH:mm")
        val displayableTime = localTime.format(format)
        return displayableTime
    }

}