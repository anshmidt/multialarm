package com.anshmidt.multialarm.repository

import android.media.RingtoneManager
import android.net.Uri
import com.anshmidt.multialarm.datasources.DataStoreStorage
import com.anshmidt.multialarm.datasources.FileStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RingtoneSettingRepository(
        private val dataStoreStorage: DataStoreStorage,
        private val fileStorage: FileStorage
) : IRingtoneSettingRepository {

    override suspend fun saveRingtoneDurationSeconds(ringtoneDurationSeconds: Int) {
        dataStoreStorage.saveRingtoneDurationSeconds(ringtoneDurationSeconds)
    }

    override fun getRingtoneDurationSeconds(): Flow<Int> {
        return dataStoreStorage.getRingtoneDurationSeconds()
    }

    override suspend fun saveRingtoneUri(uri: Uri) {
        val uriString = uri.toString()
        dataStoreStorage.saveRingtoneUriString(uriString)
    }

    override fun getRingtoneUri(): Flow<Uri> {
        val defaultUriString = getDefaultRingtoneUri().toString()

        return dataStoreStorage.getRingtoneUriString().map { uriString ->
            if (uriString.isEmpty()) {
                Uri.parse(defaultUriString)
            } else {
                Uri.parse(uriString)
            }
        }
    }

    override fun getRingtoneFileName(): Flow<String?> {
        return getRingtoneUri().map { ringtoneUri ->
            fileStorage.getFileName(ringtoneUri)
        }
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