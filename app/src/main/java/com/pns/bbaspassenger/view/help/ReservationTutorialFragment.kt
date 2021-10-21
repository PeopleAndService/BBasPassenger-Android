package com.pns.bbaspassenger.view.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.databinding.FragmentTutorialBinding

class ReservationTutorialFragment(private val position: Int) : Fragment() {
    private lateinit var binding: FragmentTutorialBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTutorialBinding.inflate(inflater, container, false)

        when (position) {
            0 -> {
                Glide.with(this).load(R.raw.reservation1).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.reservation_content1)
            }
            1 -> {
                Glide.with(this).load(R.raw.reservation2).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.reservation_content2)
            }
            2 -> {
                Glide.with(this).load(R.raw.reservation3).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.reservation_content3)
            }
            3 -> {
                Glide.with(this).load(R.raw.reservation4).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.reservation_content4)
            }
            4 -> {
                Glide.with(this).load(R.raw.reservation5).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.reservation_content5)
            }
            5 -> {
                Glide.with(this).load(R.raw.reservation6).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.reservation_content6)
            }
        }

        return binding.root
    }
}