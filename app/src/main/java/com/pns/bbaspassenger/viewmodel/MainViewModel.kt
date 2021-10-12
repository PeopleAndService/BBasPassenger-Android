package com.pns.bbaspassenger.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pns.bbaspassenger.data.model.BusSystem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel : ViewModel() {
    private val _busSystemData = MutableLiveData<ArrayList<BusSystem>>()

    val busSystemData: LiveData<ArrayList<BusSystem>> = _busSystemData

    init {
        _busSystemData.value = arrayListOf(
            BusSystem("JJB381012020", "bus", "120 (진양호공원-공영차고지)", "일반"),
            BusSystem(
                "JJB381247007", "busStop", "경상국립대학교가좌캠퍼스후문 (공영차고지 방면)", "50m", arrayListOf(
                    BusSystem("JJB381012020", "expand", "120 (진양호공원-공영차고지)", "일반"),
                    BusSystem("JJB381012110", "expand", "121 (봉전경로회관-진양호차고지)", "일반"),
                    BusSystem("JJB381012310", "expand", "123 (소곡마을-진양호차고지)", "일반")
                )
            )
        )
    }

    fun search() {
    }

    fun requestStationByLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

            } catch (e: IOException) {

            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}