package com.chelinvest.notification.api.response.mapper

import com.chelinvest.notification.api.response.obj_param_objs.GetDeliveryBranchResponse
import com.chelinvest.notification.model.IObjList
import com.chelinvest.notification.model.ObjParamObjs

// используется при result_type = X_OBJ_PARAM_OBJS – с указанием типов рассылок для каждого типа уведомлений
// из GetDeliveryBranchInteractor.loadDeliveryBranchesWithAllows
class GetDeliveryBranchResponseMapper {

    fun map(from: GetDeliveryBranchResponse): ObjParamObjs? {
        val objParamObjs = ObjParamObjs()
        objParamObjs.id = from.id ?: ""
        objParamObjs.name = from.name ?: ""
        objParamObjs.value = from.value ?: ""

        val mapper = DelivetypeExpResponseMapper()
        val objList = ArrayList<IObjList>()
        from.objList?.forEach { response ->
            mapper.map(response)?.let { objParam ->
                objList.add(objParam)
            }
        }
        objParamObjs.objList = objList

        return objParamObjs
    }

}