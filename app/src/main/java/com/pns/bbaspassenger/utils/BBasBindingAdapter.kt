package com.pns.bbaspassenger.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.RecyclerView
import com.pns.bbaspassenger.data.model.RouteItemModel
import com.pns.bbaspassenger.view.OnBoardRouteAdapter

object BBasBindingAdapter {
    @BindingConversion
    @JvmStatic
    fun convertBooleanToVisibility(visible: Boolean): Int {
        return if (visible) View.GONE else View.VISIBLE
    }

    @BindingAdapter("routeData")
    @JvmStatic
    fun bindRouteRcView(recyclerView: RecyclerView, routeList: List<RouteItemModel>?) {
        val adapter = recyclerView.adapter as OnBoardRouteAdapter
        routeList?.let {
            adapter.submitList(it)
        }
    }
}