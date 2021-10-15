package com.pns.bbaspassenger.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.databinding.FragmentTutorialBinding

class MyPageTutorialFragment(private val position: Int) : Fragment() {
    private lateinit var binding: FragmentTutorialBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTutorialBinding.inflate(inflater, container, false)

        when (position) {
            0 -> {
                Glide.with(this).load(R.raw.mypage1).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.mypage_content1)
            }
            1 -> {
                Glide.with(this).load(R.raw.mypage2).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.mypage_content2)
            }
            2 -> {
                Glide.with(this).load(R.raw.mypage3).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.mypage_content3)
            }
        }

        return binding.root
    }
}