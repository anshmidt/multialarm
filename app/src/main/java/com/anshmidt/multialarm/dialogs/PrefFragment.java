package com.anshmidt.multialarm.dialogs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import com.anshmidt.multialarm.R;

import java.util.Map;


/**
 * Created by Ilya Anshmidt on 29.09.2017.
 */


public class PrefFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }


}
