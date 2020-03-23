package com.chelinvest.notification.api.request

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import com.chelinvest.notification.api.request.IRequestElement

@Root(name = "check_agent_limit")
class CheckAgentLimitRequest : IRequestElement {

    @field:Element(name = "session_id", required = true)
    var session_id: String? = null

}