package com.chelinvest.notification.model

import java.io.Serializable

class ObjAny : Serializable {

    lateinit var id: String
    lateinit var name: String

    override fun toString(): String {
        val result = StringBuilder("")
        result.append("id=")
        result.append(id)
        result.append("; name=")
        result.append(name)

        return result.toString()
    }
}