package com.chelinvest.notification.model

class DelivetypeExp: IObjList {

    lateinit var id: String
    lateinit var name: String
    lateinit var short_name: String
    lateinit var value_name: String
    lateinit var has_send_period: String
    lateinit var value_list: List<ObjParam>

    override fun toString(): String {
        val result = StringBuilder("")
        result.append("id=")
        result.append(id)
        result.append("; name=")
        result.append(name)
        result.append("; short_name=")
        result.append(short_name)
        result.append("; value_name=")
        result.append(value_name)
        result.append("; has_send_period=")
        result.append(has_send_period)

        if (value_list.isNotEmpty()) {
            result.append("; value_list={")
            for (element in value_list) {
                result.append("(")
                result.append(element.toString())
                result.append(")")
            }
            result.append("}")
        }

        return result.toString()
    }
}