package com.anshmidt.multialarm.datasources

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

class FileStorage(private val context: Context) {

    private val fileContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.createDeviceProtectedStorageContext()
    } else {
        context
    }

    fun getFileName(uri: Uri): String? =
            when(uri.scheme) {
                ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
                else -> uri.path?.let { File(it).name }
            }

    fun clearFilesDir() {
        fileContext.filesDir.deleteRecursively()
    }

    fun copyToAppDir(sourceUri: Uri): Uri {
        val newFileName = getFileName(sourceUri) ?: generateFileName(sourceUri)

        val destinationDir = fileContext.filesDir
        val destinationFile = File(destinationDir, newFileName)

        FileOutputStream(destinationFile).use { outputStream ->
            fileContext.contentResolver.openInputStream(sourceUri).use { inputStream ->
                inputStream?.copyTo(outputStream)
            }

        }

        return destinationFile.toUri()
    }

    private fun getContentFileName(uri: Uri): String? = runCatching {
        fileContext.contentResolver.query(
                uri,
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


    private fun generateFileName(uri: Uri): String {
        val fileExtension = getFileExtension(uri) ?:
                throw RuntimeException("Cannot read extension from uri: $this")
        return generateFileName(fileExtension)
    }

    private fun generateFileName(fileExtension: String): String {
        return "${System.currentTimeMillis()}.$fileExtension"
    }

    private fun getFileExtension(uri: Uri): String? {
        val mimeType = fileContext.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    }

}