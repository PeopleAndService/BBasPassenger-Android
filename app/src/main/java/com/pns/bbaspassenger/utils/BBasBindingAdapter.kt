package com.pns.bbaspassenger.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.RecyclerView
import com.pns.bbaspassenger.adapters.BusSystemAdapter
import com.pns.bbaspassenger.data.model.BusSystem

object BBasBindingAdapter {
    @BindingConversion
    @JvmStatic
    fun convertBooleanToVisibility(visible: Boolean): Int {
        return if (visible) View.GONE else View.VISIBLE
    }

    @BindingAdapter("busSystemData")
    @JvmStatic
    fun bindBusSystem(recyclerView: RecyclerView, busSystemData: ArrayList<BusSystem>) {
        val adapter = recyclerView.adapter as BusSystemAdapter
        adapter.submitList(busSystemData)
    }
}