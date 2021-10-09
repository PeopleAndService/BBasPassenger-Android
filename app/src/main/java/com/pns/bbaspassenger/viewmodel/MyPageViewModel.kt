package com.pns.bbaspassenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pns.bbaspassenger.data.model.GetUserRequestBody
import com.pns.bbaspassenger.repository.UserRepository
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import com.pns.bbaspassenger.utils.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class MyPageViewModel : ViewModel() {
    private val _success = MutableLiveData<SingleEvent<Boolean>>()

    val success: LiveData<SingleEvent<Boolean>>
        get() = _success

    fun withDraw() {
        viewModelScope.launch(Dispatchers.IO) {
            // val requestBody = GetUserRequestBody("asdkfjppp22")
            val requestBody = GetUserRequestBody(BBasGlobalApplication.prefs.getString("userId"))
            try {
                UserRepository.deleteUser(requestBody).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            Log.d(TAG, "$it")
                            if (it.success) {
                                _success.postValue(SingleEvent(true))
                            } else {
                                _success.postValue(SingleEvent(false))
                            }
                        }
                    } else {
                        Log.d(TAG, "result : ${response.code()}")
                        _success.postValue(SingleEvent(false))
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, e.message.toString())
                e.printStackTrace()
                _success.postValue(SingleEvent(false))
            }
        }
    }

    companion object {
        private const val TAG = "MyPageViewModel"
    }
}