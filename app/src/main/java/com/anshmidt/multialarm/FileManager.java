package com.anshmidt.multialarm;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Ilya Anshmidt on 11.11.2017.
 */

public class FileManager {

    public FileManager(Context context) {
        this.context = context;
    }

    private final String LOG_TAG = FileManager.class.getSimpleName();
    private Context context;

    public void copyToAppDir(final Uri uri, final String destinationFileName) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    clearAppDir();

                    FileInputStream fileInputStream = (FileInputStream) context.getContentResolver().openInputStream(uri);
                    FileChannel source = fileInputStream.getChannel();

                    File destinationFile = new File(context.getFilesDir(), destinationFileName);
                    Log.d(LOG_TAG, "destinationFile: " + destinationFile);
                    FileChannel destination  = new FileOutputStream(destinationFile).getChannel();

                    destination.transferFrom(source, 0, source.size());

                    Log.d(LOG_TAG, "destinationFile length: " + destinationFile.length());

                } catch (IOException e) {
                    Log.e(LOG_TAG, "Copying a file failed: " + e);
                }
            }
        };

        Thread t = new Thread(runnable);
        t.start();
    }

    public String getFileName(Uri uri) {
        String uriString = uri.toString();
        String name = "";
        final String CONTENT_PREFIX = "content://";
        final String FILE_PREFIX = "file://";

        if (uriString.startsWith(CONTENT_PREFIX)) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        } else if (uriString.startsWith(FILE_PREFIX)) {
            File ringtoneFile = new File(uriString);
            name = ringtoneFile.getName();
        }
        return name;
    }

    private void clearAppDir() {
        File appDir = context.getFilesDir();
        for (File file: appDir.listFiles()) {
            if (! file.isDirectory()) {
                file.delete();
            }
        }
    }




}
