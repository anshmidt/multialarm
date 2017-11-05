package com.anshmidt.multialarm;

/**
 * Created by Ilya Anshmidt on 19.09.2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.anshmidt.multialarm.activities.DismissAlarmActivity;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    public static final String ONE_TIME = "onetime";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent dismissAlarmIntent = new Intent(context, DismissAlarmActivity.class);
        context.startActivity(dismissAlarmIntent);
    }

}
