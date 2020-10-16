package com.anshmidt.multialarm

import android.test.mock.MockContext
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationHelper
import com.anshmidt.multialarm.notifications.dismissalarm.NotificationParams
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class NotificationParamsTest {

    lateinit var notificationParams: NotificationParams


    @Before
    fun setUp() {
        notificationParams = NotificationParams()
    }

    @Test
    fun `get notification id doesn't throw exception`() {
        notificationParams.notificationId
    }

}