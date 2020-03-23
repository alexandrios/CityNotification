package com.chelinvest.notification.interactor

import android.content.Context
import android.util.Log
import okhttp3.Call
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.stream.Format
import com.chelinvest.notification.BuildConfig
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.*
import com.chelinvest.notification.api.OkRequest
import com.chelinvest.notification.api.request.MainRequest
import com.chelinvest.notification.api.response.MainResponse
import com.chelinvest.notification.exception.PasswordException
import com.chelinvest.notification.exception.ResponseException
import com.chelinvest.notification.exception.SessionException

import java.io.StringReader
import java.io.StringWriter

open class Interactor {

    private fun getServerUrl(context: Context): String {
        val isTestServer = Preferences.getInstance().isTestServer(context)
        //return if (isTestServer) TEST_SERVER_URL else RELEASE_SERVER_URL
        //return if (isTestServer) URL_XGATE_MOBILE_OUTER else URL_XGATE_MOBILE_INNER

        val xGateType = Preferences.getInstance().getXgateType(context)
        val url = if (xGateType == "dvv") URL_XGATE_MOBILE_INNER_DVV else URL_XGATE_MOBILE_INNER_JEV
        if (BuildConfig.DEBUG) {
            Log.wtf("xGate", url)
        }
        return url
    }

    protected fun send(context: Context, request: MainRequest) =
        send(context, request, {}, false, {x-> x} )

    protected fun send(context: Context, request: MainRequest, beforeResponse: (String?) -> String?) =
        send(context, request, {}, false, beforeResponse)

    protected fun send(
        context: Context,
        request: MainRequest,
        onCallCreated: (call: Call) -> Unit,
        isLong: Boolean,
        beforeResponse: (body: String?) -> String?
    ): MainResponse {
        var serializer: Serializer =
            Persister(Format("<?xml version=\"1.0\" encoding=\"windows-1251\"?>"))
        val stringWriter = StringWriter()
        serializer.write(request, stringWriter)
        val str = stringWriter.toString().replace("+", "%2B")
        if (BuildConfig.DEBUG) {
            Log.wtf("request", str)
        }

        val res = OkRequest.getInstance().request(getServerUrl(context), str, onCallCreated, isLong)

        var body = res.body
        if (BuildConfig.DEBUG) {
            Log.wtf("response", body)
        }

        //--
        if (res.errorCode != 200) {
            throw ResponseException("Response code=" + res.errorCode.toString() + "/n" + res.errorMessage )
        } else {
            if (res.body.isNullOrEmpty()) {
                throw ResponseException("Response body is empty. ")
            }
        }
        //--

        body = beforeResponse(body)
        if (BuildConfig.DEBUG) {
            Log.wtf("response", body)
        }

        val stringReader = StringReader(body ?: "")
        serializer = Persister()
        val response = serializer.read(MainResponse::class.java, stringReader)

        // Проверить response на наличие ошибок
        checkResponse(context, response)

        return response
    }

    private fun checkResponse(context: Context, response: MainResponse) {
        if (!response.errorNote.isNullOrEmpty()) {
            val errorNote = response.errorNote!!

            if (errorNote.startsWith("XGATE000") || errorNote.startsWith("XGATE002") ||
                errorNote.startsWith("XGATE004") ) {
                //throw SessionException()
                throw ResponseException(errorNote)
            }
            if (errorNote.contains("request is not allowed")) {
                throw ResponseException(errorNote)
            }
            if (errorNote.contains("Unexpected end of stream")) {
                throw ResponseException(errorNote)
            }
            if (errorNote.startsWith("XGATE005") ||errorNote.startsWith("XGATE010")) {
                //throw PasswordException()
                throw ResponseException(errorNote)
            }
            if (errorNote.startsWith("ORA-20") && errorNote.contains("ERR-")) {
                throw ResponseException(context.string(R.string.ORA1_error))
            }
            if (errorNote.startsWith("ORA-20") && errorNote.contains("ORA-0")) {
                throw ResponseException(context.string(R.string.ORA2_error))
            }
            if (errorNote.startsWith("ORA-20")) {
                val ind = errorNote.indexOf(':')
                val text = errorNote.substring(ind + 1).trim()
                throw ResponseException(text)
            }

            throw ResponseException(errorNote)
        }
    }

}