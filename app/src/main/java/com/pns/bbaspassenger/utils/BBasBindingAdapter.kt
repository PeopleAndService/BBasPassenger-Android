package com.pns.bbaspassenger.utils

import android.view.View
import androidx.databinding.BindingConversion

object BBasBindingAdapter {
    @BindingConversion
    @JvmStatic
    fun convertBooleanToVisibility(visible: Boolean): Int {
        return if (visible) View.GONE else View.VISIBLE
    }
}