package com.chelinvest.notification.api.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "obj_param_v01", strict = false)
class ObjParamV01Response : IResponseElement {

    @field:Element(name = "id", required = false)
    var id: String? = null

    @field:Element(name = "name", required = false)
    var name: String? = null

    @field:Element(name = "value", required = false)
    var value: String? = null

    @field:Element(name = "value_01", required = false)
    var value_01: String? = null

}