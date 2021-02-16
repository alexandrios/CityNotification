package com.chelinvest.notification.api.response.mapper

import com.chelinvest.notification.api.response.ObjParamResponse
import com.chelinvest.notification.model.ObjParam

class ObjParamResponseMapper {

    fun map(from: ObjParamResponse): ObjParam {
        return ObjParam(from.id ?: "", from.name ?: "", from.value ?: "")
    }

}