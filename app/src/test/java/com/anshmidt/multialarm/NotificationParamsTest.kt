package com.anshmidt.multialarm

import org.junit.Before
import org.junit.Test

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