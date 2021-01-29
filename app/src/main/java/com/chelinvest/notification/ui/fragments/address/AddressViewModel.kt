package com.chelinvest.notification.ui.fragments.address

import android.app.Application
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.chelinvest.notification.api.response.DelivetypeAddrsResponse
import com.chelinvest.notification.api.response.MainResponse
import com.chelinvest.notification.api.response.mapper.DelivetypeAddrsResponseMapper
import com.chelinvest.notification.data.Repository
import com.chelinvest.notification.model.DelivetypeAddrs
import com.chelinvest.notification.ui.BaseViewModel
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.chelinvest.notification.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AddressViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {

    val errorLiveEvent = SingleLiveEvent<String>()
    val delivetypeAddrsLiveEvent = SingleLiveEvent<ArrayList<DelivetypeAddrs>>()

    // Признак того, что адрес был изменен (или создан новый) в EditAddressFragment
    val editSaved = MutableLiveData<Boolean>()
    fun setEditSave(value: Boolean) {
        repository.setChangeAddress(value)
    }
    fun getEditSave() {
        editSaved.value = repository.getChangeAddress()
    }

    val recyclerViewExpandableItemManagerState = MutableLiveData<Parcelable?>()
    fun setStateSave(result: Parcelable?) {
        recyclerViewExpandableItemManagerState.value = result
    }

    // Получить список адресов, привязанных к подписке
    fun getDelivetypeAddrs(subscriptionId: String) {
        val sessionId = repository.getSessionId()
        Log.d(LOG_TAG, "AddressViewModel getDelivetypeAddrs sessionId=$sessionId")

        if (sessionId == null) {
            // TODO Текущая сессия прервана. Войдите заново.
            errorLiveEvent.postValue("sessionId is null")
        } else {
            val branchShort = repository.getBranchShort() ?: return
            Log.d(LOG_TAG,
                "AddressViewModel getDelivetypeAddrs branchShort=$branchShort")

            val objParamObjsList = ArrayList<DelivetypeAddrs>()

            //view.showProgress()
            repository.getDeliveryTypesForSubscription(sessionId, branchShort, subscriptionId).enqueue(object : Callback<MainResponse> {
                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.d(LOG_TAG, "SubscrViewModel onFailure: ${t.message}")
                    handleRequestFailure(t)
                    errorLiveEvent.postValue(t.message)
                }

                override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d(LOG_TAG,"SubscrViewModel onResponse: sessionId=${result?.sessionId}")
                        Log.d(LOG_TAG,"SubscrViewModel onResponse: errorNote=${result?.errorNote}")

                        if (result != null) {
                            if (!result.errorNote.isNullOrEmpty()) {
                                errorLiveEvent.postValue(result.errorNote)
                            }

                            // Обработать полученный список: убрать не свои токены для APP_PUSH
                            val token = repository.getFCMToken() ?: ""

                            val mapper = DelivetypeAddrsResponseMapper()

                            result.elements?.forEach { element ->
                                mapper.map(token, element as DelivetypeAddrsResponse)?.let { element2 ->
                                    objParamObjsList.add(element2)
                                }
                            }

                            delivetypeAddrsLiveEvent.postValue(objParamObjsList)
                        }
                    }
                }
            })
        }
    }

}