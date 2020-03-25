package com.chelinvest.notification.ui.subscr

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubscrViewModel : ViewModel() {
    val saved = MutableLiveData<Boolean>()

    fun save(result: Boolean) {
        saved.value = result
    }
}