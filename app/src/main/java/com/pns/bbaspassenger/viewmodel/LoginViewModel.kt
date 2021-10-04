package com.pns.bbaspassenger.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pns.bbaspassenger.utils.SingleEvent

class LoginViewModel : ViewModel() {
    private val _googleSignInEvent = MutableLiveData<SingleEvent<Unit>>()

    val googleSignInEvent: LiveData<SingleEvent<Unit>> = _googleSignInEvent

    fun onClickLogin(action: Unit) {
        _googleSignInEvent.value = SingleEvent(action)
    }
}