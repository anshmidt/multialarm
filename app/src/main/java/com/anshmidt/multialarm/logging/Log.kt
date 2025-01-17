package com.anshmidt.multialarm.logging

import android.content.Context
import com.anshmidt.multialarm.datasources.FileStorage
import org.tinylog.Logger
import java.io.File

object Log {
    const val LOG_FILE_NAME = "log.txt"

    fun initializeLogging(context: Context) {
        val fileContext = FileStorage.getFileContext(context)
        val directoryForLogs: File? = fileContext.filesDir
        directoryForLogs?.let {
            System.setProperty("tinylog.directory", it.absolutePath)
        }
    }

    fun getLogFile(context: Context): File {
        val fileContext = FileStorage.getFileContext(context)
        val directoryForLogs: File? = fileContext.filesDir
        return File(directoryForLogs, LOG_FILE_NAME)
    }

    fun d(tag: String, message: String) {
        Logger.debug("${tag}: ${message}", "")
    }
}