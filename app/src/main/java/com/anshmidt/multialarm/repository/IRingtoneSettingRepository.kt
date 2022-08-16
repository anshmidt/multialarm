package com.anshmidt.multialarm.repository

import android.net.Uri

interface IRingtoneSettingRepository {

    fun clearAllRingtones()

    fun copyRingtoneToAppDirectory(ringtoneUri: Uri): Uri

}