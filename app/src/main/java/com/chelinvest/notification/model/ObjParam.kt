package com.chelinvest.notification.model

import java.io.Serializable

class ObjParam(var id: String, var name: String, var value: String) : Serializable {

    override fun toString(): String {
        val result = StringBuilder("")
        result.append("id=")
        result.append(id)
        result.append("; name=")
        result.append(name)
        result.append("; value=")
        result.append(value)

        return result.toString()
    }
}