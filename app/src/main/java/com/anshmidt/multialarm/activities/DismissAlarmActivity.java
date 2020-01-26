package com.anshmidt.multialarm.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextClock;

import com.anshmidt.multialarm.AlarmParams;
import com.anshmidt.multialarm.DismissAlarmNotificationController;
import com.anshmidt.multialarm.R;
import com.anshmidt.multialarm.RingtonePlayer;
import com.anshmidt.multialarm.SharedPreferencesHelper;
import com.anshmidt.multialarm.TimerManager;
import com.anshmidt.multialarm.view_helpers.DismissButtonNameGiver;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

/**
 * Created by Ilya Anshmidt on 19.09.2017.
 */

public class DismissAlarmActivity extends AppCompatActivity implements RingtonePlayer.OnFinishListener {

    Button dismissButton;
    TextClock textClock;
    RingtonePlayer ringtonePlayer;
    SharedPreferencesHelper sharPrefHelper;
    int numberOfAlreadyRangAlarms;
    TimerManager timerManager;
    DismissAlarmNotificationController dismissAlarmNotificationController;
    private final String LOG_TAG = DismissAlarmActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dismiss);
        showOnLockedScreen();

        dismissAlarmNotificationController = new DismissAlarmNotificationController(DismissAlarmActivity.this);
        dismissAlarmNotificationController.cancelNotification();

        ringtonePlayer = new RingtonePlayer(DismissAlarmActivity.this);
        sharPrefHelper = new SharedPreferencesHelper(DismissAlarmActivity.this);
        timerManager = new TimerManager(DismissAlarmActivity.this);

        View layout = findViewById(R.id.dismissLayout);

        layout.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        dismissButton = (Button) findViewById(R.id.button_dismiss_alarm);
        textClock = (TextClock) findViewById(R.id.text_clock_dismiss);
        DismissButtonNameGiver dismissButtonNameGiver = new DismissButtonNameGiver(DismissAlarmActivity.this);
        dismissButton.setText(dismissButtonNameGiver.getName());

        scheduleNextAlarm();

        numberOfAlreadyRangAlarms = sharPrefHelper.getNumberOfAlreadyRangAlarms() + 1;
        Log.d(LOG_TAG, "numberOfAlreadyRangAlarms (including current one) = " + numberOfAlreadyRangAlarms);
        sharPrefHelper.setNumberOfAlreadyRangAlarms(numberOfAlreadyRangAlarms);

        ringtonePlayer.start();

        AlarmParams alarmParams = sharPrefHelper.getParams();
        //if all alarms have rung, and user didn't turn the switch off, alarms are set to the next day
        if (numberOfAlreadyRangAlarms >= alarmParams.numberOfAlarms) {
            Log.d(LOG_TAG, "Alarms have already rung " + numberOfAlreadyRangAlarms + " times and will be reset to tomorrow");
            long firstAlarmTimeMillis = alarmParams.firstAlarmTime.toNextDayMillis();
            timerManager.resetSingleAlarmTimer(firstAlarmTimeMillis);
        }

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtonePlayer.stop();
                finish();
            }
        });
    }

    @Override
    public void onPlayerFinished() {
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (hasWindowFocus()) {
            ringtonePlayer.stop();
        }
    }


    private void showOnLockedScreen() {

        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    private void scheduleNextAlarm() {
        long intervalBetweenRepeatingAlarmsMillis = TimeUnit.MINUTES.toMillis(sharPrefHelper.getInterval());
        long currentTimeMillis = System.currentTimeMillis();
        timerManager.resetSingleAlarmTimer(currentTimeMillis + intervalBetweenRepeatingAlarmsMillis);
    }
}
