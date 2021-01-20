package com.chelinvest.notification.data.remote

import android.util.Log
import com.chelinvest.notification.api.request.*
import com.chelinvest.notification.api.response.MainResponse
import com.chelinvest.notification.api.response.MainDeliverySubscriptionResponse
import com.chelinvest.notification.model.ObjParam
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
import java.io.StringWriter
import java.net.URLEncoder
import java.util.HashMap
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

    fun getDeliverySubscriptionForBranch(sessionId: String, branchShort: String): Call<MainDeliverySubscriptionResponse> {
        val request = MainRequest(GetDeliverySubscriptionForBranchRequest().apply {
            this.session_id = sessionId
            this.branch_short = branchShort
        })
        val requestBody = getRequestBody(request)
        return remoteService.getDeliverySubscriptionForBranch(requestBody)
    }

    fun deleteDeliverySubscriptionForBranch(sessionId: String, branchShort: String, subscriptionId: String): Call<MainResponse> {
        val request = MainRequest(DeleteDeliverySubscriptionForBranchRequest().apply {
            this.session_id = sessionId
            this.branch_short = branchShort
            this.subscription_id = subscriptionId
        })
        val requestBody = getRequestBody(request)
        return remoteService.deleteDeliverySubscriptionForBranch(requestBody)
    }

    fun getInputFields(sessionId: String, branchShort: String): Call<MainResponse> {
        val request = MainRequest(GetInputFieldsForBranchRequest().apply {
            this.session_id = sessionId
            this.branch_short = branchShort
        })
        val requestBody = getRequestBody(request)
        return remoteService.getInputFields(requestBody)
    }

    fun createSubscription(sessionId: String, branchShort: String, map: HashMap<String, ObjParam>): Call<MainDeliverySubscriptionResponse> {
        val request = MainRequest(CreateDeliverySubscriptionForBranchRequest().apply {
            this.session_id = sessionId
            this.branch_short = branchShort
            /* если @field:ElementArray(name="field_names_list", entry="field_name")
                this.field_names_list = Array(map.entries.size, {""})
                this.field_values_list = Array(map.entries.size, {""})
                var i = 0
                for (item in map.entries) {
                    val fieldName = item.key
                    this.field_names_list!!.set(i, fieldName)

                    val fieldValue = item.value.value
                    this.field_values_list!!.set(i, fieldValue)
                    i++
                }
            */
            this.field_names_list = ArrayList()
            this.field_values_list = ArrayList()

            for (item in map.entries) {
                val fieldName = item.key
                (this.field_names_list as ArrayList<String>).add(fieldName)

                val fieldValue = item.value.value
                (this.field_values_list as ArrayList<String>).add(fieldValue)
            }
        })
        val requestBody = getRequestBody(request)

        return remoteService.createSubscription(requestBody)
    }

    fun getFieldValues(sessionId: String, branchShort: String, fieldId: String): Call<MainResponse> {
        val request = MainRequest(GetFieldValuesForFieldRequest().apply {
            this.session_id = sessionId
            this.branch_short = branchShort
            this.field_id = fieldId
        })
        val requestBody = getRequestBody(request)
        return remoteService.getFieldValues(requestBody)
    }
}