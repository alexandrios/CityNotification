package com.chelinvest.notification.ui.fragments.subscr.edit

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.chelinvest.notification.api.response.MainDeliverySubscriptionResponse
import com.chelinvest.notification.api.response.MainResponse
import com.chelinvest.notification.api.response.mapper.GetDeliverySubscriptionForBranchResponseMapper
import com.chelinvest.notification.api.response.obj_param_objs.GetDeliverySubscriptionForBranchResponse
import com.chelinvest.notification.data.Repository
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import com.chelinvest.notification.ui.BaseViewModel
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class EditSubscrViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {

    val errorLiveEvent = SingleLiveEvent<String>()
    val deliverySubscriptionsLiveEvent = SingleLiveEvent<ArrayList<DeliveSubscriptionForBranch>>()

    // Признак того, что был изменен элемент списка агентов (в EditSubscrFragment)
    //val editSaved = MutableLiveData<Boolean>()
    fun setEditSave(value: Boolean) {
        repository.setChangeSubscrList(value)
        //editSaved.value = value
    }

    // Выполнить команду 1.7. update_delivery_subscription_for_branch
    fun updateSubscr(subscriptionId: String,  description: String, isActive: Int) {
        val sessionId = repository.getSessionId()
        Log.d(Constants.LOG_TAG, "EditSubscrViewModel updateSubscr sessionId=$sessionId")

        if (sessionId == null) {
            // TODO Текущая сессия прервана. Войдите заново.
            errorLiveEvent.postValue("sessionId is null")
        } else {
            val branchShort = repository.getBranchShort() ?: return
            Log.d(Constants.LOG_TAG,"EditSubscrViewModel updateSubscr branchShort=$branchShort")

            val objParamObjsList = ArrayList<DeliveSubscriptionForBranch>()
            //view.showProgress()
            repository.updateDeliverySubscriptionForBranch(sessionId, branchShort, subscriptionId,
                    description, isActive).enqueue(object : Callback<MainDeliverySubscriptionResponse> {
                override fun onFailure(call: Call<MainDeliverySubscriptionResponse>, t: Throwable) {
                    Log.d(Constants.LOG_TAG, "EditSubscrViewModel onFailure: ${t.message}")
                    errorLiveEvent.postValue(t.message)
                }

                override fun onResponse(call: Call<MainDeliverySubscriptionResponse>, response: Response<MainDeliverySubscriptionResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d(Constants.LOG_TAG,"EditSubscrViewModel onResponse: sessionId=${result?.sessionId}")
                        Log.d(Constants.LOG_TAG,"EditSubscrViewModel onResponse: errorNote=${result?.errorNote}")

                        if (result != null) {
                            if (!result.errorNote.isNullOrEmpty()) {
                                errorLiveEvent.postValue(result.errorNote)
                            }

                            val mapper = GetDeliverySubscriptionForBranchResponseMapper()
                            result.elements?.forEach { element ->
                                mapper.map(element as GetDeliverySubscriptionForBranchResponse)?.let { element2 ->
                                    objParamObjsList.add(element2)
                                }
                            }

                            deliverySubscriptionsLiveEvent.postValue(objParamObjsList)
                        }
                    }
                }
            })
        }
    }

}