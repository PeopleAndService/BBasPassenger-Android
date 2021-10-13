package com.pns.bbaspassenger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pns.bbaspassenger.data.model.BusSystem
import com.pns.bbaspassenger.databinding.ItemRvBusBinding
import com.pns.bbaspassenger.databinding.ItemRvBusstopBinding
import com.pns.bbaspassenger.databinding.ItemRvBusstopExpandBinding

private const val VIEW_TYPE_BUS = 0
private const val VIEW_TYPE_BUSSTOP = 1
private const val VIEW_TYPE_BUSSTOP_EXPAND = 2

class BusSystemAdapter(val todoItemClick: (BusSystem) -> Unit) :
    ListAdapter<BusSystem, RecyclerView.ViewHolder>(BusSystemDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding by lazy {
            when (viewType) {
                VIEW_TYPE_BUS -> ItemRvBusBinding.inflate(layoutInflater, parent, false)
                VIEW_TYPE_BUSSTOP -> ItemRvBusstopBinding.inflate(layoutInflater, parent, false)
                else -> ItemRvBusstopExpandBinding.inflate(layoutInflater, parent, false)
            }
        }

        return when (viewType) {
            VIEW_TYPE_BUS -> BusViewHolder(binding as ItemRvBusBinding)
            VIEW_TYPE_BUSSTOP -> BusStopViewHolder(binding as ItemRvBusstopBinding)
            else -> BusStopExpandViewHolder(binding as ItemRvBusstopExpandBinding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).type) {
            "bus" -> VIEW_TYPE_BUS
            "busStop" -> VIEW_TYPE_BUSSTOP
            else -> VIEW_TYPE_BUSSTOP_EXPAND
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BusViewHolder -> {
                holder.bind(getItem(position))
            }
            is BusStopViewHolder -> {
                holder.bind(getItem(position))
            }
            is BusStopExpandViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    inner class BusViewHolder(private val binding: ItemRvBusBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bus: BusSystem) {
            binding.bus = bus
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                todoItemClick(bus)
            }
        }
    }

    inner class BusStopViewHolder(private val binding: ItemRvBusstopBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(busStop: BusSystem) {
            binding.busStopName = busStop.name + " (" + busStop.busList!!.first().name.split("-").last() + ")"
            binding.busStop = busStop
            binding.rvBusList.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = BusSystemAdapter(todoItemClick)
                setHasFixedSize(true)
            }
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                binding.rvBusList.apply {
                    visibility = if (visibility == View.GONE) View.VISIBLE else View.GONE
                }
            }
        }
    }

    inner class BusStopExpandViewHolder(private val binding: ItemRvBusstopExpandBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bus: BusSystem) {
            binding.busName = bus.name.split("-").joinToString(" - ")
            binding.bus = bus
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                todoItemClick(bus)
            }
        }
    }

    companion object BusSystemDiffUtil : DiffUtil.ItemCallback<BusSystem>() {
        override fun areItemsTheSame(oldItem: BusSystem, newItem: BusSystem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BusSystem, newItem: BusSystem): Boolean {
            return true
        }
    }
}
