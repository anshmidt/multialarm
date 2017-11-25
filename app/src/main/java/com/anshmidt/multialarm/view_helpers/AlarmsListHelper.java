package com.anshmidt.multialarm.view_helpers;

import android.content.Context;
import android.widget.ListView;

import com.anshmidt.multialarm.AlarmParams;
import com.anshmidt.multialarm.AlarmTime;
import com.anshmidt.multialarm.R;
import com.anshmidt.multialarm.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ilya Anshmidt on 27.10.2017.
 */

public class AlarmsListHelper {

    private Context context;
    private ListView listView;
    private final String ATTRIBUTE_NAME_TEXT = "text";
    private SharedPreferencesHelper sharPrefHelper;
    private AlarmParams alarmParams;
    private String THREE_DOTS;
    private final int LIST_LENGTH = 6;

    public AlarmsListHelper(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
        this.sharPrefHelper = new SharedPreferencesHelper(context);
        this.alarmParams = sharPrefHelper.getParams();
        this.THREE_DOTS = AlarmsListAdapter.THREE_DOTS;
    }

    public void showList(AlarmParams alarmParams) {
        String[] alarms = getDisplayedAlarmsList();

        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(alarms.length);
        Map<String, Object> map;
        for (int i = 0; i < alarms.length; i++) {
            map = new HashMap<String, Object>();
            map.put(ATTRIBUTE_NAME_TEXT, alarms[i]);
            data.add(map);
        }

        String[] from = { ATTRIBUTE_NAME_TEXT };
        int[] to = { R.id.textview_main_alarmslist };

        this.alarmParams = alarmParams;

        AlarmsListAdapter adapter = new AlarmsListAdapter(context, data, R.layout.item_main_alarms_list,
                from, to, alarmParams);

        listView.setAdapter(adapter);
    }

    private String[] getFullAlarmsList() {
        String[] fullAlarmsList = new String[alarmParams.numberOfAlarms];
        AlarmTime time = alarmParams.firstAlarmTime;
        for (int i = 0; i < alarmParams.numberOfAlarms; i++) {
            fullAlarmsList[i] = time.toString();
            time = time.addMinutes(alarmParams.interval);
        }
        return fullAlarmsList;
    }

    private String[] getDisplayedAlarmsList() {  // { "06:00", "06:15", "...", "07:45", "08:00" };
        String[] displayedAlarmsList = new String[LIST_LENGTH];
        String[] fullAlarmsList = getFullAlarmsList();
        if (fullAlarmsList.length <= LIST_LENGTH) {
            return fullAlarmsList;
        }

        for (int i = 0; i < 3; i++) {
            displayedAlarmsList[i] = fullAlarmsList[i];
        }

        displayedAlarmsList[3] = THREE_DOTS;

        displayedAlarmsList[LIST_LENGTH - 2] = fullAlarmsList[fullAlarmsList.length - 2];
        displayedAlarmsList[LIST_LENGTH - 1] = fullAlarmsList[fullAlarmsList.length - 1];

        return displayedAlarmsList;
    }



}
