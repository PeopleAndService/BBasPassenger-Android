package com.pns.bbaspassenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pns.bbaspassenger.data.model.GetUserRequestBody
import com.pns.bbaspassenger.repository.UserRepository
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class LaunchViewModel : ViewModel() {
    private val _autoLoginSuccess = MutableLiveData<Boolean>()

    val autoLoginSuccess: LiveData<Boolean> = _autoLoginSuccess

    fun autoLogin(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val requestBody = GetUserRequestBody(userId)
            try {
                UserRepository.getUser(requestBody).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val user = it.result
                            Log.d(TAG, "result : $user")

                            BBasGlobalApplication.prefs.updateUserPrefs(user)
                            Log.d(TAG, "prefs user : ${BBasGlobalApplication.prefs.getUserPrefs()}")
                            _autoLoginSuccess.postValue(true)
                        }
                    } else {
                        Log.d(TAG, "result : ${response.code()}")
                        _autoLoginSuccess.postValue(false)
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, e.message.toString())
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val TAG = "Launch"
    }
}