package com.anshmidt.multialarm.view

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter

object FirstAlarmTimeFormatter {

    @BindingAdapter("fullText", "importantPartOfText")
    @JvmStatic
    fun format(textView: TextView, fullText: String, importantPartOfText: String) {
        val spannable = SpannableString(fullText)
        val importantTextPosition = fullText.indexOf(importantPartOfText)
        val importantTextLength = importantPartOfText.length
        spannable.setSpan(
                RelativeSizeSpan(2f),
                importantTextPosition,
                importantTextPosition + importantTextLength,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.setText(spannable)

    }

}