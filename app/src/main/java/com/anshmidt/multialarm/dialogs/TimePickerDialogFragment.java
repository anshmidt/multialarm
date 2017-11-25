package com.anshmidt.multialarm.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.anshmidt.multialarm.R;

/**
 * Created by Ilya Anshmidt on 28.09.2017.
 */

public class TimePickerDialogFragment extends DialogFragment  {
    private int hour;
    private int minute;
    private TimePickerDialog.OnTimeSetListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (TimePickerDialog.OnTimeSetListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        hour = b.getInt("alarm_hour");
        minute = b.getInt("alarm_minute");
        final AlarmTimePickerDialog timePickerDialog = new AlarmTimePickerDialog(getActivity(), listener, hour, minute, true);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View customTitleView = inflater.inflate(R.layout.partial_timepicker_dialog_title, null);

        timePickerDialog.setCustomTitle(customTitleView);

        timePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                timePickerDialog.showTimeLeft(hour, minute);
            }
        });

        return timePickerDialog;
    }


}
