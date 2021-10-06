package com.pns.bbaspassenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pns.bbaspassenger.data.model.UpdateUserRequestBody
import com.pns.bbaspassenger.repository.UserRepository
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class UserInfoViewModel : ViewModel() {
    private val _success = MutableLiveData<Boolean>()

    val success: LiveData<Boolean> = _success

    fun updateUserInfo(emergencyNumber: String, onlyLowBus: Boolean, location: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val requestBody = UpdateUserRequestBody(BBasGlobalApplication.prefs.getString("userId"), emergencyNumber, onlyLowBus, location)
                UserRepository.updateUser(requestBody).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val user = it.result
                            Log.d(TAG, "result : $user")
                            BBasGlobalApplication.prefs.updateUserPrefs(user)
                            Log.d(TAG, "result : ${BBasGlobalApplication.prefs.getUserPrefs()}")
                            _success.postValue(true)
                        }
                    } else {
                        Log.d(TAG, "result : ${response.code()}")
                        _success.postValue(false)
                    }
                }
            } catch (e : IOException) {
                Log.e(TAG, e.message.toString())
                e.printStackTrace()
                _success.postValue(false)
            }
        }
    }

    companion object {
        private const val TAG = "USER_INFO"
    }
}