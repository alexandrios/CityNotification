package com.chelinvest.notification.api.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


@Root(name = "delivetype_exp", strict = false)
class DelivetypeExpResponse : IResponseElement {

    @field:Element(name = "id", required = false)
    var id: String? = null

    @field:Element(name = "name", required = false)
    var name: String? = null

    @field:Element(name = "short_name", required = false)
    var short_name: String? = null

    @field:Element(name = "value_name", required = false)
    var value_name: String? = null

    @field:Element(name = "has_send_period", required = false)
    var has_send_period: String? = null

    @field:ElementList(name = "value_list", required = false, type = ObjParamResponse::class)
    var value_list: Collection<ObjParamResponse>? = null

}