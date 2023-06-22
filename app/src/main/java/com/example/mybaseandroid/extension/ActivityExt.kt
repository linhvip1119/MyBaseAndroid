package com.example.mybaseandroid.extension

import android.app.Activity
import android.graphics.Insets
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowInsets

fun Activity.deviceSize(): Size {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return Size(displayMetrics.widthPixels, displayMetrics.heightPixels)
    } else {
        val metrics = windowManager.currentWindowMetrics

        // Legacy size that Display#getSize reports
        val bounds: Rect = metrics.bounds

        // Gets all excluding insets
        val windowInsets = metrics.windowInsets
        val insets: Insets = windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.navigationBars()
                    or WindowInsets.Type.displayCutout()
        )
        val insetsWidth: Int = insets.right + insets.left
        val insetsHeight: Int = insets.top + insets.bottom

        return Size(bounds.width() - insetsWidth, bounds.height() - insetsHeight)
    }
}

fun Activity.deviceWidth(): Int = deviceSize().width
fun Activity.deviceHeight(): Int = deviceSize().height