package com.pns.bbaspassenger.view.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.databinding.FragmentTutorialBinding

class OnBoardTutorialFragment(private val position: Int) : Fragment() {
    private lateinit var binding: FragmentTutorialBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTutorialBinding.inflate(inflater, container, false)

        when (position) {
            0 -> {
                Glide.with(this).load(R.raw.onboard1).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.onboard_content1)
            }
            1 -> {
                Glide.with(this).load(R.raw.onboard2).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.onboard_content2)
            }
            2 -> {
                Glide.with(this).load(R.raw.onboard3).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.onboard_content3)
            }
            3 -> {
                Glide.with(this).load(R.raw.onboard4).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.onboard_content4)
            }
            4 -> {
                Glide.with(this).load(R.raw.onboard5).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.onboard_content5)
            }
            5 -> {
                Glide.with(this).load(R.raw.onboard6).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.onboard_content6)
            }
        }

        return binding.root
    }
}