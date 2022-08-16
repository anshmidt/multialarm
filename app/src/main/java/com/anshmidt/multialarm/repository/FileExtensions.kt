package com.anshmidt.multialarm.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.lang.RuntimeException

object FileExtensions {

    fun Uri.getFileName(context: Context): String? =
        when(scheme) {
            ContentResolver.SCHEME_CONTENT -> getContentFileName(context)
            else -> path?.let { File(it).name }
        }

    private fun Uri.getContentFileName(context: Context): String? = runCatching {
        context.contentResolver.query(
                this,
                null,
                null,
                null,
                null
        )?.use { cursor ->
            cursor.moveToFirst()
            return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                    .let(cursor::getString)
        }
    }.getOrNull()


    fun Uri.copyToAppDir(context: Context): Uri {
        val newFileName = this.getFileName(context) ?: this.generateFileName(context)

        val destinationDir = context.filesDir
        val destinationFile = File(destinationDir, newFileName)

        FileOutputStream(destinationFile).use { outputStream ->
            context.contentResolver.openInputStream(this).use { inputStream ->
                inputStream?.copyTo(outputStream)
            }

        }

        return destinationFile.toUri()
    }

    private fun Uri.generateFileName(context: Context): String {
        val fileExtension = getFileExtension(this, context) ?:
            throw RuntimeException("Cannot read extension from uri: $this")
        return generateFileName(fileExtension)
    }

    private fun generateFileName(fileExtension: String): String {
        return "${System.currentTimeMillis()}.$fileExtension"
    }

    private fun getFileExtension(uri: Uri, context: Context): String? {
        val mimeType = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    }

}


