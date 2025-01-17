package com.anshmidt.multialarm.logging

import android.content.Context
import org.tinylog.Logger
import java.io.File

object Log {
    const val LOG_FILE_NAME = "log.txt"

    fun initializeLogging(context: Context) {
        val directoryForLogs: File? = context.filesDir
        directoryForLogs?.let {
            System.setProperty("tinylog.directory", it.absolutePath)
        }
    }

    fun getLogFile(context: Context): File {
        val directoryForLogs: File? = context.filesDir
        return File(directoryForLogs, LOG_FILE_NAME)
    }

    fun d(tag: String, message: String) {
        Logger.debug("${tag}: ${message}", "")
    }
}