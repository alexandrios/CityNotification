package com.chelinvest.notification.additional

import android.accounts.NetworkErrorException
import com.chelinvest.notification.exception.*
import kotlinx.coroutines.*
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.xml.stream.XMLStreamException

fun resolvedLaunchWithJob(block: suspend CoroutineScope.() -> Unit,
                          onError: (ex: Exception) -> Unit) = GlobalScope.launch(Dispatchers.Main) {
    try {
        block()
    }
    catch (ex: Exception) {

        ex.printStackTrace()

        when (ex) {
            is CancellationException -> {
                onError(NotShowedException())
            }
            is SocketException -> {
                onError(NotShowedException())
            }
            is NotShowedException -> {}
            is HttpException -> {
                if (ex.errorCode == 401) {
                    onError(SessionException())
                } else {
                    if (ex.errorCode >= 500)
                        onError(InternalServerException())
                    else
                        onError(ex)
                }
            }
            is CustomException -> {
                onError(ex)
            }
            is UnknownHostException, is NetworkErrorException, is SocketTimeoutException, is ConnectException -> {
                onError(NetworkException())
            }
            is IOException -> {
                if (ex.message == "Canceled")
                    onError(NotShowedException())
                else
                    onError(UnknownException())
            }
            is XMLStreamException -> {
                onError(ex)
            }
            is ResponseException -> {
                onError(ex)
            }
            else -> onError(UnknownException())
        }
    }
}

fun resolvedLaunch(block: suspend CoroutineScope.() -> Unit,
                   onError: (ex: Exception) -> Unit) {
    resolvedLaunchWithJob(block, onError)
}

fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> = GlobalScope.async(block = block)