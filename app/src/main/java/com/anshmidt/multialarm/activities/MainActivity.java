package com.anshmidt.multialarm.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.anshmidt.multialarm.AlarmParams;
import com.anshmidt.multialarm.AlarmTime;
import com.anshmidt.multialarm.view_helpers.AlarmsListHelper;
import com.anshmidt.multialarm.view_helpers.NotificationIconHelper;
import com.anshmidt.multialarm.TimerManager;
import com.anshmidt.multialarm.R;
import com.anshmidt.multialarm.SharedPreferencesHelper;
import com.anshmidt.multialarm.dialogs.IntervalDialogFragment;
import com.anshmidt.multialarm.dialogs.NumberOfAlarmsDialogFragment;
import com.anshmidt.multialarm.dialogs.TimePickerDialogFragment;

public class MainActivity extends AppCompatActivity implements
        IntervalDialogFragment.IntervalDialogListener,
        NumberOfAlarmsDialogFragment.NumberOfAlarmsDialogListener,
        TimePickerDialog.OnTimeSetListener {

    SwitchCompat onOffSwitch;
    ListView alarmsListView;
    TextView intervalBetweenAlarmsTextView;
    TextView numberOfAlarmsTextView;
    TextView firstAlarmTextView;
    TextView timeLeftTextView;
    LinearLayout firstAlarmLayout;
    LinearLayout intervalLayout;
    LinearLayout numberOfAlarmsLayout;
    NotificationIconHelper nIconHelper;
    AlarmsListHelper alarmsListHelper;
    SharedPreferencesHelper sharPrefHelper;
    TimerManager timerManager;
    AlarmParams alarmParams;
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmsListView = (ListView) findViewById(R.id.listview_main_alarmslist);
        onOffSwitch = (SwitchCompat) findViewById(R.id.switch_main);
        intervalBetweenAlarmsTextView = (TextView) findViewById(R.id.textview_main_interval);
        numberOfAlarmsTextView = (TextView) findViewById(R.id.textview_main_numberofalarms);
        firstAlarmLayout = (LinearLayout) findViewById(R.id.layout_main_firstalarm);
        firstAlarmTextView = (TextView) findViewById(R.id.textview_main_firstalarm_time);
        timeLeftTextView = (TextView) findViewById(R.id.textview_main_timeleft);
        intervalLayout = (LinearLayout) findViewById(R.id.layout_main_interval);
        numberOfAlarmsLayout = (LinearLayout) findViewById(R.id.layout_main_numberofalarms);

        sharPrefHelper = new SharedPreferencesHelper(MainActivity.this);
        alarmParams = sharPrefHelper.getParams();
        timerManager = new TimerManager(MainActivity.this);
        nIconHelper = new NotificationIconHelper(MainActivity.this);
        alarmsListHelper = new AlarmsListHelper(MainActivity.this, alarmsListView);

        showFirstAlarmTime(alarmParams.firstAlarmTime.toString());
        showTimeLeft(alarmParams);

        showInterval(sharPrefHelper.getIntervalStr());
        showNumberOfAlarms(sharPrefHelper.getNumberOfAlarmsStr());
        onOffSwitch.setChecked(sharPrefHelper.isAlarmTurnedOn());
        nIconHelper.setNotificationIcon(sharPrefHelper.isAlarmTurnedOn());

        alarmsListHelper.showList(alarmParams);

        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarmParams.turnedOn = isChecked;
                if (isChecked) {
                    timerManager.startTimer(alarmParams);
                    nIconHelper.showNotificationIcon();
                    showToast(getString(R.string.main_alarm_turned_on_toast));
                } else {
                    timerManager.cancelTimer();
                    nIconHelper.hideNotificationIcon();
                    showToast(getString(R.string.main_alarm_turned_off_toast));
                }
                alarmsListHelper.showList(alarmParams);
                showTimeLeft(alarmParams);
                sharPrefHelper.setAlarmState(isChecked);
            }
        });

        intervalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntervalDialogFragment dialog = new IntervalDialogFragment();
                Bundle intervalBundle = new Bundle();
                intervalBundle.putString("interval", sharPrefHelper.getIntervalStr());
                dialog.setArguments(intervalBundle);
                dialog.show(getFragmentManager(), "intervalDialog");
            }
        });

        numberOfAlarmsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberOfAlarmsDialogFragment dialog = new NumberOfAlarmsDialogFragment();
                Bundle numberOfAlarmsBundle = new Bundle();
                numberOfAlarmsBundle.putString("number_of_alarms", sharPrefHelper.getNumberOfAlarmsStr());
                dialog.setArguments(numberOfAlarmsBundle);
                dialog.show(getFragmentManager(), "numberOfAlarmsDialog");
            }
        });

        firstAlarmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle timePickerBundle = new Bundle();
                timePickerBundle.putInt("alarm_hour", sharPrefHelper.getHour());
                timePickerBundle.putInt("alarm_minute", sharPrefHelper.getMinute());

                TimePickerDialogFragment timePicker = new TimePickerDialogFragment();

                timePicker.setArguments(timePickerBundle);
                timePicker.show(getFragmentManager(), "time_picker");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: {
                Intent intent = new Intent(this, PrefActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onIntervalChanged(String intervalStr) {
        showInterval(intervalStr);
        alarmParams.interval = Integer.parseInt(intervalStr);
        alarmsListHelper.showList(alarmParams);
        resetTimerIfTurnedOn();
        sharPrefHelper.setInterval(intervalStr);
    }

    @Override
    public void onNumberOfAlarmsChanged(String numberOfAlarmsStr) {
        showNumberOfAlarms(numberOfAlarmsStr);
        alarmParams.numberOfAlarms = Integer.parseInt(numberOfAlarmsStr);
        alarmsListHelper.showList(alarmParams);
        resetTimerIfTurnedOn();
        sharPrefHelper.setNumberOfAlarms(numberOfAlarmsStr);
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        AlarmTime alarmTime = new AlarmTime(hour, minute);
        alarmParams.firstAlarmTime = alarmTime;
        showFirstAlarmTime(alarmTime.toString());
        alarmsListHelper.showList(alarmParams);
        showTimeLeft(alarmParams);
        resetTimerIfTurnedOn();
        sharPrefHelper.setTime(alarmTime);
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void resetTimerIfTurnedOn() {
        if (onOffSwitch.isChecked()) {
            timerManager.resetTimer(alarmParams);
            showToast(getString(R.string.main_alarm_reset_toast));
        }
    }

    private void showInterval(String interval) {
        String wholeTitle = getString(R.string.main_interval, interval);
        SpannableString wholeTitleSpan = new SpannableString(wholeTitle);
        wholeTitleSpan.setSpan(new RelativeSizeSpan(2f), wholeTitle.indexOf(interval), interval.length() + 1, 0);
        intervalBetweenAlarmsTextView.setText(wholeTitleSpan);
    }

    private void showNumberOfAlarms(String numberOfAlarms) {
        int numberOfAlarmsInt = Integer.parseInt(numberOfAlarms);
        String wholeTitle = this.getResources().getQuantityString(R.plurals.main_number_of_alarms, numberOfAlarmsInt, numberOfAlarmsInt);
        SpannableString wholeTitleSpan = new SpannableString(wholeTitle);
        wholeTitleSpan.setSpan(new RelativeSizeSpan(2f), wholeTitle.indexOf(numberOfAlarms),
                numberOfAlarms.length() + 1, 0);
        numberOfAlarmsTextView.setText(wholeTitleSpan);
    }

    private void showFirstAlarmTime(String firstAlarmTime) {
        String wholeTitle = getString(R.string.main_firstalarm_time, firstAlarmTime);
        SpannableString wholeTitleSpan = new SpannableString(wholeTitle);
        wholeTitleSpan.setSpan(new RelativeSizeSpan(2f), wholeTitle.indexOf(firstAlarmTime) - 1,
                wholeTitle.indexOf(firstAlarmTime) + firstAlarmTime.length(), 0);
        firstAlarmTextView.setText(wholeTitleSpan);
    }

    private void showTimeLeft(AlarmParams alarmParams) {
        AlarmTime alarmTime = alarmParams.firstAlarmTime;
        timeLeftTextView.setText(getString(R.string.all_time_left, alarmTime.getHoursLeft(), alarmTime.getMinutesLeft()));
        if (alarmParams.turnedOn) {
            timeLeftTextView.setTextColor(getColor(R.color.primary));
        } else {
            timeLeftTextView.setTextColor(getColor(R.color.main_disabled_textcolor));
        }
    }








}
