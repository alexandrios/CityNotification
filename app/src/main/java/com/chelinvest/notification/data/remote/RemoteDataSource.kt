package com.chelinvest.notification.data.remote

import android.util.Log
import com.chelinvest.notification.api.OkRequest
import com.chelinvest.notification.api.request.*
import com.chelinvest.notification.api.response.MainResponse
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.Constants.ENCODING
import com.chelinvest.notification.utils.Constants.LOG_TAG
import okhttp3.MediaType
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
    private fun mediaType(): MediaType? = ("application/x-www-form-urlencoded; charset=${ENCODING}").toMediaTypeOrNull()

    private fun getRequestBody(request: MainRequest): RequestBody {
        val serializer: Serializer =
            Persister(Format("<?xml version=\"1.0\" encoding=\"windows-1251\"?>"))
        val stringWriter = StringWriter()
        serializer.write(request, stringWriter)
        val query = stringWriter.toString().replace("+", "%2B")
        Log.d(LOG_TAG, "query=$query")
        val body = String.format(Constants.REQUEST_BODY, URLEncoder.encode(query, ENCODING))
        //Log.d(LOG_TAG, "body=$body")
        return body.toRequestBody(mediaType())
    }

    fun getSession(user: String, pass: String): Call<MainResponse> {
        val request = MainRequest(LoginRequest().apply {
            this.user = user
            this.password = pass
        })
        val requestBody = getRequestBody(request)
        return remoteService.getSession(requestBody)
    }

    fun loadDeliveryBranches(sessionId: String): Call<MainResponse> {
        val request = MainRequest(GetDeliveryBranchRequest().apply {
            this.session_id = sessionId
        })
        val requestBody = getRequestBody(request)
        return remoteService.loadDeliveryBranches(requestBody)
    }

    fun loadAgentInfo(sessionId: String): Call<MainResponse> {
        val request = MainRequest(GetAgentInfoRequest().apply {
            this.session_id = sessionId
        })
        val requestBody = getRequestBody(request)
        return remoteService.loadAgentInfo(requestBody)
    }

    fun loadAgentLimit(sessionId: String): Call<MainResponse> {
        val request = MainRequest(CheckAgentLimitRequest().apply {
            this.session_id = sessionId
        })
        val requestBody = getRequestBody(request)
        return remoteService.loadAgentLimit(requestBody)
    }

}