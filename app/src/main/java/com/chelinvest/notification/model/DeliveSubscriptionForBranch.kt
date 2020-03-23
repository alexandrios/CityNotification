package com.chelinvest.notification.model

import java.io.Serializable

class DeliveSubscriptionForBranch : Serializable {

    lateinit var id: String
    lateinit var name: String
    lateinit var value: String
    lateinit var objList: List<ObjParamV01>

    //
    constructor ()

    constructor(id: String, name: String, value: String) {
        this.id = id
        this.name = name
        this.value = value
        this.objList = ArrayList()
    }
    //

    override fun toString(): String {
        val result = StringBuilder("")
        result.append("id=")
        result.append(id)
        result.append("; name=")
        result.append(name)
        result.append("; value=")
        result.append(value)

        if (objList.size > 0) {
            result.append("; obj_list={")
            for (i in 0..objList.size - 1) {
                result.append("(")
                result.append(objList[i].toString())
                result.append(")")
            }
            result.append("}")
        }

        return result.toString()
    }

}