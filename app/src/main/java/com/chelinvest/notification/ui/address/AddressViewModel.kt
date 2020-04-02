package com.chelinvest.notification.ui.address

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//https://developer.android.com/topic/libraries/architecture/viewmodel.html#sharing

class AddressViewModel : ViewModel() {

    // Признак того, что адрес был изменен (или создан новый) в EditAddressFragment
    val editSaved = MutableLiveData<Boolean>()
    fun setEditSave(result: Boolean) {
        editSaved.value = result
    }

    val recyclerViewExpandableItemManagerState = MutableLiveData<Parcelable?>()
    fun setStateSave(result: Parcelable?) {
        recyclerViewExpandableItemManagerState.value = result
    }

}