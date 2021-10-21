package com.pns.bbaspassenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pns.bbaspassenger.data.model.GetUserRequestBody
import com.pns.bbaspassenger.repository.OnBoardRepository
import com.pns.bbaspassenger.repository.UserRepository
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import com.pns.bbaspassenger.utils.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class LaunchViewModel : ViewModel() {
    private val _autoLoginSuccess = MutableLiveData<Boolean>()
    private val _isQueueExist = MutableLiveData<SingleEvent<String>>()
    private val _queueDelete = MutableLiveData<SingleEvent<Boolean>>()

    val autoLoginSuccess: LiveData<Boolean> = _autoLoginSuccess
    val isQueueExist: LiveData<SingleEvent<String>> = _isQueueExist
    val queueDelete: LiveData<SingleEvent<Boolean>> = _queueDelete

    fun autoLogin(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val requestBody = GetUserRequestBody(userId)
            try {
                UserRepository.getUser(requestBody).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.success) {
                                val userResult = it.result
                                Log.d(TAG, "result : $userResult")
                                BBasGlobalApplication.prefs.updateUserPrefs(userResult)
                                Log.d(TAG, "prefs user : ${BBasGlobalApplication.prefs.getUserPrefs()}")
                                _autoLoginSuccess.postValue(true)
                            } else {
                                _autoLoginSuccess.postValue(false)
                            }
                        }
                    } else {
                        Log.d(TAG, "result : ${response.code()}")
                        _autoLoginSuccess.postValue(false)
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, e.message.toString())
                e.printStackTrace()
                _autoLoginSuccess.postValue(false)
            }
        }
    }

    fun getQueue() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                OnBoardRepository.getQueue(BBasGlobalApplication.prefs.getString("userId")).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.success) {
                                _isQueueExist.postValue(SingleEvent("exist"))
                            } else {
                                _isQueueExist.postValue(SingleEvent("noInfo"))
                            }
                        }
                    } else {
                        Log.d(TAG, "${response.code()}")
                        _isQueueExist.postValue(SingleEvent("readFail"))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
                e.printStackTrace()
                _isQueueExist.postValue(SingleEvent("readFail"))
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

    companion object {
        private const val TAG = "Launch"
    }
}