package com.chelinvest.notification.ui.fragments.page

import android.app.Application
import com.chelinvest.notification.data.Repository
import com.chelinvest.notification.ui.BaseViewModel
import javax.inject.Inject

class PageViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {

}