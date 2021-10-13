package com.pns.bbaspassenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pns.bbaspassenger.data.model.BusSystem
import com.pns.bbaspassenger.data.model.GetStationRequestBody
import com.pns.bbaspassenger.data.model.SearchRequestBody
import com.pns.bbaspassenger.repository.BusSystemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel : ViewModel() {
    private val _busSystemData = MutableLiveData<MutableList<BusSystem>>()

    val busSystemData: LiveData<MutableList<BusSystem>> = _busSystemData

    fun search(latitude: Double, longitude: Double, query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                BusSystemRepository.search(SearchRequestBody(latitude, longitude, query)).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val result = it.result.toMutableList()

                            _busSystemData.postValue(result)
                        }
                    } else {
                        Log.d(TAG, "${response.code()}")
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "${e.message} here")
                _busSystemData.postValue(mutableListOf())
                e.printStackTrace()
            }
        }
    }

    fun requestStationByLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                BusSystemRepository.getStation(GetStationRequestBody(latitude, longitude)).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val result = it.result.sortedBy { station ->
                                station.description.dropLast(1).toInt()
                            }.toMutableList()

                            _busSystemData.postValue(result)
                        }
                    } else {
                        Log.d(TAG, "${response.code()}")
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}