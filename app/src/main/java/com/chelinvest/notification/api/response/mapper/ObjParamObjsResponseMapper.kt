package com.chelinvest.notification.api.response.mapper

import com.chelinvest.notification.api.response.obj_param_objs.ObjParamObjsResponse
import com.chelinvest.notification.model.IObjList
import com.chelinvest.notification.model.ObjParamObjs

class ObjParamObjsResponseMapper {

    fun map(from: ObjParamObjsResponse): ObjParamObjs {
        val objParamObjs = ObjParamObjs()
        objParamObjs.id = from.id ?: ""
        objParamObjs.name = from.name ?: ""
        objParamObjs.value = from.value ?: ""

        // if (вызван из GetDeliveryBranchInteractor)
        val mapper = DelivetypeExpResponseMapper()
        val objList = ArrayList<IObjList>()
        from.objList?.forEach { response ->
            mapper.map(response).let { objParam ->
                objList.add(objParam)
            }
        }
        objParamObjs.objList = objList

        return objParamObjs
    }

}