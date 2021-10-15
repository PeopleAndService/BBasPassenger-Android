package com.pns.bbaspassenger.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.data.model.BusLocation
import com.pns.bbaspassenger.databinding.ItemRvBusSelectBinding

class BusSelectAdapter(private val start: Int, private val onClickItem: (BusLocation) -> Unit) :
    ListAdapter<BusLocation, BusSelectAdapter.ViewHolder>(BusSelectDiffUtil) {
    private val viewHolderList = ArrayList<ViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRvBusSelectBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_rv_bus_select
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder !in viewHolderList) viewHolderList.add(holder)
        holder.bind(getItem(position))
    }

    inner class ViewHolder(val binding: ItemRvBusSelectBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(busLocation: BusLocation) {
            binding.busLocation = busLocation
            binding.busNo = busLocation.vehicleId.takeLast(4)
            binding.busArrival =
                binding.root.context.getString(R.string.bus_arrival_count_format, (start + 1) - busLocation.nodeOrder)
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                onClickItem(busLocation)
                binding.cvItem.setStrokeColor(binding.root.context.getColorStateList(R.color.state_bus_select))
                binding.root.contentDescription = binding.root.context.getString(
                    R.string.des_check_reservation_bus,
                    busLocation.nodeName,
                    busLocation.vehicleId.takeLast(4),
                    binding.root.context.getString(
                        R.string.bus_arrival_count_format,
                        (start + 1) - busLocation.nodeOrder
                    )
                )
                for (i in 0 until itemCount) {
                    if (viewHolderList[i] != this) viewHolderList[i].binding.cvItem.setCardBackgroundColor(
                        ContextCompat.getColor(binding.root.context, R.color.colorBackgroundElevated)
                    )
                }
            }
        }
    }

    companion object BusSelectDiffUtil : DiffUtil.ItemCallback<BusLocation>() {
        override fun areItemsTheSame(oldItem: BusLocation, newItem: BusLocation): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BusLocation, newItem: BusLocation): Boolean {
            return true
        }
    }
}