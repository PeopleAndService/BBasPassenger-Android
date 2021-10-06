package com.pns.bbaspassenger.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pns.bbaspassenger.data.model.SignUserRequestBody
import com.pns.bbaspassenger.repository.UserRepository
import com.pns.bbaspassenger.utils.BBasGlobalApplication
import com.pns.bbaspassenger.utils.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel : ViewModel() {
    private val _googleSignInEvent = MutableLiveData<SingleEvent<Unit>>()
    private val _loginSuccess = MutableLiveData<Boolean>()

    val googleSignInEvent: LiveData<SingleEvent<Unit>> = _googleSignInEvent
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun onClickLogin(action: Unit) {
        _googleSignInEvent.value = SingleEvent(action)
    }

    fun sign(userId: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val signUpRequestBody = SignUserRequestBody(userId, name)
            try {
                UserRepository.sign(signUpRequestBody).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val userResult = it.result
                            Log.d(TAG, "result : $userResult")

                            BBasGlobalApplication.prefs.setUserPrefs(userResult)
                            Log.d(TAG, "prefs user : ${BBasGlobalApplication.prefs.getUserPrefs()}")
                            _loginSuccess.postValue(true)
                        }
                    } else {
                        Log.d(TAG, "result : ${response.code()}")
                        _loginSuccess.postValue(false)
                    }
                }
            } catch (e : IOException) {
                Log.e(TAG, e.message.toString())
                e.printStackTrace()
                _loginSuccess.postValue(false)
            }
        }
    }

    companion object {
        private const val TAG = "Login"
    }
}