package com.anshmidt.multialarm.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.anshmidt.multialarm.R;
import com.anshmidt.multialarm.view_helpers.KeyboardHelper;

/**
 * Created by Ilya Anshmidt on 26.09.2017.
 */

public class NumberOfAlarmsDialogFragment extends DialogFragment {

    public interface NumberOfAlarmsDialogListener {
        void onNumberOfAlarmsChanged(String numberOfAlarms);
    }

    EditText numberOfAlarmsEditText;
    KeyboardHelper keyboardHelper;
    boolean numberValid = true;
    public static final String FRAGMENT_TAG = "numberOfAlarmsDialog";
    public static final String BUNDLE_KEY_NUMBER_OF_ALARMS = "number_of_alarms";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.dialog_number_of_alarms, null);
        keyboardHelper = new KeyboardHelper(getActivity());
        numberOfAlarmsEditText = (EditText)subView.findViewById(R.id.edittext_numberdialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(subView);

        String getArgument = getArguments().getString(BUNDLE_KEY_NUMBER_OF_ALARMS);
        numberOfAlarmsEditText.setText(getArgument);
        keyboardHelper.moveCursorToEnd(numberOfAlarmsEditText);
        keyboardHelper.showKeyboard(numberOfAlarmsEditText);


        builder.setPositiveButton(getString(R.string.number_of_alarms_dialog_ok_button_name), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String numberStr = numberOfAlarmsEditText.getText().toString();
                if (numberValid) {
                    NumberOfAlarmsDialogListener activity = (NumberOfAlarmsDialogListener) getActivity();
                    activity.onNumberOfAlarmsChanged(numberStr);
                    keyboardHelper.hideKeyboard(numberOfAlarmsEditText);
                }

                NumberOfAlarmsDialogListener activity = (NumberOfAlarmsDialogListener) getActivity();
                activity.onNumberOfAlarmsChanged(numberOfAlarmsEditText.getText().toString());
                keyboardHelper.hideKeyboard(numberOfAlarmsEditText);
            }
        });

        builder.setNegativeButton(getString(R.string.number_of_alarms_dialog_cancel_button_name), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                keyboardHelper.hideKeyboard(numberOfAlarmsEditText);
            }
        });

        final AlertDialog dialog = builder.create();

        numberOfAlarmsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValid(s.toString())) {
                    numberValid = true;
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    numberValid = false;
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });

        return dialog;
    }

    private boolean isValid(String numberStr) {
        if (TextUtils.isEmpty(numberStr)) {
            return false;
        }

        try {
            int number = Integer.parseInt(numberStr);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
