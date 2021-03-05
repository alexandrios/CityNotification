package com.chelinvest.notification.ui.fragments.types

import android.app.Application
import com.chelinvest.notification.data.Repository
import com.chelinvest.notification.ui.BaseViewModel
import com.chelinvest.notification.utils.SingleLiveEvent
import javax.inject.Inject

class TypesViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {

    val loginAgainLiveEvent = SingleLiveEvent<Nothing>()

    fun setSelectedItem(value: Int) {
        repository.setSelectedItem(value)
    }

    fun getSelectedItem(): Int {
        return repository.getSelectedItem()
    }

    fun loginOnClick() {
        loginAgainLiveEvent.call()
    }
}