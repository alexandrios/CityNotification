package com.chelinvest.notification.api.response.obj_param_objs

import com.chelinvest.notification.api.response.DelivetypeExpResponse
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import com.chelinvest.notification.api.response.IResponseElement
import com.chelinvest.notification.api.response.ObjParamV01Response
import org.simpleframework.xml.ElementListUnion

//@Root(name = "obj_subscription_objs", strict = false)
@Root(name = "obj_param_objs", strict = false)
class GetDeliverySubscriptionForBranchResponse : IResponseElement {

    @field:Element(name = "id", required = false)
    var id: String? = null

    @field:Element(name = "name", required = false)
    var name: String? = null

    @field:Element(name = "value", required = false)
    var value: String? = null

    @field:ElementList(name = "obj_list", type = ObjParamV01Response::class)
    var objList: Collection<ObjParamV01Response>? = null

}