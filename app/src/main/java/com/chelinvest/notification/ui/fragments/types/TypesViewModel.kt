package com.chelinvest.notification.ui.fragments.types

import android.app.Application
import com.chelinvest.notification.data.Repository
import com.chelinvest.notification.ui.BaseViewModel
import javax.inject.Inject

class TypesViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {

}