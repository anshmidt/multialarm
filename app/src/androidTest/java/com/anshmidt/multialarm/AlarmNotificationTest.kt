package com.anshmidt.multialarm

import android.content.Intent
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationHelper
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationParams
import com.anshmidt.multialarm.services.MusicService
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AlarmNotificationTest {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    val notificationParams = NotificationParams()
    val notificationHelper = NotificationHelper(context = appContext, notificationParams = notificationParams)

    private val uiDevice by lazy { UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()) }

    val expectedTitle = appContext.getString(R.string.dismiss_alarm_notification_title)
    val notificationAnimationTimeout = 10000L

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

    private fun isDismissAlarmActivityDisplayed(): Boolean {
        val packageName = BuildConfig.APPLICATION_ID
        val buttonResourceId = "button_dismiss_alarm"
        val fullScreenDismissButton = uiDevice.findObject(By.res(packageName, buttonResourceId))
        if (fullScreenDismissButton == null) return false
        return fullScreenDismissButton.isClickable
    }

    @After
    fun tearDown() {
        notificationHelper.cancelNotification()
        uiDevice.wait(Until.gone(By.text(expectedTitle)), notificationAnimationTimeout)
        hideNotifications()
        uiDevice.wakeUp()
    }

    @Test
    fun notificationAppears() {
        //when
        notificationHelper.showNotification()

        //then
        uiDevice.openNotification()
        uiDevice.wait(Until.hasObject(By.text(expectedTitle)), notificationAnimationTimeout)
        val title: UiObject2 = uiDevice.findObject(By.text(expectedTitle))
        Assert.assertEquals(expectedTitle, title.text)
    }

    @Test
    fun turnedOffScreen() {
        //when
        uiDevice.sleep()

        val isScreenOn = uiDevice.isScreenOn

        notificationHelper.showNotification()

        //then full screen activity appears
        Assert.assertTrue(isDismissAlarmActivityDisplayed())
    }

    @Test
    fun dismissButton() {
        //when
        notificationHelper.showNotification()
        uiDevice.openNotification()

        val dismissButtonBy = By.text("Dismiss")
        val dismissButton: UiObject2 = uiDevice.findObject(dismissButtonBy)
        dismissButton.click()
        uiDevice.wait(Until.gone(dismissButtonBy), notificationAnimationTimeout)

        //then there is no dismiss button
        val dismissButtons = uiDevice.findObjects(dismissButtonBy)
        Assert.assertEquals(0, dismissButtons.size)
    }

    @Test
    fun nextNotificationAppearsAfterDismissingPreviousOne() {
        //given
        notificationHelper.showNotification()
        uiDevice.openNotification()

        val dismissButtonBy = By.text("Dismiss")
        val dismissButton: UiObject2 = uiDevice.findObject(dismissButtonBy)
        dismissButton.click()
        uiDevice.wait(Until.gone(dismissButtonBy), notificationAnimationTimeout)

        //when
        notificationHelper.showNotification()

        //then
        uiDevice.wait(Until.hasObject(By.text(expectedTitle)), notificationAnimationTimeout)
        val title: UiObject2 = uiDevice.findObject(By.text(expectedTitle))
        Assert.assertEquals(expectedTitle, title.text)
    }


}