package com.anshmidt.multialarm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anshmidt.multialarm.data.TimeFormatter
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.Duration

class TimeFormattingTest {
    // Allows to set livedata
    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun normalizeDuration_negative() {
        val duration = Duration.ofHours(-22)
        val expectedNormalizedDuration = Duration.ofHours(2)
        val actualNormalizedDuration = TimeFormatter.normalizeDurationByAddingOrSubtractingDays(duration)
        Assert.assertEquals(expectedNormalizedDuration, actualNormalizedDuration)
    }

    @Test
    fun normalizeDuration_alreadyNormalized() {
        val duration = Duration.ofMinutes(123)
        val expectedNormalizedDuration = Duration.ofMinutes(123)
        val actualNormalizedDuration = TimeFormatter.normalizeDurationByAddingOrSubtractingDays(duration)
        Assert.assertEquals(expectedNormalizedDuration, actualNormalizedDuration)
    }

    @Test
    fun normalizeDuration_moreThanDay() {
        val duration = Duration.ofHours(51)
        val expectedNormalizedDuration = Duration.ofHours(3)
        val actualNormalizedDuration = TimeFormatter.normalizeDurationByAddingOrSubtractingDays(duration)
        Assert.assertEquals(expectedNormalizedDuration, actualNormalizedDuration)
    }
}