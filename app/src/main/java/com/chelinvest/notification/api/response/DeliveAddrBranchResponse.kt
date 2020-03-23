package com.chelinvest.notification.api.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "deliveaddr_branch", strict = false)
class DeliveAddrBranchResponse : IResponseElement {

    @field:Element(name = "id", required = false)
    var id: String? = null

    @field:Element(name = "delive_type", required = false, type = DeliveTypeResponse::class)
    var delive_type: DeliveTypeResponse? = null

    @field:Element(name = "address", required = false)
    var address: String? = null

    @field:Element(name = "is_valid", required = false)
    var is_valid: String? = null

    @field:Element(name = "is_confirmed", required = false)
    var is_confirmed: String? = null

    @field:Element(name = "start_hour", required = false)
    var start_hour: String? = null

    @field:Element(name = "finish_hour", required = false)
    var finish_hour: String? = null

    @field:Element(name = "timezone", required = false)
    var timezone: String? = null

}