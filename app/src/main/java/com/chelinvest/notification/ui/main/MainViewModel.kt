package com.chelinvest.notification.ui.main

import android.app.Application
import android.content.Context
import android.util.Log
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.data.Repository
import com.chelinvest.notification.ui.BaseViewModel
import com.chelinvest.notification.utils.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import javax.inject.Inject

class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {

    fun getFCMToken(context: Context) {
        //FirebaseInstallations.getInstance().id.addOnCompleteListener(OnCompleteListener { task ->
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                repository.setFCMToken(null)
                return@OnCompleteListener
            }

            val token = task.result?.token
            //Preferences.getInstance().saveFCMToken(context, token)
            Log.d(Constants.LOG_TAG, "MainViewModel token=$token")
            repository.setFCMToken(token)
        })
    }
}