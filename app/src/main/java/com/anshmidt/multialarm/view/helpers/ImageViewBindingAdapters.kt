package com.anshmidt.multialarm.view.helpers

import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter

object ImageViewBindingAdapters {
    @JvmStatic
    @BindingAdapter("app:tint")
    fun ImageView.setImageTint(@ColorInt color: Int) {
        setColorFilter(color)
    }
}