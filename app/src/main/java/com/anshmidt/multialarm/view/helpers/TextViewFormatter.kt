package com.anshmidt.multialarm.view.helpers

import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter

object TextViewFormatter {

    @BindingAdapter("fullText", "partOfTextToResize")
    @JvmStatic
    fun resizePartOfText(textView: TextView, fullText: String, partOfTextToResize: String) {
        if (partOfTextToResize.isEmpty()) return
        if (fullText.isEmpty()) return

        val resizeTextProportion = 2f
        val spannable = SpannableString(fullText)
        val partPosition = fullText.indexOf(partOfTextToResize)
        val partLength = partOfTextToResize.length
        spannable.setSpan(
                RelativeSizeSpan(resizeTextProportion),
                partPosition,
                partPosition + partLength,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.setText(spannable)
    }

}