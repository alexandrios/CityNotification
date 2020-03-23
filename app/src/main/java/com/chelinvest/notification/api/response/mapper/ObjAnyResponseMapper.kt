package com.chelinvest.notification.api.response.mapper

import com.chelinvest.notification.api.response.ObjAnyResponse
import com.chelinvest.notification.model.ObjAny

class ObjAnyResponseMapper {

    fun map(from: ObjAnyResponse): ObjAny? {

        val objAny = ObjAny()
        objAny.id = from.id ?: ""
        objAny.name = from.name ?: ""

        return objAny
    }

}