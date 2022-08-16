package com.anshmidt.multialarm.repository

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import com.anshmidt.multialarm.datasources.SharedPreferencesStorage
import com.anshmidt.multialarm.repository.FileExtensions.copyToAppDir
import com.anshmidt.multialarm.repository.FileExtensions.getFileName

class RingtoneSettingRepository(
        private val context: Context,
        private val sharedPreferencesStorage: SharedPreferencesStorage
) : IRingtoneSettingRepository {

    override var songDurationSeconds = sharedPreferencesStorage.songDurationSeconds

    override var songUri: Uri
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
        return songUri.getFileName(context)
    }

    override fun getRingtoneFileName(uri: Uri): String? {
        return uri.getFileName(context)
    }

    override fun clearAllRingtones() {
        context.filesDir.deleteRecursively()
    }

    override fun copyRingtoneToAppDirectory(ringtoneUri: Uri): Uri {
        return ringtoneUri.copyToAppDir(context)
    }

    private fun getDefaultRingtoneUri(): Uri {
        // if user has never set alarm on a new device, uri for TYPE_ALARM could be null
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM) ?:
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    }
}