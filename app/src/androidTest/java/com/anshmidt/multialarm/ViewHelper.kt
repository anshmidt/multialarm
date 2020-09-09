package com.anshmidt.multialarm

import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher
import org.threeten.bp.LocalTime
import java.lang.RuntimeException

object ViewHelper {

    fun getTextFromTextView(matcher: ViewInteraction): String {
        var text = String()
        matcher.perform(object : ViewAction {

            override fun getConstraints(): org.hamcrest.Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView
                text = tv.text.toString()
            }
        })

        return text
    }

    fun setTimeOnTimePicker(hour: Int, minute: Int, matcher: ViewInteraction) {
        matcher.perform(object : ViewAction {

            override fun getConstraints(): org.hamcrest.Matcher<View> {
                return isAssignableFrom(TimePicker::class.java)
            }

            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun perform(uiController: UiController, view: View) {
                val timePicker = view as TimePicker
                timePicker.hour = hour
                timePicker.minute = minute
            }
        })
    }

    fun getTimeFromTimePicker(matcher: ViewInteraction): LocalTime {
        var time = LocalTime.MIDNIGHT
        matcher.perform(object : ViewAction {

            override fun getConstraints(): org.hamcrest.Matcher<View> {
                return isAssignableFrom(TimePicker::class.java)
            }

            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun perform(uiController: UiController, view: View) {
                val timePicker = view as TimePicker
                val hour = timePicker.hour
                val minute = timePicker.minute
                time = LocalTime.of(hour, minute)
            }
        })

        return time
    }

    fun setValueOnNumberPicker(value: Int, matcher: ViewInteraction) {
        matcher.perform(object : ViewAction {
            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(NumberPicker::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                val numberPicker = view as NumberPicker
                numberPicker.value = value
            }
        })
    }

    fun getValueFromNumberPicker(matcher: ViewInteraction): Int {
        var value: Int? = null
        matcher.perform(object : ViewAction {
            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(NumberPicker::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                val numberPicker = view as NumberPicker
                value = numberPicker.value
            }
        })

        return value ?: throw RuntimeException("Cannot get value from NumberPicker")
    }

}