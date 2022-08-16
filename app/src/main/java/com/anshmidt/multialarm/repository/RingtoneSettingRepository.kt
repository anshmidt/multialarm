package com.anshmidt.multialarm.repository

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import com.anshmidt.multialarm.datasources.FileStorage
import com.anshmidt.multialarm.datasources.SharedPreferencesStorage

class RingtoneSettingRepository(
        private val sharedPreferencesStorage: SharedPreferencesStorage,
        private val fileStorage: FileStorage
) : IRingtoneSettingRepository {

    override var ringtoneDurationSeconds
        get() = sharedPreferencesStorage.ringtoneDurationSeconds
        set(value) {
            sharedPreferencesStorage.ringtoneDurationSeconds = value
        }

    override var ringtoneUri: Uri
        get() {
            val defaultUriString = getDefaultRingtoneUri().toString()
            val uriString = sharedPreferencesStorage.ringtoneUriString

            return if (uriString.isNullOrEmpty()) {
                Uri.parse(defaultUriString)
            } else {
                Uri.parse(uriString)
            }
        }
        set(value) {
            val uriString = value.toString()
            sharedPreferencesStorage.ringtoneUriString = uriString
        }

    override fun getRingtoneFileName(): String? {
        return fileStorage.getFileName(ringtoneUri)
    }

    override fun getRingtoneFileName(uri: Uri): String? {
        return fileStorage.getFileName(uri)
    }

    override fun clearAllRingtones() {
        fileStorage.clearFilesDir()
    }

    override fun copyRingtoneToAppDirectory(ringtoneUri: Uri): Uri {
        return fileStorage.copyToAppDir(ringtoneUri)
    }

    private fun getDefaultRingtoneUri(): Uri {
        // if user has never set alarm on a new device, uri for TYPE_ALARM could be null
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM) ?:
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    }
}