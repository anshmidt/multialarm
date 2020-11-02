package com.anshmidt.multialarm

import androidx.test.platform.app.InstrumentationRegistry

class DeviceStateController {
    val uiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation()

    fun goToDozeMode() {
        uiAutomation.executeShellCommand("dumpsys battery unplug")
        uiAutomation.executeShellCommand("dumpsys deviceidle force-idle")
    }

    fun returnFromDozeMode() {
        uiAutomation.executeShellCommand("dumpsys deviceidle unforce")
        uiAutomation.executeShellCommand("dumpsys battery reset")
    }
}