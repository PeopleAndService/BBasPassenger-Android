package com.pns.bbaspassenger.view.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.databinding.FragmentTutorialBinding

class MainTutorialFragment(private val position: Int) : Fragment() {
    private lateinit var binding: FragmentTutorialBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTutorialBinding.inflate(inflater, container, false)

        when (position) {
            0 -> {
                Glide.with(this).load(R.raw.search1).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.search_content1)
            }
            1 -> {
                Glide.with(this).load(R.raw.search2).into(binding.ivTutorial)
                binding.tvContent.text = getString(R.string.search_content2)
            }
        }

        return binding.root
    }
}