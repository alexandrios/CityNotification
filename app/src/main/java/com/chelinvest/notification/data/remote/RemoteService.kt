package com.chelinvest.notification.data.remote

import com.chelinvest.notification.model.session.Session
import com.chelinvest.notification.utils.Constants.GET_WORD_HTTP_REQUEST
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*

interface RemoteService {

    @POST("login_pass")
    fun getSession(@Body requestBody: RequestBody): Call<String>

}