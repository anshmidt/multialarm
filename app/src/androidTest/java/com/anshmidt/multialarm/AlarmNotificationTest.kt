package com.anshmidt.multialarm

import android.graphics.Point
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationHelper
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationParams
import com.anshmidt.multialarm.view.MainActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AlarmNotificationTest {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    val notificationParams = NotificationParams()
    val notificationHelper = NotificationHelper(context = appContext, notificationParams = notificationParams)

    val expectedAppName = appContext.getString(R.string.app_name)

    private val uiDevice by lazy { UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()) }

    private fun hideNotifications() {
        val displayHeight = uiDevice.displayHeight
        val displayWidth = uiDevice.displayWidth
        val startX = displayWidth / 2
        val startY = displayHeight / 2
        val endX = startX
        val endY = 0
        uiDevice.swipe(
                startX,
                startY,
                endX,
                endY,
                15
        )
    }

    @Test
    fun notificationAppears() {
        //given
        val expectedTitle = appContext.getString(R.string.dismiss_alarm_notification_title)

        //when
        notificationHelper.showNotification()

        //then
        uiDevice.openNotification()
        val timeout = 10000L
        uiDevice.wait(Until.hasObject(By.text(expectedTitle)), timeout)
        val title: UiObject2 = uiDevice.findObject(By.text(expectedTitle))
        Assert.assertEquals(expectedTitle, title.text)

        //teardown
        notificationHelper.cancelNotification()
        uiDevice.wait(Until.gone(By.text(expectedTitle)), timeout)
        hideNotifications()
    }
}