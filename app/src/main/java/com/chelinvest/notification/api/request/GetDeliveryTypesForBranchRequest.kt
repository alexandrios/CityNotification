package com.chelinvest.notification.api.request

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import com.chelinvest.notification.api.request.IRequestElement

@Root(name = "get_delivery_types_for_branch")
class GetDeliveryTypesForBranchRequest : IRequestElement {

    @field:Element(name = "session_id", required = true)
    var session_id: String? = null

    @field:Element(name = "result_type", required = true)
    var resultType: String = "X_DELIVETYPE_EXP_SORT"
    //var resultType: String = "X_DELIVETYPE_NAME"

    @field:Element(name="branch_short", required = true)
    var branch_short: String? = null

    @field:Element(name = "app_code", required = true)
    var app_code: String = "A18D9F3D7F8030C2"

    @field:Element(name = "app_pin", required = true)
    var app_pin: String = "01A39CD182C7D2F4D4831BFD"
}