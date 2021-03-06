package com.chelinvest.notification.api.request

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "get_input_fields_for_branch")
class GetInputFieldsForBranchRequest : IRequestElement {

    @field:Element(name = "session_id", required = true)
    var session_id: String? = null

    @field:Element(name="branch_short", required = true)
    var branch_short: String? = null

    @field:Element(name = "result_type", required = true)
    var resultType: String = "X_OBJ_ANY"

}