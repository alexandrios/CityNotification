package com.chelinvest.notification.ui.fragments.login

import android.app.Application
import android.util.Log
import com.chelinvest.notification.BaseApplication
import com.chelinvest.notification.R
import com.chelinvest.notification.api.response.MainResponse
import com.chelinvest.notification.data.Repository
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

//    val userInput = MutableLiveData<String>()
//    val passInput = MutableLiveData<String>()

    fun login(user: String, pass: String) {

        if (user.isEmpty()) {
            errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.login_check_input_user))
            return
        }

        if (pass.isEmpty()) {
            errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.login_check_input_pass))
            return
        }

        /*
        if (userInput.value.isNullOrEmpty()) {
            errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.login_check_input_user))
            return
        }

        if (passInput.value.isNullOrEmpty()) {
            errorLiveEvent.postValue(getApplication<BaseApplication>().getString(R.string.login_check_input_pass))
            return
        }
        */

        //repository.getSession(userInput.value!!, passInput.value!!).enqueue(object : Callback<MainResponse> {
        repository.getSession(user, pass).enqueue(object : Callback<MainResponse> {
            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                Log.d(LOG_TAG, "LoginViewModel onFailure: ${t.message}")
                handleRequestFailure(t)
                errorLiveEvent.postValue(t.message)
            }

            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d(LOG_TAG, "LoginViewModel onResponse: sessionId=${result?.sessionId}")
                    Log.d(LOG_TAG, "LoginViewModel onResponse: errorNote=${result?.errorNote}")

                    if (result != null) {
                        if (!result.errorNote.isNullOrEmpty()) {
                            errorLiveEvent.postValue(result.errorNote)
                        }

                        val session = Session()
                        session.setResponse(result)
                        getSessionId(session)
                    }
                }
            }
        })
    }

    fun getSessionId(session: Session) {
        // Сохранить session_id
        repository.setSessionId(session.session_id ?: "")
        val sessionId = repository.getSessionId()
        Log.d(LOG_TAG, "sessionId=$sessionId")

        if (session.session_id != null) {

            // Увеличить счетчик успешных входов в приложение
            var launchCount = repository.getLaunchCount()
            if (launchCount < 0)
                launchCount = 0
            repository.setLaunchCount(launchCount + 1)

            sessionLiveEvent.postValue(session)
        }
    }
}