package com.chelinvest.notification.api.response.mapper

import com.chelinvest.notification.api.response.DelivetypeExpResponse
import com.chelinvest.notification.api.response.ObjParamResponse
import com.chelinvest.notification.model.DelivetypeExp
import com.chelinvest.notification.model.ObjParam

class DelivetypeExpResponseMapper {

    fun map(from: DelivetypeExpResponse): DelivetypeExp {

        val objParam = DelivetypeExp()
        objParam.id = from.id ?: ""
        objParam.name = from.name ?: ""
        objParam.short_name = from.short_name ?: ""
        objParam.value_name = from.value_name ?: ""
        objParam.has_send_period = from.has_send_period ?: ""

        // if (вызван из GetDeliveryBranchInteractor)
        val mapper = ObjParamResponseMapper()
        val objList = ArrayList<ObjParam>()
        from.value_list?.forEach { response ->
            mapper.map(response).let { objParam ->
                objList.add(objParam)
            }
        }
        objParam.value_list = objList

        return objParam
    }

}