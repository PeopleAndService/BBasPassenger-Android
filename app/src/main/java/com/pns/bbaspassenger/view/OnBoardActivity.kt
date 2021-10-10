package com.pns.bbaspassenger.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pns.bbaspassenger.databinding.ActivityOnBoardBinding
import com.pns.bbaspassenger.viewmodel.OnBoardViewModel

class OnBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardBinding
    private val viewModel: OnBoardViewModel by viewModels()

    private lateinit var mRouteAdapter: OnBoardRouteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        initRcView()
    }

    private fun initRcView() {
        binding.rcvRoute.apply {
            layoutManager = LinearLayoutManager(this@OnBoardActivity)
            setHasFixedSize(true)
            mRouteAdapter = OnBoardRouteAdapter()
            adapter = mRouteAdapter
        }
    }
}