package com.pns.bbaspassenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pns.bbaspassenger.data.model.BusArrivalInfo
import com.pns.bbaspassenger.data.model.GetUserRequestBody
import com.pns.bbaspassenger.data.model.PostRatingRequestBody
import com.pns.bbaspassenger.data.model.Queue
import com.pns.bbaspassenger.data.model.RouteItemModel
import com.pns.bbaspassenger.data.model.RouteStation
import com.pns.bbaspassenger.data.model.UpdateQueueRequestBody
import com.pns.bbaspassenger.repository.OnBoardRepository
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import com.pns.bbaspassenger.utils.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URLDecoder

class OnBoardViewModel : ViewModel() {
    private val _userQueue = MutableLiveData<Queue>()
    private val _route = MutableLiveData<List<RouteStation>>()
    private val _routeItems = MutableLiveData<MutableList<RouteItemModel>>()
    private val _busPosition = MutableLiveData<Int>()
    private val _routeItemList = mutableListOf<RouteItemModel>()
    private val _loaded = MutableLiveData<SingleEvent<Boolean>>()
    private val _arriveInfo = MutableLiveData<BusArrivalInfo>()
    private val _passengerBoard = MutableLiveData<SingleEvent<Boolean>>()
    private val _queueDelete = MutableLiveData<SingleEvent<Boolean>>()
    private val _ratingDone = MutableLiveData<SingleEvent<Boolean>>()

    val userQueue: LiveData<Queue> = _userQueue
    val route: LiveData<List<RouteStation>> = _route
    val routeItems: LiveData<MutableList<RouteItemModel>> = _routeItems
    val busPosition: LiveData<Int> = _busPosition
    val loaded: LiveData<SingleEvent<Boolean>> = _loaded
    val passengerBoard: LiveData<SingleEvent<Boolean>> = _passengerBoard
    val queueDelete: LiveData<SingleEvent<Boolean>> = _queueDelete
    val ratingDone: LiveData<SingleEvent<Boolean>> = _ratingDone

    init {
        // 예약 정보 필수
        getQueue()
    }

    private fun getQueue() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                OnBoardRepository.getQueue(BBasGlobalApplication.prefs.getString("userId")).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.success) {
                                val queue = it.result
                                Log.d(TAG, "Queue : $queue")
                                _userQueue.postValue(queue)
                            } else {
                                _loaded.postValue(SingleEvent(false))
                            }
                        }
                    } else {
                        Log.d(TAG, "${response.code()}")
                        _loaded.postValue(SingleEvent(false))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
                _loaded.postValue(SingleEvent(false))
            }
        }
    }

    fun initRoute(queue: Queue) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                OnBoardRepository.getRoute(SERVICE_KEY, CITY_CODE, queue.routeId).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val innerList = it.response.body.items.result.toList()
                            Log.d(TAG, "route : $innerList")
                            _route.postValue(innerList)
                            withContext(Dispatchers.IO) {
                                for (station in innerList) {
                                    if (station.nodeOrder > queue.endStationOrder) {
                                        break
                                    }
                                    _routeItemList.add(RouteItemModel(
                                        station.nodeId,
                                        station.nodeOrder,
                                        station.nodeName,
                                        station.nodeNo,
                                        isStart = station.nodeOrder == queue.startStationOrder,
                                        isEnd = station.nodeOrder == queue.endStationOrder,
                                        isDuring = queue.startStationOrder < station.nodeOrder && station.nodeOrder < queue.endStationOrder,
                                        isFirst = station.nodeOrder == 1,
                                        isBusHere = false,
                                        remainSec = 0,
                                        remainCnt = 0
                                    ))
                                }
                                _loaded.postValue(SingleEvent(true))
                            }
                        }
                    } else {
                        Log.d(TAG, "${response.code()}")
                        _loaded.postValue(SingleEvent(false))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
                _loaded.postValue(SingleEvent(false))
            }
        }
    }

    private fun getArriveInfo(queue: Queue) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                OnBoardRepository.getBusArrival(SERVICE_KEY, CITY_CODE, queue.startStationId, queue.routeId)
                    .let { response ->
                        if (response.isSuccessful) {
                            response.body()?.let { result ->
                                val res = result.response.body.items.result
                                Log.d(TAG, "arriveInfo : $res")
                                _arriveInfo.postValue(res)
                            }
                        } else {
                            Log.d(TAG, "${response.code()}")
                        }
                    }
                delay(5000)
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun updateArriveInfo(queue: Queue) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                while (_busPosition.value ?: 1 < queue.startStationOrder) {
                    getArriveInfo(queue)
                    delay(5000)
                }
            } catch (e: IOException) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun updateBusPosition() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                while (_busPosition.value == null || _busPosition.value ?: 1 < _userQueue.value?.endStationOrder ?: 1) {
                    getBusPosition()
                    delay(5000)
                }
            } catch (e: IOException) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun getBusPosition() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                OnBoardRepository.getBusPosition(SERVICE_KEY, CITY_CODE, _userQueue.value?.routeId ?: "").let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            // 도착 정보
                            val busOrdList = it.response.body.items.result
                            Log.d(TAG, "busOrdList : $busOrdList")

                            // 차량 번호로 원하는 버스 찾기
                            val findBus = busOrdList.find { bus -> bus.vehicleId == _userQueue.value?.vehicleId }
                            Log.d(TAG, "findBus : $findBus")

                            val busOrd by lazy {
                                findBus?.nodeOrder ?: -1
                            }

                            if (_busPosition.value == null) {
                                if (busOrd == -1) {
                                    _busPosition.postValue(1)
                                } else {
                                    _busPosition.postValue(busOrd)
                                }
                            } else {
                                val cur = _busPosition.value!!
                                if (busOrd == -1) {
                                    if (cur != 1) {
                                        _busPosition.postValue(cur)
                                    } else {
                                        _busPosition.postValue(1)
                                    }
                                } else {
                                    if (busOrd >= cur) {
                                        _busPosition.postValue(busOrd)
                                    } else {
                                        _busPosition.postValue(cur)
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun moveBusPos(cur: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            while (_routeItemList[0].nodeOrder < cur) {
                _routeItemList.removeAt(0)
            }
            _routeItemList[0].isBusHere = _routeItemList[0].nodeOrder == cur
            if (cur < _userQueue.value?.startStationOrder ?: 1) {
                _routeItemList.find {
                    it.isStart
                }?.let { rim ->
                    _arriveInfo.value?.let { bai ->
                        rim.remainCnt = rim.nodeOrder - cur
                        if (bai.prevCnt.toInt() >= rim.remainCnt) {
                            rim.remainSec = bai.remainTime.toInt()
                        }
                    }
                }
            }
            _routeItems.postValue(_routeItemList)
        }
    }

    fun sendMessage(viewSend : (String, String, String, String) -> Unit) {
        _userQueue.value?.let { queue ->
            if (queue.boardState > 0) {
                viewSend(BBasGlobalApplication.prefs.getString("userName"), queue.routeNo, queue.vehicleId, _routeItemList[0].nodeName)
            } else {
                // TODO : 위치 정보로 보내기
            }
        }
    }

    fun onClick(viewEvent : (String, String, String) -> Unit) {
        _userQueue.value?.let { queue ->
            viewEvent(queue.routeId, queue.routeNo, queue.vehicleId)
        }
    }

    fun updateState(state: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updateQueueRequestBody = UpdateQueueRequestBody(BBasGlobalApplication.prefs.getString("userId"), state)
                OnBoardRepository.updateQueue(updateQueueRequestBody).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let { result ->
                            Log.d(TAG, "$response")
                            if (result.success) {
                                val queue = result.result
                                Log.d(TAG, "Queue : $queue")
                                _userQueue.postValue(queue)
                                _passengerBoard.postValue(SingleEvent(true))
                            } else {
                                Log.d(TAG, "$response")
                                _passengerBoard.postValue(SingleEvent(false))
                            }
                        }
                    } else {
                        Log.d(TAG, "${response.code()}")
                        _passengerBoard.postValue(SingleEvent(false))
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
                _passengerBoard.postValue(SingleEvent(false))
            }
        }
    }

    fun deleteQueue() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val updateQueueRequestBody = GetUserRequestBody(BBasGlobalApplication.prefs.getString("userId"))
                OnBoardRepository.deleteQueue(updateQueueRequestBody).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let { result ->
                            Log.d(TAG, "$response")
                            if (result.success) {
                                _queueDelete.postValue(SingleEvent(true))
                            } else {
                                Log.d(TAG, "$response")
                                _queueDelete.postValue(SingleEvent(false))
                            }
                        }
                    } else {
                        Log.d(TAG, "${response.code()}")
                        _queueDelete.postValue(SingleEvent(false))
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
                _queueDelete.postValue(SingleEvent(false))
            }
        }
    }

    fun doRating(vehicleId: String, ratingData: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "$vehicleId, $ratingData")
                val postRatingRequestBody = PostRatingRequestBody(vehicleId, ratingData)
                OnBoardRepository.createRating(postRatingRequestBody).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let { result ->
                            Log.d(TAG, "$response")
                            if (result.success) {
                                _ratingDone.postValue(SingleEvent(true))
                            } else {
                                _ratingDone.postValue(SingleEvent(false))
                            }
                        }
                    } else {
                        Log.d(TAG, "${response.code()}")
                        _ratingDone.postValue(SingleEvent(false))
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
                _ratingDone.postValue(SingleEvent(false))
            }
        }
    }

    companion object {
        private const val TAG = "OnBoardViewModel"
        private val SERVICE_KEY = URLDecoder.decode(BBasGlobalApplication.prefs.getString("busApiKey"), "UTF-8")
        private val CITY_CODE = BBasGlobalApplication.prefs.getString("location")
    }
}