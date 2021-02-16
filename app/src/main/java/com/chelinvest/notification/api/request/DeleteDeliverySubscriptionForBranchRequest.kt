package com.chelinvest.notification.api.request

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "delete_delivery_subscription_for_branch")
class DeleteDeliverySubscriptionForBranchRequest : IRequestElement {

    @field:Element(name="session_id", required = true)
    var session_id: String? = null

    @field:Element(name="branch_short", required = true)
    var branch_short: String? = null  // например, AGENT_REDUCTION2LIMIT

    @field:Element(name="subscription_id", required = true)
    var subscription_id: String? = null

}