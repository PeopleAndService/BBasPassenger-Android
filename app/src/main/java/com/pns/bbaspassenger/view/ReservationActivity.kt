package com.pns.bbaspassenger.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.data.model.BusSystem
import com.pns.bbaspassenger.databinding.ActivityReservationBinding
import com.pns.bbaspassenger.viewmodel.ReservationViewModel

class ReservationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationBinding
    private val viewModel: ReservationViewModel by viewModels()

    private lateinit var mAdapter: ReservationRouteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        setObserver()
        initActionBar()
        initRecyclerView()
    }

    private fun initBinding() {
        binding = ActivityReservationBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        intent.getParcelableExtra<BusSystem>("route").let { route ->
            route!!.name.split('-').let { name ->
                binding.routeNum = name[0] + "번"
                binding.route = name[name.lastIndex]
            }

            viewModel.getBusRoute(route.id)
        }

        binding.btnReservation.setOnClickListener {
            Log.d("TAG", "start : ${mAdapter.getStart()}, end : ${mAdapter.getEnd()}")
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
            mAdapter = ReservationRouteAdapter() {
                onClickSelect(it)
            }
            adapter = mAdapter
            setHasFixedSize(true)
        }
    }

    private fun setObserver() {
        viewModel.routeItemList.observe(this) {
            mAdapter.setList(it)
        }

        viewModel.startPos.observe(this) {
            if (it == null) {
                mAdapter.clearStart()
            } else {
                mAdapter.setStart(it)
            }
        }

        viewModel.endPos.observe(this) {
            if (it == null) {
                mAdapter.clearEnd()
            } else {
                mAdapter.setEnd(it)
            }
        }

        viewModel.startSelect.observe(this) {
            it.getContentIfNotHandled()?.let { res ->
                when(res) {
                    "startIsLast" -> {
                        MaterialAlertDialogBuilder(this)
                            .setTitle(getString(R.string.start_station_error_title))
                            .setMessage(getString(R.string.start_station_last_error_message))
                            .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .setCancelable(false)
                            .show()
                    }
                    "success" -> {
                        Toast.makeText(this, getString(R.string.start_station_select), Toast.LENGTH_SHORT).show()
                    }
                    "clearStart" -> {
                        Toast.makeText(this, getString(R.string.start_station_unselect), Toast.LENGTH_SHORT).show()
                    }
                    "clearEndFirst" -> {
                        MaterialAlertDialogBuilder(this)
                            .setTitle(getString(R.string.station_select_error_title))
                            .setMessage(getString(R.string.station_clear_end_error_message))
                            .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .setCancelable(false)
                            .show()
                    }
                }
            }
        }

        viewModel.endSelect.observe(this) {
            it.getContentIfNotHandled()?.let { res ->
                when(res) {
                    "endBeforeStart" -> {
                        MaterialAlertDialogBuilder(this)
                            .setTitle(getString(R.string.end_station_error_title))
                            .setMessage(getString(R.string.end_station_before_start))
                            .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .setCancelable(false)
                            .show()
                    }
                    "success" -> {
                        Toast.makeText(this, getString(R.string.end_station_select), Toast.LENGTH_SHORT).show()
                        binding.btnReservation.visibility = View.VISIBLE
                    }
                    "clearEnd" -> {
                        Toast.makeText(this, getString(R.string.end_station_unselect), Toast.LENGTH_SHORT).show()
                        binding.btnReservation.visibility = View.GONE
                    }
                    "fullSelected" -> {
                        MaterialAlertDialogBuilder(this)
                            .setTitle(getString(R.string.station_full_select_title))
                            .setMessage(getString(R.string.station_full_select_message))
                            .setPositiveButton(getString(R.string.btn_confirm)) { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .setCancelable(false)
                            .show()
                    }
                }
            }
        }
    }

    private fun onClickSelect(position: Int) {
        viewModel.handleSelect(position)
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