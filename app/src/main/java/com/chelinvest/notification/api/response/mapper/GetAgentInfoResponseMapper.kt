package com.chelinvest.notification.api.response.mapper

import com.chelinvest.notification.api.response.OrgNameResponse
import com.chelinvest.notification.model.OrgName

class GetAgentInfoResponseMapper {

    fun map(from: OrgNameResponse): OrgName {

        val orgName = OrgName()
        orgName.id = from.id
        orgName.name = from.name
        return orgName
    }

}