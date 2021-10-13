package com.pns.bbaspassenger.view

import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.data.model.ReservationRouteItem
import com.pns.bbaspassenger.databinding.ItemReservationRouteBinding

class ReservationRouteAdapter(val click: (Int) -> Unit) :
    RecyclerView.Adapter<ReservationRouteAdapter.ReservationRouteViewHolder>() {
    private val itemList: MutableList<ReservationRouteItem> = mutableListOf()

    private var startPos = -1
    private var endPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationRouteViewHolder {
        val binding = ItemReservationRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ReservationRouteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationRouteViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount() = itemList.size

    fun setList(list: MutableList<ReservationRouteItem>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun setStart(position: Int) {
        itemList[position].isStart = true
        notifyItemChanged(position)
        startPos = position
    }

    fun clearStart() {
        itemList[startPos].isStart = false
        notifyItemChanged(startPos)
        startPos = -1
    }

    fun setEnd(position: Int) {
        itemList[position].isEnd = true
        endPos = position

        if (startPos != -1) {
            for (i in startPos + 1 until position) {
                itemList[i].isDuring = true
            }
        }

        notifyItemRangeChanged(startPos, endPos - startPos + 1)
    }

    fun clearEnd() {
        itemList[endPos].isEnd = false
        if (startPos != -1) {
            for (i in startPos + 1 until endPos) {
                itemList[i].isDuring = false
            }
        }

        notifyItemRangeChanged(startPos, endPos - startPos + 1)
        endPos = -1
    }

    fun getStart(): ReservationRouteItem? {
        return if (startPos != -1) {
            itemList[startPos]
        } else {
            null
        }
    }

    fun getEnd(): ReservationRouteItem? {
        return if (endPos != -1) {
            itemList[endPos]
        } else {
            null
        }
    }

    inner class ReservationRouteViewHolder(val binding: ItemReservationRouteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReservationRouteItem) {
            binding.data = item

            binding.root.setOnClickListener {
                click(adapterPosition)
            }

            if (item.isStart) {
                binding.lineStart.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorText))
                binding.lineStart.layoutParams?.let {
                    it.width = dpToPx(2.0F, binding.root.context.resources)
                }
                binding.icStation.borderColor = ContextCompat.getColor(binding.root.context, R.color.colorSecondary)
                binding.icStation.circleBackgroundColor =
                    ContextCompat.getColor(binding.root.context, R.color.stationBackGround)
                binding.icStation.layoutParams?.let {
                    it.width = dpToPx(32.0F, binding.root.context.resources)
                }
                Glide.with(binding.icStation).load(R.drawable.ic_bus_arrive).into(binding.icStation)
                if (adapterPosition != itemCount - 1 && !itemList[adapterPosition + 1].isDuring && !itemList[adapterPosition + 1].isEnd) {
                    binding.lineEnd.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorText))
                    binding.lineEnd.layoutParams?.let {
                        it.width = dpToPx(2.0F, binding.root.context.resources)
                    }
                } else {
                    binding.lineEnd.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                    binding.lineEnd.layoutParams?.let {
                        it.width = dpToPx(16.0F, binding.root.context.resources)
                    }
                }
            } else if (item.isDuring) {
                binding.lineStart.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                binding.lineStart.layoutParams?.let {
                    it.width = dpToPx(16.0F, binding.root.context.resources)
                }
                binding.lineEnd.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                binding.lineEnd.layoutParams?.let {
                    it.width = dpToPx(16.0F, binding.root.context.resources)
                }
                binding.icStation.borderColor = ContextCompat.getColor(binding.root.context, R.color.colorSecondary)
                binding.icStation.circleBackgroundColor =
                    ContextCompat.getColor(binding.root.context, R.color.stationBackGround)
                binding.icStation.layoutParams?.let {
                    it.width = dpToPx(16.0F, binding.root.context.resources)
                }
                binding.icStation.setImageDrawable(ContextCompat.getDrawable(binding.root.context, android.R.color.transparent))
            } else if (item.isEnd) {
                binding.lineStart.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                binding.lineStart.layoutParams?.let {
                    it.width = dpToPx(16.0F, binding.root.context.resources)
                }
                binding.lineEnd.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorText))
                binding.lineEnd.layoutParams?.let {
                    it.width = dpToPx(2.0F, binding.root.context.resources)
                }
                binding.icStation.borderColor = ContextCompat.getColor(binding.root.context, R.color.colorSecondary)
                binding.icStation.circleBackgroundColor =
                    ContextCompat.getColor(binding.root.context, R.color.stationBackGround)
                binding.icStation.layoutParams?.let {
                    it.width = dpToPx(32.0F, binding.root.context.resources)
                }
                Glide.with(binding.icStation).load(R.drawable.ic_bus_arrive).into(binding.icStation)
            } else {
                binding.lineStart.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorText))
                binding.lineStart.layoutParams?.let {
                    it.width = dpToPx(2.0F, binding.root.context.resources)
                }
                binding.lineEnd.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorText))
                binding.lineEnd.layoutParams?.let {
                    it.width = dpToPx(2.0F, binding.root.context.resources)
                }
                binding.icStation.borderColor = ContextCompat.getColor(binding.root.context, R.color.colorText)
                binding.icStation.circleBackgroundColor =
                    ContextCompat.getColor(binding.root.context, R.color.stationBackGround)
                binding.icStation.layoutParams?.let {
                    it.width = dpToPx(32.0F, binding.root.context.resources)
                }
                binding.icStation.setImageDrawable(ContextCompat.getDrawable(binding.root.context, android.R.color.transparent))
            }
        }

        private fun dpToPx(dp: Float, resources: Resources): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
        }
    }
}