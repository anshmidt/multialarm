package com.anshmidt.multialarm.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.anshmidt.multialarm.R;
import com.anshmidt.multialarm.dialogs.PrefFragment;

import java.util.List;

/**
 * Created by Ilya Anshmidt on 29.09.2017.
 */

public class PrefActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragment()).commit();
    }


}
