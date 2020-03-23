package com.chelinvest.notification.model

import java.io.Serializable

class DelivetypeAddrs: Serializable {

    lateinit var id: String
    lateinit var name: String
    lateinit var short_name: String
    lateinit var value_name: String
    lateinit var has_send_period: String
    lateinit var value_list: List<ObjParam>
    lateinit var address_list: List<DeliveAddrBranch>

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

        if (value_list.size > 0) {
            result.append("; value_list={")
            for (i in 0..value_list.size - 1) {
                result.append("(")
                result.append(value_list[i].toString())
                result.append(")")
            }
            result.append("}")
        }

        return result.toString()
    }
}