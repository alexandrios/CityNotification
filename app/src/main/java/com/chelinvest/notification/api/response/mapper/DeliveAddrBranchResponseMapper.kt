package com.chelinvest.notification.api.response.mapper

import com.chelinvest.notification.api.response.DeliveAddrBranchResponse
import com.chelinvest.notification.model.*

class DeliveAddrBranchResponseMapper {

    fun map(from: DeliveAddrBranchResponse): DeliveAddrBranch? {

        val objParam = DeliveAddrBranch()
        objParam.id = from.id ?: return null
        val mapper = DeliveTypeResponseMapper()
        if (from.delive_type == null) {
            objParam.delive_type = DeliveType()
        }
        else {
            objParam.delive_type = mapper.map(from.delive_type!!)
        }
        objParam.address = from.address ?: ""
        objParam.is_valid = from.is_valid ?: ""
        objParam.is_confirmed = from.is_confirmed ?: ""
        objParam.start_hour = from.start_hour ?: ""
        objParam.finish_hour = from.finish_hour ?: ""
        objParam.timezone = from.timezone ?: ""

        return objParam
    }

}