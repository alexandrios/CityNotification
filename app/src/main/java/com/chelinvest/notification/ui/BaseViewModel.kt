package com.chelinvest.notification.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.chelinvest.notification.BaseApplication
import com.chelinvest.notification.R
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseViewModel constructor(
    application: Application
) : AndroidViewModel(application) {
    var requestListener: RequestListener? = null

    fun handleRequestFailure(exception: Throwable) {
        when (exception) {
           is JsonSyntaxException ->
               requestListener?.onRequestFailure(getApplication<BaseApplication>().resources.getString(R.string.error_no_word))
           is HttpException -> {
                when (exception.code()) {
                    404 -> requestListener?.onRequestFailure(getApplication<BaseApplication>().resources.getString(R.string.error_404), true)
                    else -> requestListener?.onRequestFailure("Exception " + exception.code(), true)
                }
            }
            is UnknownHostException -> {
                requestListener?.onRequestFailure(getApplication<BaseApplication>().resources.getString(R.string.error_404), true)
            }
            is SocketTimeoutException -> {
                requestListener?.onRequestFailure(getApplication<BaseApplication>().resources.getString(R.string.error_host), true)
            }
            else ->
                requestListener?.onRequestFailure(exception.message!!)
        }
    }
}