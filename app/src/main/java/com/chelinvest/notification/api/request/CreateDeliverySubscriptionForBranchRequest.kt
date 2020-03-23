package com.chelinvest.notification.api.request

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import com.chelinvest.notification.api.request.IRequestElement

@Root(name = "create_delivery_subscription_for_branch")
class CreateDeliverySubscriptionForBranchRequest : IRequestElement {

    @field:Element(name="session_id", required = true)
    var session_id: String? = null

    @field:Element(name="branch_short", required = true)
    var branch_short: String? = null  // например, AGENT_REDUCTION2LIMIT

    //@field:ElementList(name="field_names_list", type = FieldNameRequest::class)
    //@field:ElementArray(name="field_names_list", entry="field_name")
    //var field_names_list: Array<String>? = null
    @field:ElementList(type=String::class, entry="field_name")
    var field_names_list: ArrayList<String>? = null

    //@field:ElementList(name="field_values_list", type = FieldValueRequest::class)
    @field:ElementList(type=String::class, entry="field_value")
    var field_values_list: ArrayList<String>? = null
}