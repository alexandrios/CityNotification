package com.chelinvest.notification.api.response.obj_param_objs

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import com.chelinvest.notification.api.response.DelivetypeExpResponse
import com.chelinvest.notification.api.response.IResponseElement

@Root(name = "obj_delivetype_objs", strict = false)
class ObjParamObjsResponse : IResponseElement {

    @field:Element(name = "id", required = false)
    var id: String? = null

    @field:Element(name = "name", required = false)
    var name: String? = null

    @field:Element(name = "value", required = false)
    var value: String? = null

    @field:ElementList(name = "obj_list", type = DelivetypeExpResponse::class)
    var objList: Collection<DelivetypeExpResponse>? = null
}