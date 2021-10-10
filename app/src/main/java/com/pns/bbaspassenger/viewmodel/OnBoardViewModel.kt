package com.pns.bbaspassenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pns.bbaspassenger.data.model.Queue
import com.pns.bbaspassenger.data.model.RouteItemModel
import com.pns.bbaspassenger.data.model.RouteStation
import com.pns.bbaspassenger.repository.OnBoardRepository
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLDecoder

class OnBoardViewModel : ViewModel() {
    private val _userQueue = MutableLiveData<Queue>()
    private val _route = MutableLiveData<List<RouteStation>>()
    private val _routeItems = MutableLiveData<List<RouteItemModel>>()

    val routeItemList: LiveData<List<RouteItemModel>> = _routeItems

    init {
        getQueue()
    }

    private fun getQueue() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                OnBoardRepository.getQueue(BBasGlobalApplication.prefs.getString("userId")).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val queue = it.result
                            Log.d(TAG, "$queue")
                            _userQueue.postValue(queue)
                            getRoute(queue.routeId)
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

    private fun getRoute(routeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                OnBoardRepository.getRoute(SERVICE_KEY, BBasGlobalApplication.prefs.getString("location"), routeId).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val innerList = it.response.body.items.result.toList()
                            Log.d(TAG, "$innerList")
                            _route.postValue(innerList)
                            val makeList = ArrayList<RouteItemModel>()
                            for (station in innerList) {
                                if (station.nodeOrder > _userQueue.value?.endStationOrder ?: -1) {
                                    break
                                }
                                makeList.add(RouteItemModel(
                                    station.nodeId,
                                    station.nodeOrder,
                                    station.nodeName,
                                    station.nodeNo,
                                    innerList.size,
                                    station.nodeOrder == _userQueue.value?.startStationOrder ?: -1,
                                    station.nodeOrder == _userQueue.value?.endStationOrder ?: -1,
                                    _userQueue.value?.startStationOrder ?: -1 < station.nodeOrder && station.nodeOrder < _userQueue.value?.endStationOrder ?: -1
                                ).apply {
                                    if (this.nodeOrder == 8) {
                                        this.isBusHere = true
                                    }
                                })
                            }
                            Log.d(TAG, "$makeList")
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

    companion object {
        private const val TAG = "OnBoardViewModel"
        private val SERVICE_KEY = URLDecoder.decode(BBasGlobalApplication.prefs.getString("busApiKey"), "UTF-8")
    }
}