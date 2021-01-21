package com.chelinvest.notification.api.response.mapper

import com.chelinvest.notification.api.response.ObjParamV01Response
import com.chelinvest.notification.api.response.obj_param_objs.GetDeliverySubscriptionForBranchResponse
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import com.chelinvest.notification.model.ObjParamV01

class GetDeliverySubscriptionForBranchResponseMapper {

    fun map(from: GetDeliverySubscriptionForBranchResponse): DeliveSubscriptionForBranch? {
        val objParamObjs = DeliveSubscriptionForBranch()
        objParamObjs.id = from.id ?: ""
        objParamObjs.name = from.name ?: ""
        objParamObjs.value = from.value ?: ""

        val mapper = ObjParamV01ResponseMapper()
        val objList = ArrayList<ObjParamV01>()
        from.objList?.forEach { response ->
            mapper.map(response as ObjParamV01Response)?.let { objParam ->
                objList.add(objParam)
            }
        }
        objParamObjs.objList = objList

        return objParamObjs
    }

}