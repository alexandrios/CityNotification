package com.chelinvest.notification.api.request

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "get_delivery_types_for_subscription")
class GetDeliveryTypesForSubscriptionRequest : IRequestElement {

    @field:Element(name = "session_id", required = true)
    var session_id: String? = null

    @field:Element(name = "result_type", required = true)
    var resultType: String = "X_DELIVETYPE_ADDRS"  //"X_DELIVETYPE_ADDRS_SORT"

    @field:Element(name="branch_short", required = true)
    var branch_short: String? = null

    @field:Element(name="subscription_id", required = true)
    var subscription_id: String? = null

    @field:Element(name = "app_code", required = true)
    var app_code: String = "A18D9F3D7F8030C2"

    @field:Element(name = "app_pin", required = true)
    var app_pin: String = "01A39CD182C7D2F4D4831BFD"
}