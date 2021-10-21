package com.pns.bbaspassenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pns.bbaspassenger.data.model.BusLocation
import com.pns.bbaspassenger.data.model.CreateQueueRequestBody
import com.pns.bbaspassenger.data.model.Queue
import com.pns.bbaspassenger.data.model.ReservationRouteItem
import com.pns.bbaspassenger.repository.ReservationRepository
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import com.pns.bbaspassenger.utils.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLDecoder

class ReservationViewModel : ViewModel() {
    private val _routeNo = MutableLiveData<String>()
    private val _routeId = MutableLiveData<String>()
    private val _queue = MutableLiveData<Queue>()

    private val _innerList = mutableListOf<ReservationRouteItem>()
    private val _routeItems = MutableLiveData<MutableList<ReservationRouteItem>>()

    private val _busLocations = MutableLiveData<List<BusLocation>>()
    private val _selectedBus = MutableLiveData<BusLocation?>()

    private val _startPos = MutableLiveData<Int?>()
    private val _startSelect = MutableLiveData<SingleEvent<String>>()

    private val _endPos = MutableLiveData<Int?>()
    private val _endSelect = MutableLiveData<SingleEvent<String>>()

    val routeNo: LiveData<String> = _routeNo

    val routeItemList: LiveData<MutableList<ReservationRouteItem>> = _routeItems
    val busLocations: LiveData<List<BusLocation>> = _busLocations
    val selectedBus: LiveData<BusLocation?> = _selectedBus

    val startPos: LiveData<Int?> = _startPos
    val startSelect: LiveData<SingleEvent<String>> = _startSelect

    val endPos: LiveData<Int?> = _endPos
    val endSelect: LiveData<SingleEvent<String>> = _endSelect

    fun getBusRoute(routeId: String, routeNo: String, startStationId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ReservationRepository.getRoute(SERVICE_KEY, CITY_CODE, routeId).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val result = it.response.body.items.result.toList()

                            for (station in result) {
                                _innerList.add(
                                    ReservationRouteItem(
                                        station.nodeId,
                                        station.nodeOrder,
                                        station.nodeName,
                                        station.nodeNo,
                                        station.nodeOrder == 1,
                                        station.nodeOrder == result.size,
                                        isStart = false,
                                        isDuring = false,
                                        isEnd = false,
                                    )
                                )
                            }

                            _routeItems.postValue(_innerList)
                            _routeNo.postValue(routeNo)
                            _routeId.postValue(routeId)

                            val startStation = _innerList.find { item -> item.nodeId == startStationId }
                            startStation?.let { station -> _startPos.postValue(station.nodeOrder - 1) }
                        }
                    } else {
                        Log.d(TAG, "${response.code()}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun getBusLocation(routeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ReservationRepository.getBusLocation(SERVICE_KEY, CITY_CODE, routeId).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val result = it.response.body.items.result.filter { location ->
                                location.nodeOrder < _startPos.value!! + 1
                            }.sortedBy { location -> _startPos.value!! + 1 - location.nodeOrder }.take(3)

                            _busLocations.postValue(result)
                        }
                    } else {
                        Log.d(TAG, "${response.code()}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun handleSelect(position: Int) {
        if (_startPos.value == null) {
            if (position != _innerList.size - 1) {
                _startPos.postValue(position)
                _startSelect.postValue(SingleEvent("success"))
            } else {
                _startSelect.postValue(SingleEvent("startIsLast"))
            }
        } else {
            if (position == _startPos.value) {
                if (endPos.value == null) {
                    _startPos.postValue(null)
                    _startSelect.postValue(SingleEvent("clearStart"))
                } else {
                    _startSelect.postValue(SingleEvent("clearEndFirst"))
                }
            } else {
                if (_endPos.value == null) {
                    if (position < _startPos.value!!) {
                        _endSelect.postValue(SingleEvent("endBeforeStart"))
                    } else {
                        _endPos.postValue(position)
                        _endSelect.postValue(SingleEvent("success"))
                    }
                } else {
                    if (position == _endPos.value) {
                        _endPos.postValue(null)
                        _endSelect.postValue(SingleEvent("clearEnd"))
                    } else {
                        _endSelect.postValue(SingleEvent("fullSelected"))
                    }
                }
            }
        }
    }

    fun selectBus(selected: BusLocation) {
        _selectedBus.value = selected
        Log.d(TAG, "${_selectedBus.value}")
    }

    fun requestReservation(onJobComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val startStation = _routeItems.value!![_startPos.value!!]
                val endStation = _routeItems.value!![_endPos.value!!]

                val createQueueRequestBody = CreateQueueRequestBody(
                    startStation.nodeId,
                    endStation.nodeId,
                    _routeId.value!!,
                    _selectedBus.value!!.vehicleId,
                    BBasGlobalApplication.prefs.getString("userId"),
                    startStation.nodeOrder,
                    endStation.nodeOrder
                )

                ReservationRepository.createReservation(createQueueRequestBody).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val result = it.result

                            _queue.postValue(result)
                            onJobComplete()
                        }
                    } else {
                        Log.d(TAG, "${response.code()}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun cancelReservation() {
        _selectedBus.value = null
        Log.d(TAG, "${_selectedBus.value}")
    }

    fun clearAll() {
        _endPos.postValue(null)
        _startPos.postValue(null)
    }

    companion object {
        private const val TAG = "ReservationViewModel"
        private val SERVICE_KEY = URLDecoder.decode(
            BBasGlobalApplication.prefs.getString("busApiKey"),
            "UTF-8"
        )
        private val CITY_CODE = BBasGlobalApplication.prefs.getString("location")
    }
}