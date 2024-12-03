package com.anshmidt.multialarm.view.helpers

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.anshmidt.multialarm.R
import com.anshmidt.multialarm.data.TimeFormatter
import com.anshmidt.multialarm.data.TimeLeft
import com.anshmidt.multialarm.view.helpers.TextViewBindingAdapters.resizePartOfText
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.TextStyle
import java.util.Locale

object FirstAlarmTimeBindingAdapters {



    @BindingAdapter("displayMainFirstAlarmTime")
    @JvmStatic
    fun TextView.displayMainFirstAlarmTime(localDateTime: LocalDateTime?) {
        if (localDateTime == null) {
            this.setText("")
            return
        }

        val displayableTime = TimeFormatter.getDisplayableTime(localDateTime.toLocalTime())
        val dayOfWeek = localDateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val fullText = this.context.getString(R.string.main_firstalarm_time, displayableTime, dayOfWeek)
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