package com.anshmidt.multialarm

import androidx.test.uiautomator.UiDevice

class AlarmDumpsysInteractor(val uiDevice: UiDevice) {

    /**
     * Output if alarm not scheduled: "[]"
     */
    fun getAlarmEntry(): String {
        val output = uiDevice.executeShellCommand("dumpsys alarm")
        val outputParts = output.split("Batch")
        val outputForApp = outputParts.filter { it.contains("multialarm") }
        return outputForApp.toString()
    }

    fun isAlarmScheduled(): Boolean {
        if (getAlarmEntry().length < 10) {
            return false
        } else {
            return true
        }
    }

}