package com.chelinvest.notification.api.response.mapper

import com.chelinvest.notification.api.response.*
import com.chelinvest.notification.model.*

class DeliveTypeResponseMapper {

    fun map(from: DeliveTypeResponse): DeliveType {

        val obj = DeliveType()
        if (from.id != null )
            obj.id = from.id!!

        return obj
    }

}