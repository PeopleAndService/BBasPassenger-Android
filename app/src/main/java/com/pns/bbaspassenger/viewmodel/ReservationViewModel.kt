package com.pns.bbaspassenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pns.bbaspassenger.data.model.RouteItemModel
import com.pns.bbaspassenger.data.model.RouteStation
import com.pns.bbaspassenger.repository.ReservationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLDecoder

class ReservationViewModel : ViewModel() {
    private val _route = MutableLiveData<List<RouteStation>>()
    private val _routeItems = MutableLiveData<List<RouteItemModel>>()
    private val _reservation = MutableLiveData<ArrayList<RouteItemModel>>()

    val routeItemList: LiveData<List<RouteItemModel>> = _routeItems
    val reservation: LiveData<ArrayList<RouteItemModel>> = _reservation

    fun getBusRoute(routeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ReservationRepository.getRoute(SERVICE_KEY, "38030", routeId).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val innerList = it.response.body.items.result.toList()
                            val makeList = ArrayList<RouteItemModel>()

                            _route.postValue(innerList)

                            for (station in innerList) {
                                makeList.add(RouteItemModel(
                                    station.nodeId,
                                    station.nodeOrder,
                                    station.nodeName,
                                    station.nodeNo,
                                    false,
                                    station.nodeOrder == innerList.size,
                                    isDuring = false,
                                    isFirst = false,
                                    isBusHere = false,
                                    remainSec = 0,
                                    remainCnt = 0
                                ))
                            }

                            _routeItems.postValue(makeList.toList())
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

    fun makeReservation(reservation: ArrayList<RouteItemModel>) {
        _reservation.value = reservation

        _routeItems.postValue(_routeItems.value?.map { routeItem ->
            when {
                routeItem == reservation.first() -> {
                    RouteItemModel(
                        routeItem.nodeId,
                        routeItem.nodeOrder,
                        routeItem.nodeName,
                        routeItem.nodeNo,
                        isStart = true,
                        isEnd = false,
                        isDuring = false,
                        isFirst = false,
                        isBusHere = true,
                        remainSec = 0,
                        remainCnt = 0
                    )
                }
                routeItem == reservation.last() -> {
                    RouteItemModel(
                        routeItem.nodeId,
                        routeItem.nodeOrder,
                        routeItem.nodeName,
                        routeItem.nodeNo,
                        isStart = false,
                        isEnd = false,
                        isDuring = false,
                        isFirst = false,
                        isBusHere = false,
                        remainSec = 0,
                        remainCnt = 0
                    )
                }
                routeItem.nodeOrder in reservation.first().nodeOrder..reservation.last().nodeOrder -> {
                    RouteItemModel(
                        routeItem.nodeId,
                        routeItem.nodeOrder,
                        routeItem.nodeName,
                        routeItem.nodeNo,
                        isStart = false,
                        isEnd = false,
                        isDuring = true,
                        isFirst = false,
                        isBusHere = false,
                        remainSec = 0,
                        remainCnt = 0
                    )
                }
                else -> {
                    routeItem
                }
            }
        })
    }

    companion object {
        private const val TAG = "ReservationViewModel"
        private val SERVICE_KEY = URLDecoder.decode(
            "",
            "UTF-8"
        )
    }
}