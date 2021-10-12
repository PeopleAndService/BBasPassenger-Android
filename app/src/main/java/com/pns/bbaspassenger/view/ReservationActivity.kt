package com.pns.bbaspassenger.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.data.model.BusSystem
import com.pns.bbaspassenger.data.model.RouteItemModel
import com.pns.bbaspassenger.databinding.ActivityReservationBinding
import com.pns.bbaspassenger.viewmodel.ReservationViewModel

class ReservationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationBinding
    private val viewModel: ReservationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initActionBar()
        initRecyclerView()
    }

    private fun initBinding() {
        binding = ActivityReservationBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        intent.getParcelableExtra<BusSystem>("route").let { route ->
            route!!.name.split('(').let { name ->
                binding.routeNum = name[0] + "번"
                binding.route = name[name.lastIndex].dropLast(1)
            }

            viewModel.getBusRoute(route.id)
        }

        setContentView(binding.root)
    }

    private fun initActionBar() {
        setSupportActionBar(binding.tbReservation)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun initRecyclerView() {
        binding.rvRoute.apply {
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            adapter = OnBoardRouteAdapter(1) { reservation -> viewModel.makeReservation(reservation) }
            setHasFixedSize(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar_reservation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.btn_emergency -> {
                //TODO 비상 연락
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}