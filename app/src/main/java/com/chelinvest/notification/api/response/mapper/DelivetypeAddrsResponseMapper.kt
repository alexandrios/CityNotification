package com.chelinvest.notification.api.response.mapper

import com.chelinvest.notification.api.response.*
import com.chelinvest.notification.model.*
import com.chelinvest.notification.utils.Constants.APP_PUSH

class DelivetypeAddrsResponseMapper {

    fun map(token: String, from: DelivetypeAddrsResponse): DelivetypeAddrs {

        val objParam = DelivetypeAddrs()
        objParam.id = from.id ?: ""
        objParam.name = from.name ?: ""
        objParam.short_name = from.short_name ?: ""
        objParam.value_name = from.value_name ?: ""
        objParam.has_send_period = from.has_send_period ?: ""

        val mapper = ObjParamResponseMapper()
        val objList = ArrayList<ObjParam>()
        from.value_list?.forEach { response ->
            mapper.map(response).let { objParam ->
                objList.add(objParam)
            }
        }
        objParam.value_list = objList

        val mapper2 = DeliveAddrBranchResponseMapper()
        val addrList = ArrayList<DeliveAddrBranch>()
        from.address_list?.forEach { response ->
            // Обработать полученный список: убрать не свои токены для APP_PUSH
            if (objParam.short_name == APP_PUSH && response.address == token || objParam.short_name != APP_PUSH) {
                mapper2.map(response)?.let { addr ->
                    addrList.add(addr)
                }
            }
        }
        objParam.address_list = addrList

        return objParam
    }

}