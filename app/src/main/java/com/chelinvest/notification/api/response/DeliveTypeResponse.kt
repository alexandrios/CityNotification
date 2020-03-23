package com.chelinvest.notification.api.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "delive_type", strict = false)
class DeliveTypeResponse : IResponseElement {

    @field:Element(name = "id", required = false)
    var id: String? = null

}