package com.anshmidt.multialarm.data

fun AlarmSettings.getAlarmsList(): List<AlarmListEntry> {
    val alarmsList: MutableList<AlarmListEntry> = mutableListOf()
    val firstAlarmTimeMillis = this.firstAlarmTimeMillis
    var alarmTime = TimeFormatter.getLocalTime(timeMillis = firstAlarmTimeMillis)

    for (i in 0..this.numberOfAlarms - 1 ) {
        alarmsList.add(
            AlarmListEntry(
                time = alarmTime,
                isEnabled = isAlarmEnabled(alarmPosition = i)
            )
        )
        alarmTime = alarmTime.plusMinutes(this.minutesBetweenAlarms.toLong())
    }
    return alarmsList
}

/**
 * Returns time of the next upcoming alarm,
 * or null if there is no next alarm.
 */
fun AlarmSettings.getNextAlarmTimeMillis(): Long? {
    if (isThereNextAlarm().not()) return null
    val firstAlarmTimeMillis = this.firstAlarmTimeMillis
    val minutesSinceFirstAlarm =
        this.numberOfAlreadyRangAlarms * this.minutesBetweenAlarms
    val millisSinceFirstAlarm = minutesSinceFirstAlarm * 60 * 1000
    val nextAlarmMillis = firstAlarmTimeMillis + millisSinceFirstAlarm
    return nextAlarmMillis
}

/**
 * Returns true if alarms are on and not all alarms have rang
 */
fun AlarmSettings.isThereNextAlarm(): Boolean {
    if (this.areOn.not()) return false
    return (this.numberOfAlreadyRangAlarms < this.numberOfAlarms)
}

fun AlarmSettings.isAlarmEnabled(alarmPosition: Int): Boolean {
    if (this.areOn.not()) return false
    return (alarmPosition >= this.numberOfAlreadyRangAlarms)
}
