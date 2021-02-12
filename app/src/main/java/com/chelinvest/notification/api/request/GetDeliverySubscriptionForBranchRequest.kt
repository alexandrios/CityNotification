package com.chelinvest.notification.api.request

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "get_delivery_subscription_for_branch")
class GetDeliverySubscriptionForBranchRequest : IRequestElement {

    @field:Element(name="session_id", required = true)
    var session_id: String? = null

    @field:Element(name="result_type", required = true)
    var result_type: String = "X_OBJ_PARAM_OBJS"

    @field:Element(name="branch_short", required = true)
    var branch_short: String? = null  // например, AGENT_REDUCTION2LIMIT
}