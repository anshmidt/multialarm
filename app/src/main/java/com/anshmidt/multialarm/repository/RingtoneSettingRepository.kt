package com.anshmidt.multialarm.repository

import android.media.RingtoneManager
import android.net.Uri
import com.anshmidt.multialarm.datasources.FileStorage
import com.anshmidt.multialarm.datasources.SharedPreferencesStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RingtoneSettingRepository(
    private val sharedPreferencesStorage: SharedPreferencesStorage,
    private val fileStorage: FileStorage
) : IRingtoneSettingRepository {

    override suspend fun saveRingtoneDurationSeconds(ringtoneDurationSeconds: Int) {
        withContext(Dispatchers.IO) {
            sharedPreferencesStorage.saveRingtoneDurationSeconds(ringtoneDurationSeconds)
        }
    }

    override fun getRingtoneDurationSeconds(): Flow<Int> {
        return sharedPreferencesStorage.getRingtoneDurationSeconds().flowOn(Dispatchers.IO)
    }

    override suspend fun saveRingtoneUri(uri: Uri) {
        withContext(Dispatchers.IO) {
            val uriString = uri.toString()
            sharedPreferencesStorage.saveRingtoneUriString(uriString)
        }
    }

    override fun getRingtoneUri(): Flow<Uri> = flow {
        val defaultUriString = withContext(Dispatchers.IO) {
            getDefaultRingtoneUri().toString()
        }

        sharedPreferencesStorage.getRingtoneUriString().collect { uriString ->
            val parsedUri = withContext(Dispatchers.IO) {
                if (uriString.isEmpty()) {
                    Uri.parse(defaultUriString)
                } else {
                    Uri.parse(uriString)
                }
            }
            emit(parsedUri)
        }
    }

    override fun getRingtoneFileName(): Flow<String?> {
        return getRingtoneUri().map { ringtoneUri ->
            fileStorage.getFileName(ringtoneUri)
        }.flowOn(Dispatchers.IO)
    }

    override fun getRingtoneFileName(uri: Uri): String? {
        return fileStorage.getFileName(uri)
    }

    override suspend fun saveMusicVolume(musicVolumePercents: Int) {
        withContext(Dispatchers.IO) {
            sharedPreferencesStorage.saveMusicVolume(musicVolumePercents)
        }
    }

    override fun getMusicVolumePercents() =
        sharedPreferencesStorage.getMusicVolumePercents().flowOn(Dispatchers.IO)

    override suspend fun clearAllRingtones() {
        withContext(Dispatchers.IO) {
            fileStorage.clearFilesDir()
        }
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