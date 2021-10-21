package com.pns.bbaspassenger.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.pns.bbaspassenger.R
import com.pns.bbaspassenger.utils.BBasGlobalApplication

abstract class BaseActivity : AppCompatActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLocationClient()
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
            Log.d("BaseActivity", "location client setting success")
        }

        task.addOnFailureListener {
            Log.d("BaseActivity", "location client setting failure")
        }
    }

    fun requestLocation(todo: (Double, Double) -> Unit) {
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
            Log.d("BaseActivity", "$location")
            todo(location.latitude, location.longitude)
        }

        currentTask.addOnCanceledListener {
            Log.d("BaseActivity", "get location failure")
        }
    }

    fun sendEmergencyMessage() {
        requestLocation { latitude, longitude ->
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra("address", BBasGlobalApplication.prefs.getString("emergencyNumber"))
                putExtra(
                    "sms_body",
                    getString(
                        R.string.emergency_message_format_no_ride,
                        BBasGlobalApplication.prefs.getString("userName"),
                        latitude,
                        longitude
                    )
                )
                type = "text/plain"
            }

            startActivity(sendIntent)
        }
    }
}