package com.chelinvest.notification.data.remote

import com.chelinvest.notification.api.response.MainResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RemoteService {

    @POST("login_pass")
    fun getSession(@Body requestBody: RequestBody): Call<MainResponse>

    @POST("get_delivery_branch")
    fun loadDeliveryBranches(@Body requestBody: RequestBody): Call<MainResponse>

    @POST("get_agent_info")
    fun loadAgentInfo(@Body requestBody: RequestBody): Call<MainResponse>

    @POST("check_agent_limit")
    fun loadAgentLimit(@Body requestBody: RequestBody): Call<MainResponse>

}

