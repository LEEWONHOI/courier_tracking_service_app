package com.example.wonhoi_courier_tracking_service.extension

import android.widget.TextView
import androidx.annotation.ColorRes

fun TextView.setTextColorRes(@ColorRes colorResId: Int) {
    setTextColor(color(colorResId))
}