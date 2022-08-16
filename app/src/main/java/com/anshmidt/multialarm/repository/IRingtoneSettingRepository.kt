package com.anshmidt.multialarm.repository

import android.net.Uri

interface IRingtoneSettingRepository {

    var ringtoneDurationSeconds: Int
    var ringtoneUri: Uri
    fun getRingtoneFileName(): String?
    fun getRingtoneFileName(uri: Uri): String?

    fun clearAllRingtones()

    fun copyRingtoneToAppDirectory(ringtoneUri: Uri): Uri

}