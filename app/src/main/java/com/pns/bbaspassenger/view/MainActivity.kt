package com.pns.bbaspassenger.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.adapters.BusSystemAdapter
import com.pns.bbaspassenger.data.model.BusSystem
import com.pns.bbaspassenger.databinding.ActivityMainBinding
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import com.pns.bbaspassenger.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        setSupportActionBar(binding.tbMain)
        initRecyclerView()
        initLocationClient()
        requestLocation()

        if (BBasGlobalApplication.prefs.getString("emergencyNumber") == "" || BBasGlobalApplication.prefs.getString("location") == "0") {
            UserInfoDialog().show(supportFragmentManager, "user info dialog")
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
            adapter = BusSystemAdapter() { route -> moveToReservation(route) }
            setHasFixedSize(true)
        }
    }

    private fun initLocationClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(applicationContext)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            Log.d(TAG, "location client setting success")
        }

        task.addOnFailureListener {
            Log.d(TAG, "location client setting failure")
        }
    }

    fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val currentTask = fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        )

        currentTask.addOnSuccessListener { location ->
            Log.d("TAG", "$location")
            viewModel.requestStationByLocation(location.latitude, location.longitude)
        }

        currentTask.addOnCanceledListener {
            Log.d("TAG", "get location failure")
        }
    }

    private fun moveToReservation(route: BusSystem) {
        Intent(applicationContext, ReservationActivity::class.java).run {
            putExtra("route", route)
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
                //TODO 마이페이지 이동
                true
            }
            R.id.btn_emergency -> {
                //TODO 비상 연락
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}