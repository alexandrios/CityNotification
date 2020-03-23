package com.chelinvest.notification.model

class ObjParamV01 : IObjList {

    lateinit var id: String
    lateinit var name: String
    lateinit var value: String
    lateinit var value_01: String

    override fun toString(): String {
        val result = StringBuilder("")
        result.append("id=")
        result.append(id)
        result.append("; name=")
        result.append(name)
        result.append("; value")
        result.append(value)
        result.append("; value_01=")
        result.append(value_01)

        return result.toString()
    }
}