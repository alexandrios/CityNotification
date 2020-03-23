package com.chelinvest.notification.api.response.mapper

import com.chelinvest.notification.api.response.ObjParamResponse
import com.chelinvest.notification.model.ObjParam

class ObjParamResponseMapper {

    fun map(from: ObjParamResponse): ObjParam? {

        val objParam = ObjParam()
        objParam.id = from.id ?: ""
        objParam.name = from.name ?: ""
        objParam.value = from.value ?: ""

        return objParam
    }

}