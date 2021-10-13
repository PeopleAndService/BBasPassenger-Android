package com.pns.bbaspassenger.view

import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.data.model.RouteItemModel
import com.pns.bbaspassenger.databinding.ItemRouteBinding

class OnBoardRouteAdapter() : RecyclerView.Adapter<OnBoardRouteAdapter.OnBoardRouteViewHolder>() {
    private val itemList: MutableList<RouteItemModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardRouteAdapter.OnBoardRouteViewHolder {
        val binding = ItemRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return OnBoardRouteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardRouteAdapter.OnBoardRouteViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setList(list: MutableList<RouteItemModel>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    inner class OnBoardRouteViewHolder(val binding: ItemRouteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(routeItemModel: RouteItemModel) {
            binding.data = routeItemModel

            if (routeItemModel.isStart) {
                if (!routeItemModel.isBusHere) {
                    if (routeItemModel.remainSec == 0) {
                        if (routeItemModel.remainCnt == 0) {
                            binding.tvArrival.text = binding.root.context.getString(R.string.bus_arrival_info_not_found)
                        } else {
                            binding.tvArrival.text = binding.root.context.getString(
                                R.string.bus_arrival_count_format,
                                routeItemModel.remainCnt
                            )
                        }
                    } else if (routeItemModel.remainSec <= 180) {
                        binding.tvArrival.text =
                            binding.root.context.getString(R.string.bus_arrival_soon_format, routeItemModel.remainCnt)
                    } else {
                        binding.tvArrival.text = binding.root.context.getString(
                            R.string.bus_arrival_info_format,
                            routeItemModel.remainSec / 60,
                            routeItemModel.remainCnt
                        )
                    }
                }

                binding.lineEnd.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                binding.lineEnd.layoutParams?.let {
                    it.width = dpToPx(16.0F, binding.root.context.resources)
                }
                binding.icStation.borderColor = ContextCompat.getColor(binding.root.context, R.color.colorSecondary)
                binding.icStation.circleBackgroundColor =
                    ContextCompat.getColor(binding.root.context, R.color.stationBackGround)
            } else if (routeItemModel.isDuring) {
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
                binding.icStation.circleBackgroundColor =
                    ContextCompat.getColor(binding.root.context, R.color.stationBackGround)
            } else if (routeItemModel.isEnd) {
                binding.lineEnd.visibility = View.GONE
                binding.lineStart.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                binding.lineStart.layoutParams?.let {
                    it.width = dpToPx(16.0F, binding.root.context.resources)
                }
                binding.icStation.borderColor = ContextCompat.getColor(binding.root.context, R.color.colorSecondary)
                binding.icStation.circleBackgroundColor =
                    ContextCompat.getColor(binding.root.context, R.color.stationBackGround)
                binding.icStation.layoutParams?.let {
                    it.width = dpToPx(32.0F, binding.root.context.resources)
                }
            }

            if (routeItemModel.isBusHere) {
                binding.icStation.layoutParams?.let {
                    it.width = dpToPx(32.0F, binding.root.context.resources)
                }

                if (routeItemModel.isStart || routeItemModel.isDuring || routeItemModel.isEnd) {
                    Glide.with(binding.icStation).load(R.drawable.ic_bus_arrive).into(binding.icStation)
                    binding.icStation.borderColor = ContextCompat.getColor(binding.root.context, R.color.colorSecondary)
                } else {
                    Glide.with(binding.icStation).load(R.drawable.ic_bus_unarrive).into(binding.icStation)
                    binding.icStation.borderColor = ContextCompat.getColor(binding.root.context, R.color.colorWarning)
                }
            } else {
                binding.icStation.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.root.context,
                        android.R.color.transparent
                    )
                )
            }
        }

        private fun dpToPx(dp: Float, resources: Resources): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
        }
    }
}