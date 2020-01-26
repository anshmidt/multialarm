package com.anshmidt.multialarm.dialogs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.anshmidt.multialarm.FileManager;
import com.anshmidt.multialarm.R;
import com.anshmidt.multialarm.SharedPreferencesHelper;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Ilya Anshmidt on 29.09.2017.
 */


public class PrefFragment extends PreferenceFragment {

    private final String LOG_TAG = PrefFragment.class.getSimpleName();
    private SharedPreferencesHelper sharPrefHelper;
    private FileManager fileManager;
    private String RINGTONE_FILENAME_KEY;
    private final int PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE = 23;
    private final int FILE_CHOOSER_REQUEST_CODE = 202;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        sharPrefHelper = new SharedPreferencesHelper(getContext());
        RINGTONE_FILENAME_KEY = getString(R.string.key_ringtone_filename);
        showRingtoneName(sharPrefHelper.getRingtoneFileName());

        Preference ringtonePreference = findPreference(RINGTONE_FILENAME_KEY);
        ringtonePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                onRingtonePreferenceClicked();
                return true;
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE: {
                // If request is cancelled, the result array is empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "Permission granted");
                    openFileChooser();
                } else {
                    Log.d(LOG_TAG, "Permission denied");
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedFileUri = data.getData();
            Log.d(LOG_TAG, "File found: uri: "+selectedFileUri);
            fileManager = new FileManager(getContext());
            String chosenFileName = fileManager.getFileName(selectedFileUri);

            sharPrefHelper = new SharedPreferencesHelper(getContext());
            sharPrefHelper.setRingtoneFileName(chosenFileName);

            Log.d(LOG_TAG, "File found: displayName: "+fileManager.getFileName(selectedFileUri));

            // Android 7 gives only temporary access to files from another apps, so I have do copy them to my app dir
            fileManager.copyToAppDir(selectedFileUri, chosenFileName);

            showRingtoneName(chosenFileName);
        }
    }

    private void onRingtonePreferenceClicked() {
        // request permissions if user didn't grant them before
        if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_READ_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            Log.d(LOG_TAG, "Permission already granted");
            openFileChooser();
        }
    }

    private void showRingtoneName(String ringtoneName) {
        Preference pref = findPreference(RINGTONE_FILENAME_KEY);
        if (ringtoneName.equals("")) {
            ringtoneName = getString(R.string.preferences_default_ringtone_name);
        }
        pref.setSummary(ringtoneName);
    }

    public void openFileChooser() {
        final String AUDIO_FILE_TYPE = "audio/*";
        Intent intent = new Intent()
                .setType(AUDIO_FILE_TYPE)
                .setAction(Intent.ACTION_GET_CONTENT);

        String chooserTitle = getString(R.string.chooseringtone_chooser_title);
        startActivityForResult(Intent.createChooser(intent, chooserTitle), FILE_CHOOSER_REQUEST_CODE);
    }



}
