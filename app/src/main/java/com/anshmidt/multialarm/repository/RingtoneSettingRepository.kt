package com.anshmidt.multialarm.repository

import android.content.Context
import android.net.Uri
import com.anshmidt.multialarm.repository.FileExtensions.copyToAppDir

class RingtoneSettingRepository(
        private val context: Context
) : IRingtoneSettingRepository {

    override fun clearAllRingtones() {
        context.filesDir.deleteRecursively()
    }

    override fun copyRingtoneToAppDirectory(ringtoneUri: Uri): Uri {
        return ringtoneUri.copyToAppDir(context)
    }
}