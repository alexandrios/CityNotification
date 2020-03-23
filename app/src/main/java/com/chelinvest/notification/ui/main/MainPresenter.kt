package com.chelinvest.notification.ui.main

import android.content.Context
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.ui.presenter.Presenter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class MainPresenter : Presenter() {

    fun getFCMToken(context: Context) {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Preferences.getInstance().saveFCMToken(context, null)
                return@OnCompleteListener
            }

            val token = task.result?.token
            Preferences.getInstance().saveFCMToken(context, token)
        })
    }

}