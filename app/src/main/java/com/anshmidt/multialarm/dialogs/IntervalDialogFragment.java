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

public class IntervalDialogFragment extends DialogFragment {

    public interface IntervalDialogListener {
        void onIntervalChanged(String interval);
    }

    EditText intervalEditText;
    KeyboardHelper keyboardHelper;
    boolean intervalValid = true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        View subView = inflater.inflate(R.layout.dialog_interval, null);
        keyboardHelper = new KeyboardHelper(getActivity());
        intervalEditText = (EditText)subView.findViewById(R.id.edittext_intervaldialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(subView);

        String getArgument = getArguments().getString("interval");
        intervalEditText.setText(getArgument);
        keyboardHelper.moveCursorToEnd(intervalEditText);
        keyboardHelper.showKeyboard(intervalEditText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String intervalStr = intervalEditText.getText().toString();
                if (intervalValid) {
                    IntervalDialogListener activity = (IntervalDialogListener) getActivity();
                    activity.onIntervalChanged(intervalStr);
                    keyboardHelper.hideKeyboard(intervalEditText);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                keyboardHelper.hideKeyboard(intervalEditText);
            }
        });

        final AlertDialog dialog = builder.create();

        intervalEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValid(s.toString())) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    intervalValid = true;
                } else {
                    intervalValid = false;
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });

        return dialog;
    }


    private boolean isValid(String intervalStr) {
        if (TextUtils.isEmpty(intervalStr)) {
            return false;
        }

        int interval;
        try {
            interval = Integer.parseInt(intervalStr);
        } catch (NumberFormatException e) {
            return false;
        }
        if (interval < 2) {
            return false;
        }
        return true;
    }
}
