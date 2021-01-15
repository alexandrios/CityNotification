package com.chelinvest.notification.data.remote

import com.chelinvest.notification.model.session.Session
import com.chelinvest.notification.utils.Constants.GET_WORD_HTTP_REQUEST
import retrofit2.Call
import retrofit2.http.*

interface RemoteService {

    @GET(GET_WORD_HTTP_REQUEST)
    fun getSession(@Query("user", encoded = true) user: String,
                   @Query("pass", encoded = true) pass: String): Call<Session>

}