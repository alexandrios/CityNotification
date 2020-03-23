package com.chelinvest.notification.api.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "org_name", strict = false)
class OrgNameResponse : IResponseElement {

    @field:Element(name = "id", required = false)
    lateinit var id: String

    @field:Element(name = "name", required = false)
    lateinit var name: String

}