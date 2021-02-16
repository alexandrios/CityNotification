package com.chelinvest.notification.api.response.mapper

import com.chelinvest.notification.api.response.ObjParamV01Response
import com.chelinvest.notification.model.ObjParamV01

class ObjParamV01ResponseMapper {

    fun map(from: ObjParamV01Response): ObjParamV01 {

        val objParam = ObjParamV01()
        objParam.id = from.id ?: ""
        objParam.name = from.name ?: ""
        objParam.value = from.value ?: ""
        objParam.value_01 = from.value_01 ?: ""

        return objParam
    }

}