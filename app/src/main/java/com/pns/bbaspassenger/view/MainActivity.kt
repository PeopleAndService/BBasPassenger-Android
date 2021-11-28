package com.pns.bbaspassenger.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.adapters.BusSystemAdapter
import com.pns.bbaspassenger.data.model.BusSystem
import com.pns.bbaspassenger.databinding.ActivityMainBinding
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import com.pns.bbaspassenger.view.dialogs.UserInfoDialog
import com.pns.bbaspassenger.viewmodel.MainViewModel

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        setSupportActionBar(binding.tbMain)
        initRecyclerView()
        refresh()

        if (BBasGlobalApplication.prefs.getString("emergencyNumber") == "" || BBasGlobalApplication.prefs.getString("location") == "0") {
            UserInfoDialog().show(supportFragmentManager, "user info dialog")
        }

        binding.etSearch.setOnEditorActionListener { _, action, _ ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_SEARCH) {
                requestLocation { latitude, longitude ->
                    viewModel.search(
                        latitude,
                        longitude,
                        binding.etSearch.text.toString()
                    )
                }

                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                handled = true
            }

            handled
        }
    }

    private fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.view = this
        binding.viewModel = viewModel
        setContentView(binding.root)
    }

    private fun initRecyclerView() {
        binding.rvBusSystem.apply {
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            adapter = BusSystemAdapter { startStationId, route -> moveToReservation(startStationId, route) }
            setHasFixedSize(true)
        }
    }

    fun refresh() {
        requestLocation { latitude, longitude ->
            viewModel.requestStationByLocation(latitude, longitude)
            binding.etSearch.text.clear()
        }
    }

    private fun moveToReservation(startStationId: String?, route: BusSystem) {
        Intent(applicationContext, ReservationActivity::class.java).run {
            putExtra("route", route)
            putExtra("startStationId", startStationId)
            startActivity(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_my_page -> {
                Intent(this, MyPageActivity::class.java).run { startActivity(this) }
                true
            }
            R.id.btn_emergency -> {
                sendEmergencyMessage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}