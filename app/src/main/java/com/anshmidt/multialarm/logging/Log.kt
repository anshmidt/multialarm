package com.anshmidt.multialarm.logging

import android.content.Context
import org.tinylog.Logger
import java.io.File

object Log {

    fun initializeLogging(context: Context) {
        val directoryForLogs: File? = context.filesDir
        directoryForLogs?.let {
            System.setProperty("tinylog.directory", it.absolutePath)
        }
    }

    fun d(tag: String, message: String) {
        Logger.debug("${tag}: ${message}", "")
    }
}