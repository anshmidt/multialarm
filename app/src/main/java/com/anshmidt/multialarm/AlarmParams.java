package com.anshmidt.multialarm;

/**
 * Created by Ilya Anshmidt on 27.10.2017.
 */

public class AlarmParams {

    public boolean turnedOn;
    public AlarmTime firstAlarmTime;

    public int interval;
    public int numberOfAlarms;

    public AlarmParams(boolean turnedOn, AlarmTime firstAlarmTime, int interval, int numberOfAlarms) {
        this.turnedOn = turnedOn;
        this.firstAlarmTime = firstAlarmTime;
        this.interval = interval;
        this.numberOfAlarms = numberOfAlarms;
    }
}
