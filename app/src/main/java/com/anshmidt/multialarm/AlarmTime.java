package com.anshmidt.multialarm;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ilya Anshmidt on 13.10.2017.
 */

public class AlarmTime {

    private int minute;
    private int hour;

    public AlarmTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public long toMillis() {
        long currentTimeMillis = Calendar.getInstance().getTimeInMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        long alarmTimeMillis = calendar.getTimeInMillis();

        // alarm will be set to the next 24 h
        long timeDifferenceMillis = alarmTimeMillis - currentTimeMillis;
        if (timeDifferenceMillis < 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (timeDifferenceMillis > TimeUnit.DAYS.toMillis(1)) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        return calendar.getTimeInMillis();
    }

    public long toNextDayMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis();
    }


    public String getTimeLeftString() {
        long currentTimeMillis = Calendar.getInstance().getTimeInMillis();
        long alarmTimeMillis = toMillis();
        long timeDifferenceMillis = alarmTimeMillis - currentTimeMillis;
        return getTimeDifference(timeDifferenceMillis).toString();
    }

    private long getMillisLeft() {
        long currentTimeMillis = Calendar.getInstance().getTimeInMillis();
        long alarmTimeMillis = toMillis();
        return alarmTimeMillis - currentTimeMillis;
    }

    public String getHoursLeft() {
        return String.format("%d",TimeUnit.MILLISECONDS.toHours(getMillisLeft()));
    }

    public String getMinutesLeft() {
        long millisLeft = getMillisLeft();
        return String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(millisLeft) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisLeft)));
    }

    private String millisToString(long timeInMillis) {
        return millisToAlarmTime(timeInMillis).toString();
    }

    public AlarmTime millisToAlarmTime(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new AlarmTime(hour, minute);
    }

    public AlarmTime getTimeDifference(long timeDifferenceMillis) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(timeDifferenceMillis);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new AlarmTime(hour, minute);
    }


    @Override
    public String toString() {
        String hourToDisplay;
        String minuteToDisplay;

        if (hour < 10) {
            hourToDisplay = "0" + hour;
        } else {
            hourToDisplay = "" + hour;
        }

        if (minute < 10) {
            minuteToDisplay = "0" + minute;
        } else {
            minuteToDisplay = "" + minute;
        }
        return hourToDisplay+":"+minuteToDisplay;
    }


    public AlarmTime addMinutes(int minutesAdded) {
        long initialTimeMillis = this.toMillis();
        long incrementMillis = TimeUnit.MINUTES.toMillis(minutesAdded);
        long incrementedTimeMillis = initialTimeMillis + incrementMillis;
        return millisToAlarmTime(incrementedTimeMillis);
    }
}
