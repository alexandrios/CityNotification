package com.chelinvest.notification.api.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "obj_any", strict = false)
class ObjAnyResponse : IResponseElement {

    @field:Element(name = "id", required = false)
    var id: String? = null

    @field:Element(name = "name", required = false)
    var name: String? = null

}