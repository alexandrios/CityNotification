package com.chelinvest.notification.data.remote

import android.util.Log
import com.chelinvest.notification.api.OkRequest
import com.chelinvest.notification.api.request.LoginRequest
import com.chelinvest.notification.api.request.MainRequest
import com.chelinvest.notification.model.session.Session
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.Constants.LOG_TAG
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.stream.Format
import retrofit2.Call
import java.io.StringWriter
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val remoteService: RemoteService) {
    fun getSession(user: String, pass: String): Call<A> {

        val serverUrl = Constants.URL_XGATE_MOBILE_INNER_DVV
        val request = MainRequest(LoginRequest().apply {
            this.user = user
            this.password = pass
        })
        val serializer: Serializer =
            Persister(Format("<?xml version=\"1.0\" encoding=\"windows-1251\"?>"))
        val stringWriter = StringWriter()
        serializer.write(request, stringWriter)
        val requestBodyText = stringWriter.toString().replace("+", "%2B")
        Log.d(LOG_TAG, "requestBodyText=$requestBodyText")


        val ENCODING = "windows-1251"
        val mediaType =
            ("application/x-www-form-urlencoded; charset=${ENCODING}").toMediaTypeOrNull()

        val requestBody: RequestBody = requestBodyText.toRequestBody(mediaType)

        val responseBody = remoteService.getSession(requestBody)

        return responseBody
    }
}