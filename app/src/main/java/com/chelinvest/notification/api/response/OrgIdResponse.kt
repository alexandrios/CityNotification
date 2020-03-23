package com.chelinvest.notification.api.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "org_id", strict = false)
class OrgIdResponse : IResponseElement {

    @field:Element(name = "id", required = false)
    var id: String? = null

}