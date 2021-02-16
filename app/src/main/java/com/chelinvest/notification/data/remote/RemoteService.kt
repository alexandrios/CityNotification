package com.chelinvest.notification.data.remote

import com.chelinvest.notification.api.response.MainResponse
import com.chelinvest.notification.api.response.MainDeliverySubscriptionResponse
import okhttp3.RequestBody
import retrofit2.Call
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

    @POST("get_delivery_subscription_for_branch")
    fun getDeliverySubscriptionForBranch(@Body requestBody: RequestBody): Call<MainDeliverySubscriptionResponse>

    @POST("delete_delivery_subscription_for_branch")
    fun deleteDeliverySubscriptionForBranch(@Body requestBody: RequestBody): Call<MainResponse>

    @POST("get_input_fields_for_branch")
    fun getInputFields(@Body requestBody: RequestBody): Call<MainResponse>

    @POST("create_delivery_subscription_for_branch")
    fun createSubscription(@Body requestBody: RequestBody): Call<MainDeliverySubscriptionResponse>

    @POST("get_field_values_for_field")
    fun getFieldValues(@Body requestBody: RequestBody): Call<MainResponse>

    @POST("update_delivery_subscription_for_branch")
    fun updateDeliverySubscriptionForBranch(@Body requestBody: RequestBody): Call<MainDeliverySubscriptionResponse>

    @POST("get_delivery_types_for_subscription")
    fun getDeliveryTypesForSubscription(@Body requestBody: RequestBody): Call<MainResponse>

    @POST("set_delivery_address_for_subscription")
    fun setDeliveryAddressForSubscription(@Body requestBody: RequestBody): Call<MainResponse>
}

