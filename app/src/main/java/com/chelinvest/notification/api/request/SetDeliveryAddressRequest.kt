package com.chelinvest.notification.api.request

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "set_delivery_address_for_subscription")
class SetDeliveryAddressRequest : IRequestElement {

    @field:Element(name = "session_id", required = true)
    var session_id: String? = null

    @field:Element(name="branch_short", required = true)
    var branch_short: String? = null

    @field:Element(name = "subscription_id", required = true)
    var subscription_id: String? = null

    @field:Element(name = "address", required = true)
    var address: String? = null

    @field:Element(name = "delivetype_id", required = true)
    var delivetype_id: String? = null

    @field:Element(name = "old_address", required = false)
    var old_address: String? = null

    @field:Element(name = "is_confirm", required = false)
    var is_confirm: String? = null

    @field:Element(name = "start_hour", required = false)
    var start_hour: Int? = null

    @field:Element(name = "finish_hour", required = false)
    var finish_hour: Int? = null

    @field:Element(name = "timezone", required = false)
    var timezone: Int? = null
}