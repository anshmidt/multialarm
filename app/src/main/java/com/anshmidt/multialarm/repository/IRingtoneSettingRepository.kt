package com.anshmidt.multialarm.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface IRingtoneSettingRepository {

    fun getRingtoneFileName(uri: Uri): String?

    suspend fun saveRingtoneDurationSeconds(ringtoneDurationSeconds: Int)
    fun getRingtoneDurationSeconds(): Flow<Int>
    suspend fun saveRingtoneUri(uri: Uri)
    fun getRingtoneUri(): Flow<Uri>

    fun getRingtoneFileName(): Flow<String?>

    fun clearAllRingtones()

    fun copyRingtoneToAppDirectory(ringtoneUri: Uri): Uri

}