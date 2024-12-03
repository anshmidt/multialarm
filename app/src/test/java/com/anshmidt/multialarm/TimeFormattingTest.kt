package com.anshmidt.multialarm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anshmidt.multialarm.data.TimeFormatter
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.LocalTime
import java.lang.Math.abs
import java.util.concurrent.TimeUnit

class TimeFormattingTest {
    // Allows to set livedata
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getFirstAlarmTimeMillis_sameDayNow() {
        val firstAlarmTime = LocalTime.now().plusMinutes(5)  //5 minutes is added so that method under test chooses today/tomorrow correctly
        val actualFirstAlarmTimeMillis = TimeFormatter.getAlarmMillisWithin24Hours(firstAlarmTime)
        val expectedFirstAlarmTimeMillis = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)
        val MAX_RESULTS_DIFFERENCE_MILLIS = 2000  //calculation of the time will take some ms, so the results will not be equal
        Assert.assertTrue(
                "expected millis: $expectedFirstAlarmTimeMillis, actual millis: $actualFirstAlarmTimeMillis, difference: ${expectedFirstAlarmTimeMillis - actualFirstAlarmTimeMillis}",
                abs(expectedFirstAlarmTimeMillis - actualFirstAlarmTimeMillis) < MAX_RESULTS_DIFFERENCE_MILLIS
        )
    }

    @Test
    fun getFirstAlarmTimeMillis_sameDayAfterSomeTime() {
        val firstAlarmTime = LocalTime.now().plusHours(1)
        val actualFirstAlarmTimeMillis = TimeFormatter.getAlarmMillisWithin24Hours(firstAlarmTime)
        val expectedFirstAlarmTimeMillis = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)
        val MAX_RESULTS_DIFFERENCE_MILLIS = 2000
        Assert.assertTrue(
                "expected millis: $expectedFirstAlarmTimeMillis, actual millis: $actualFirstAlarmTimeMillis, difference: ${expectedFirstAlarmTimeMillis - actualFirstAlarmTimeMillis}",
                abs(expectedFirstAlarmTimeMillis - actualFirstAlarmTimeMillis) < MAX_RESULTS_DIFFERENCE_MILLIS
        )
    }

    @Test
    fun getFirstAlarmTimeMillis_after12Hours() {
        val firstAlarmTime = LocalTime.now().plusHours(12)
        val actualFirstAlarmTimeMillis = TimeFormatter.getAlarmMillisWithin24Hours(firstAlarmTime)
        val expectedFirstAlarmTimeMillis = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(12)
        val MAX_RESULTS_DIFFERENCE_MILLIS = 2000
        Assert.assertTrue(
                "expected millis: $expectedFirstAlarmTimeMillis, actual millis: $actualFirstAlarmTimeMillis, difference: ${expectedFirstAlarmTimeMillis - actualFirstAlarmTimeMillis}",
                abs(expectedFirstAlarmTimeMillis - actualFirstAlarmTimeMillis) < MAX_RESULTS_DIFFERENCE_MILLIS
        )
    }

    @Test
    fun getFirstAlarmTimeMillis_someTimeBeforeCurrentMoment() {
        val firstAlarmTime = LocalTime.now().minusHours(1)
        val actualFirstAlarmTimeMillis = TimeFormatter.getAlarmMillisWithin24Hours(firstAlarmTime)
        val expectedFirstAlarmTimeMillis = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(23)
        val MAX_RESULTS_DIFFERENCE_MILLIS = 2000
        Assert.assertTrue(
                "expected millis: $expectedFirstAlarmTimeMillis, actual millis: $actualFirstAlarmTimeMillis, difference: ${expectedFirstAlarmTimeMillis - actualFirstAlarmTimeMillis}",
                abs(expectedFirstAlarmTimeMillis - actualFirstAlarmTimeMillis) < MAX_RESULTS_DIFFERENCE_MILLIS
        )
    }


}