package com.chelinvest.notification.data.remote

import android.util.Log
import com.chelinvest.notification.api.OkRequest
import com.chelinvest.notification.api.request.LoginRequest
import com.chelinvest.notification.api.request.MainRequest
import com.chelinvest.notification.api.response.MainResponse
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.Constants.LOG_TAG
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.stream.Format
import retrofit2.Call
import retrofit2.Response
import java.io.StringWriter
import java.net.URLEncoder
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val remoteService: RemoteService) {
    fun getSession(user: String, pass: String): Call<MainResponse> {

        //val serverUrl = Constants.URL_XGATE_MOBILE_INNER_DVV
        val request = MainRequest(LoginRequest().apply {
            this.user = user
            this.password = pass
        })
        val serializer: Serializer =
            Persister(Format("<?xml version=\"1.0\" encoding=\"windows-1251\"?>"))
        val stringWriter = StringWriter()
        serializer.write(request, stringWriter)
        val query = stringWriter.toString().replace("+", "%2B")
        Log.d(LOG_TAG, "query=$query")

        val ENCODING = "windows-1251"
        val body = String.format(
            Constants.REQUEST_BODY, URLEncoder.encode(query, ENCODING))
        Log.d(LOG_TAG, "body=$body")

        val mediaType =
            ("application/x-www-form-urlencoded; charset=${ENCODING}").toMediaTypeOrNull()
        val requestBody: RequestBody = body.toRequestBody(mediaType)

        val responseBody = remoteService.getSession(requestBody)

        return responseBody
    }
}