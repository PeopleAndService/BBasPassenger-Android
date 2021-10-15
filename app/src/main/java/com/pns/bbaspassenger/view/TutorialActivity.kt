package com.pns.bbaspassenger.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pns.bbaspassenger.databinding.ActivityTutorialBinding

class TutorialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTutorialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        intent.getIntExtra("type", -1).let {
            if (it != -1) {
                initViewPager(it)
            }
        }
    }

    private fun initViewPager(type: Int) {
        when (type) {
            0 -> {
                binding.pagerTutorial.apply {
                    adapter = MainTutorialAdapter(this@TutorialActivity)
                    offscreenPageLimit = 2
                }
            }
            1 -> {
                binding.pagerTutorial.apply {
                    adapter = ReservationTutorialAdapter(this@TutorialActivity)
                    offscreenPageLimit = 6
                }
            }
            2 -> {
                binding.pagerTutorial.apply {
                    adapter = OnBoardTutorialAdapter(this@TutorialActivity)
                    offscreenPageLimit = 6
                }
            }
            3 -> {
                binding.pagerTutorial.apply {
                    adapter = MyPageTutorialAdapter(this@TutorialActivity)
                    offscreenPageLimit = 3
                }
            }
        }
        binding.indicatorTutorial.setViewPager2(binding.pagerTutorial)
    }

    inner class MainTutorialAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount() = 2

        override fun createFragment(position: Int) = MainTutorialFragment(position)
    }

    inner class ReservationTutorialAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount() = 6
        override fun createFragment(position: Int) = ReservationTutorialFragment(position)
    }

    inner class OnBoardTutorialAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount() = 6
        override fun createFragment(position: Int) = OnBoardTutorialFragment(position)
    }

    inner class MyPageTutorialAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount() = 3
        override fun createFragment(position: Int) = MyPageTutorialFragment(position)
    }
}