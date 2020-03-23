package com.chelinvest.notification.api

class OkResponse {

    var errorCode: Int = 0

    var errorMessage: String? = null

    var isSuccessful: Boolean = false

    var body: String? = null

    var cookies: List<String>? = null

    override fun toString(): String = "$errorCode $errorMessage $body"
}