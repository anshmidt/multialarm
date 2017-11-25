package com.anshmidt.multialarm;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ilya Anshmidt on 08.10.2017.
 */

public class RingtonePlayer {

    public interface OnFinishListener {
        void onPlayerFinished();
    }

    private Context context;
    private SharedPreferencesHelper sharPrefHelper;
    private MediaPlayer mp;
    private AudioManager audioManager;
    private int initialRingerMode;
    private CountDownTimer countDownTimer;
    private int durationSeconds;
    private final String LOG_TAG = RingtonePlayer.class.getSimpleName();
    private OnFinishListener listener;

    public RingtonePlayer(Context context) {
        sharPrefHelper = new SharedPreferencesHelper(context);
        this.context = context;
        this.listener = (OnFinishListener) context;
    }

    public void start() {
        Log.d(LOG_TAG, "Playing started");
        setNormalRingerMode();
//        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        initialRingerMode = audioManager.getRingerMode();
//        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

        durationSeconds = sharPrefHelper.getDurationInt();
//        if (durationSeconds < 0) {
//            Log.e(LOG_TAG, "Invalid duration = " + durationSeconds);
//            return;
//        }

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_ALARM);
        if (durationSeconds > 0) {
            mp.setLooping(true);
        }

        if (durationSeconds == 0) {
            durationSeconds = mp.getDuration();
        }

        try {
            mp.setDataSource(context, getRingtone());
            mp.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Preparing MediaPlayer failed: " + e);
            e.printStackTrace();
            //if MediaPlayer fails to play ringtone from sharedPreferences, try to play default ringtone
            try {
                mp.setDataSource(context, getDefaultRingtone());
                mp.prepare();
            } catch (IOException e1) {
                Log.e(LOG_TAG, "Preparing MediaPlayer with default ringtone failed: " + e1);
            }
        }

        mp.start();
        startCountDownTimer(durationSeconds);

        Log.d(LOG_TAG, "MediaPlayer started: duration = "+ durationSeconds);
    }


    public void stop() {
        stopCountDownTimer();
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.reset();
            mp.release();
        }
        setInitialRingerMode();

        if (listener != null) {
            listener.onPlayerFinished();
        }

    }


    private void startCountDownTimer(int durationSec) {
        countDownTimer = new CountDownTimer(durationSec*1000, durationSec*1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                stop();
            }
        };
        countDownTimer.start();
    }

    private void stopCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private Uri getRingtone() {
        String filename = sharPrefHelper.getRingtoneFileName();
        if (filename.equals("")) {   // if ringtone not chosen yet
            return getDefaultRingtone();
        } else {
            File ringtone = new File(context.getFilesDir(), filename);
            return Uri.fromFile(ringtone);
        }

    }

    private Uri getDefaultRingtone() {
        return sharPrefHelper.getDefaultRingtoneUri();
    }


    private void setNormalRingerMode() {  // in case phone is in "Vibrate" mode
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        initialRingerMode = audioManager.getRingerMode();
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }

    private void setInitialRingerMode() {
        audioManager.setRingerMode(initialRingerMode);
    }


}
