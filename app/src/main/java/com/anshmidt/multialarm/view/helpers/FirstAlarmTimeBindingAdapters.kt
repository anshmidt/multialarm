package com.anshmidt.multialarm.view.helpers

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.data.TimeFormatter
import com.anshmidt.multialarm.data.TimeLeft
import com.anshmidt.multialarm.view.helpers.TextViewBindingAdapters.resizePartOfText
import org.threeten.bp.LocalTime

object FirstAlarmTimeBindingAdapters {



    @BindingAdapter("displayMainFirstAlarmTime")
    @JvmStatic
    fun TextView.displayMainFirstAlarmTime(localTime: LocalTime?) {
        if (localTime == null) {
            this.setText("")
            return
        }
        val displayableTime = TimeFormatter.getDisplayableTime(localTime)
        val fullText = this.context.getString(R.string.main_firstalarm_time, displayableTime)
        val partOfTextToResize = displayableTime
        this.resizePartOfText(
                fullText = fullText,
                partOfTextToResize = partOfTextToResize
        )
    }

    @BindingAdapter("displayAllTimeLeft")
    @JvmStatic
    fun TextView.displayAllTimeLeft(timeLeft: TimeLeft?) {
        if (timeLeft == null) {
            this.setText("")
            return
        }
        val text = this.context.getString(R.string.all_time_left, timeLeft.hours, timeLeft.minutes)
        this.setText(text)
    }
}