package com.example.wonhoi_courier_tracking_service.extension

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat


fun Context.color(@ColorRes colorRes: Int) : Int = ContextCompat.getColor(this,colorRes)