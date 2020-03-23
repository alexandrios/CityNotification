package com.chelinvest.notification.exception

import com.chelinvest.notification.api.OkResponse

class HttpException(response: OkResponse) : CustomException() {

    var errorCode: Int = 0
    var errorMessage: String = ""

    init {
        errorCode = response.errorCode
        errorMessage = response.errorMessage ?: ""
    }

}