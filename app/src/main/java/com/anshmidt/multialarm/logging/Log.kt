package com.anshmidt.multialarm.logging

import com.anshmidt.multialarm.di.DpsContext
import org.tinylog.Logger
import java.io.File

object Log {
    const val LOG_FILE_NAME = "log.txt"

    fun initializeLogging(dpsContext: DpsContext) {
        val directoryForLogs: File? = getDirectoryForLogs(dpsContext)
        directoryForLogs?.let {
            System.setProperty("tinylog.directory", it.absolutePath)
        }
    }

    fun getLogFile(dpsContext: DpsContext): File {
        val directoryForLogs: File? = getDirectoryForLogs(dpsContext)
        return File(directoryForLogs, LOG_FILE_NAME)
    }

    private fun getDirectoryForLogs(dpsContext: DpsContext): File? {
        return dpsContext.context.filesDir
    }

    fun d(tag: String, message: String) {
        Logger.debug("${tag}: ${message}", "")
    }
}