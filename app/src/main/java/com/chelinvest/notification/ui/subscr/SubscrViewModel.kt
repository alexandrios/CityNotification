package com.chelinvest.notification.ui.subscr

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//https://developer.android.com/topic/libraries/architecture/viewmodel.html#sharing

class SubscrViewModel : ViewModel() {

    // Признак того, что был изменен элемент списка агентов (в EditSubscrFragment)
    val editSaved = MutableLiveData<Boolean>()
    fun setEditSave(result: Boolean) {
        editSaved.value = result
    }

    // Храним значение CheckBox "Только активные подписки"
    val activeOnly = MutableLiveData<Boolean>()
    fun setActiveOnly(result: Boolean) {
        activeOnly.value = result
    }
}