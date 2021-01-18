package com.chelinvest.notification.api.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.ElementListUnion
import org.simpleframework.xml.Root
import com.chelinvest.notification.api.response.obj_param_objs.GetDeliveryBranchResponse
import com.chelinvest.notification.api.response.obj_param_objs.GetDeliverySubscriptionForBranchResponse

@Root(name = "xgate_result", strict = false)
class MainResponse {

    @field:Element(name = "result", required = false)
    var result: Long? = null

    @field:Element(name = "error_note", required = false)
    var errorNote: String? = null

    @field:Element(name = "datasource", required = false)
    var dataSource: String? = null

    @field:Element(name = "row_num", required = false)
    var rowNum: Long? = null

    @field:Element(name = "session_id", required = false)
    var sessionId: String? = null

    @field:Element(name = "value", required = false)
    var value: String? = null

    @field:Element(name = "request_id", required = false)
    var requestId: String? = null

    @field:Element(name = "class_name", required = false)
    var class_name: String? = null

    @field:Element(name = "org_name", required = false, type = OrgNameResponse::class)
    var org_name: OrgNameResponse? = null

    @field:ElementListUnion(
            ElementList(inline = true, type = GetDeliveryBranchResponse::class, required = false),
            ElementList(inline = true, type = GetDeliverySubscriptionForBranchResponse::class, required = false),
            ElementList(inline = true, type = DelivetypeExpResponse::class, required = false),
            ElementList(inline = true, type = DelivetypeExpSortResponse::class, required = false),
            ElementList(inline = true, type = ObjParamResponse::class, required = false),
            ElementList(inline = true, type = ObjParamV01Response::class, required = false),
            ElementList(inline = true, type = ObjAnyResponse::class, required = false),
            ElementList(inline = true, type = DelivetypeAddrsResponse::class, required = false),
            ElementList(inline = true, type = DeliveAddrBranchResponse::class, required = false),
            ElementList(inline = true, type = DeliveTypeResponse::class, required = false)
    )
    var elements: List<IResponseElement>? = null

    override fun toString(): String {
        return "$result $errorNote $dataSource $rowNum $sessionId {$elements}"
    }

}