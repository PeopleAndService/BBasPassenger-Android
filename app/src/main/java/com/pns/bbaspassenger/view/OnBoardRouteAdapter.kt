package com.pns.bbaspassenger.view

import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.data.model.RouteItemModel
import com.pns.bbaspassenger.databinding.ItemRouteBinding

class OnBoardRouteAdapter : ListAdapter<RouteItemModel, OnBoardRouteAdapter.OnBoardRouteViewHolder>(RouteDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardRouteViewHolder {
        val binding = ItemRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return OnBoardRouteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardRouteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class OnBoardRouteViewHolder(val binding: ItemRouteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(routeItemModel: RouteItemModel) {
            with(routeItemModel) {
                binding.data = this

                if (this.isStart) {
                    binding.lineEnd.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                    binding.lineEnd.layoutParams?.let {
                        it.width = dpToPx(16.0F, binding.root.context.resources)
                    }
                    binding.icStation.borderColor = ContextCompat.getColor(binding.root.context, R.color.colorSecondary)
                } else if (this.isDuring) {
                    binding.lineStart.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                    binding.lineStart.layoutParams?.let {
                        it.width = dpToPx(16.0F, binding.root.context.resources)
                    }
                    binding.lineEnd.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                    binding.lineEnd.layoutParams?.let {
                        it.width = dpToPx(16.0F, binding.root.context.resources)
                    }
                    binding.icStation.borderColor = ContextCompat.getColor(binding.root.context, R.color.colorSecondary)
                    binding.icStation.layoutParams?.let {
                        it.width = dpToPx(16.0F, binding.root.context.resources)
                    }
                } else if (this.isEnd) {
                    binding.lineStart.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                    binding.lineStart.layoutParams?.let {
                        it.width = dpToPx(16.0F, binding.root.context.resources)
                    }
                    binding.icStation.borderColor = ContextCompat.getColor(binding.root.context, R.color.colorSecondary)
                }

                if (this.isBusHere) {
                    binding.icStation.layoutParams?.let {
                        it.width = dpToPx(32.0F, binding.root.context.resources)
                    }

                    if (this.isStart || this.isDuring || this.isEnd) {
                        Glide.with(binding.icStation).load(R.drawable.ic_bus_arrive).into(binding.icStation)
                        binding.icStation.borderColor = ContextCompat.getColor(binding.root.context, R.color.colorSecondary)
                    } else {
                        Glide.with(binding.icStation).load(R.drawable.ic_bus_unarrive).into(binding.icStation)
                        binding.icStation.borderColor = ContextCompat.getColor(binding.root.context, R.color.colorWarning)
                    }
                }
            }
        }

        private fun dpToPx(dp: Float, resources: Resources): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
        }
    }

    companion object RouteDiffUtil : DiffUtil.ItemCallback<RouteItemModel>() {
        override fun areContentsTheSame(oldItem: RouteItemModel, newItem: RouteItemModel): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: RouteItemModel, newItem: RouteItemModel): Boolean {
            return oldItem.nodeId == oldItem.nodeId
        }
    }
}