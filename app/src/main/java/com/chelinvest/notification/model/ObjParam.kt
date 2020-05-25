package com.chelinvest.notification.model

import java.io.Serializable

class ObjParam(var id: String, var name: String, var value: String) : Serializable {

    /*
class ObjParam() : Serializable {
    lateinit var id: String
    lateinit var name: String
    lateinit var value: String

    constructor(id: String, name: String, value: String) : this() {
        this.id = id
        this.name = name
        this.value = value
    }
    */

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