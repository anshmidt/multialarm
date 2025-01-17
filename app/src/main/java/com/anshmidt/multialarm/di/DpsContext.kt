package com.anshmidt.multialarm.di

import android.content.Context
import android.os.Build

/**
 * This context is needed for access to alarm settings when device rebooted
 */
class DpsContext(context: Context) {

    val context = getDpsContext(context)

    companion object {
        fun getDpsContext(context: Context) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createDeviceProtectedStorageContext()
        } else {
            context
        }
    }
}