package com.chelinvest.notification.ui.login

import android.app.Application
import android.util.Log
import com.chelinvest.notification.BaseApplication
import com.chelinvest.notification.R
import com.chelinvest.notification.data.Repository
import com.chelinvest.notification.data.remote.A
import com.chelinvest.notification.model.session.Session
import com.chelinvest.notification.ui.BaseViewModel
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.chelinvest.notification.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class LoginViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {

    val errorLiveEvent = SingleLiveEvent<String>()
    val sessionLiveEvent = SingleLiveEvent<Session>()

    fun login(user: String, pass: String) {

        if (user.isEmpty()) {
            errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.login_check_input_user))
            return
        }

        if (pass.isEmpty()) {
            errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.login_check_input_pass))
            return
        }

        repository.getSession(user, pass).enqueue(object : Callback<A> {
            override fun onFailure(call: Call<A>, t: Throwable) {
                //navigator?.finishProgress()
                handleRequestFailure(t)
            }

            override fun onResponse(call: Call<A>, response: Response<A>) {
                if (response.isSuccessful) {
                    //Log.d(LOG_TAG, "LoginViewModel onResponse: ${response.body()}")
                    val result = response.body()
                    Log.d(LOG_TAG, "LoginViewModel onResponse: ${result?.xml}")
                    sessionLiveEvent.postValue(Session())
                }
                //navigator?.finishProgress()
            }
        })

    }
}