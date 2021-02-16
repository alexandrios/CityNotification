package com.chelinvest.notification.model

import java.io.Serializable

class DeliveSubscriptionForBranch : Serializable {
    lateinit var id: String
    lateinit var name: String
    lateinit var value: String
    lateinit var objList: List<ObjParamV01>

    override fun toString(): String {
        val result = StringBuilder("")
        result.append("id=")
        result.append(id)
        result.append("; name=")
        result.append(name)
        result.append("; value=")
        result.append(value)

        if (objList.isNotEmpty()) {
            result.append("; obj_list={")
            for (element in objList) {
                result.append("(")
                result.append(element.toString())
                result.append(")")
            }
            result.append("}")
        }

        return result.toString()
    }
}