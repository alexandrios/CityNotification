package com.chelinvest.notification.api.request

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root
class LoginRequest : IRequestElement {

/*
    @field:Element(name = "os_type", required = false)
    var osType: String = "Android"

    @field:Element(name = "os_version", required = false)
    var osVersion: String? = null
*/
    @field:Element(name = "user", required = true)
    var user: String? = null

    @field:Element(name = "password", required = true)
    var password: String? = null

}