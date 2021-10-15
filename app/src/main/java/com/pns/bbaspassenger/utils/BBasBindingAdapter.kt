package com.pns.bbaspassenger.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.RecyclerView
import com.pns.bbaspassenger.adapters.BusSelectAdapter
import com.pns.bbaspassenger.adapters.BusSystemAdapter
import com.pns.bbaspassenger.data.model.BusLocation
import com.pns.bbaspassenger.data.model.BusSystem

object BBasBindingAdapter {
    @BindingConversion
    @JvmStatic
    fun convertBooleanToVisibility(visible: Boolean): Int {
        return if (visible) View.GONE else View.VISIBLE
    }

    @BindingConversion
    @JvmStatic
    fun convertIntToVisibility(visible: Int): Int {
        return if (visible == 0) View.VISIBLE else View.GONE
    }

    @BindingAdapter("busSystemData")
    @JvmStatic
    fun bindBusSystem(recyclerView: RecyclerView, busSystemData: List<BusSystem>?) {
        val adapter = recyclerView.adapter as BusSystemAdapter
        adapter.submitList(busSystemData)
    }

    @BindingAdapter("busLocationData")
    @JvmStatic
    fun bindBusLocation(recyclerView: RecyclerView, busLocationData: List<BusLocation>?) {
        val adapter = recyclerView.adapter as BusSelectAdapter
        adapter.submitList(busLocationData)
    }
}