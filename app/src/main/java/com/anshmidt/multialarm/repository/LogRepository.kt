package com.anshmidt.multialarm.repository

import com.anshmidt.multialarm.datasources.FileStorage

class LogRepository(
    private val fileStorage: FileStorage
) {

    fun getLog(): List<String> {
        return fileStorage.readLogFile()
    }
}