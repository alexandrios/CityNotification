package com.chelinvest.notification.data.remote

import com.chelinvest.notification.api.response.MainResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RemoteService {

    @POST("login_pass")
    fun getSession(@Body requestBody: RequestBody): Call<MainResponse>

}

