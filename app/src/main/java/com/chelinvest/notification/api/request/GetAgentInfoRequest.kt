package com.chelinvest.notification.api.request

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import com.chelinvest.notification.api.request.IRequestElement

@Root(name = "get_agent_info")
class GetAgentInfoRequest : IRequestElement {

    @field:Element(name = "session_id", required = true)
    var session_id: String? = null

    @field:Element(name = "result_type", required = true)
    var resultType: String = "X_ORG_NAME"
}