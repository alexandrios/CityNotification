package com.chelinvest.notification.ui.fragments.subscr

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.chelinvest.notification.api.response.MainResponse
import com.chelinvest.notification.api.response.ObjAnyResponse
import com.chelinvest.notification.api.response.ObjParamResponse
import com.chelinvest.notification.api.response.mapper.GetDeliverySubscriptionForBranchResponseMapper
import com.chelinvest.notification.api.response.mapper.ObjAnyResponseMapper
import com.chelinvest.notification.api.response.mapper.ObjParamResponseMapper
import com.chelinvest.notification.api.response.MainDeliverySubscriptionResponse
import com.chelinvest.notification.api.response.obj_param_objs.GetDeliverySubscriptionForBranchResponse
import com.chelinvest.notification.data.Repository
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import com.chelinvest.notification.model.ObjAny
import com.chelinvest.notification.model.ObjParam
import com.chelinvest.notification.ui.BaseViewModel
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.chelinvest.notification.utils.SingleLiveEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SubscrViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {

    val errorLiveEvent = SingleLiveEvent<String>()
    val deliverySubscriptionsLiveEvent = SingleLiveEvent<ArrayList<DeliveSubscriptionForBranch>>()
    val activeDeliverySubscriptionsLiveEvent = SingleLiveEvent<ArrayList<DeliveSubscriptionForBranch>>()
    val deleteDeliverySubscriptionLiveEvent = SingleLiveEvent<String>()
    val inputFieldsLiveEvent = SingleLiveEvent<ArrayList<ObjAny>>()
    val createSubscriptionLiveEvent = SingleLiveEvent<String>()
    val getFieldValuesLiveEvent = SingleLiveEvent<ArrayList<ObjParam>>()
    val loginAgainLiveEvent = SingleLiveEvent<Nothing>()

    // Признак того, что был изменен элемент списка агентов (в EditSubscrFragment)
    val editSaved = MutableLiveData<Boolean>()
    fun setEditSave(value: Boolean) {
        repository.setChangeSubscrList(value)
    }
    fun getEditSave() {
        editSaved.value = repository.getChangeSubscrList()
    }

    fun getLaunchCount(): Int {
        return repository.getLaunchCount()
    }

    // Храним значение Switch "Только активные подписки"
    val activeOnly = SingleLiveEvent<Boolean>()
    fun setActiveOnly(result: Boolean) {
        activeOnly.value = result
    }

    // Получить список подписок: get_delivery_subscription_for_branch
    fun getDeliverySubscriptionsForBranch() {
        val sessionId = repository.getSessionId()
        Log.d(LOG_TAG, "SubscrViewModel getDeliverySubscriptionsForBranch sessionId=$sessionId")

        if (sessionId == null) {
            // TODO Текущая сессия прервана. Войдите заново.
            errorLiveEvent.postValue("sessionId is null")
        } else {
            val branchShort = repository.getBranchShort() ?: return
            Log.d(LOG_TAG, "SubscrViewModel getDeliverySubscriptionsForBranch branchShort=$branchShort")

            val objParamObjsList = ArrayList<DeliveSubscriptionForBranch>()

            repository.getDeliverySubscriptionForBranch(sessionId, branchShort).enqueue(object : Callback<MainDeliverySubscriptionResponse> {
                override fun onFailure(call: Call<MainDeliverySubscriptionResponse>, t: Throwable) {
                    Log.d(LOG_TAG, "SubscrViewModel onFailure: ${t.message}")
                    handleRequestFailure(t)
                    errorLiveEvent.postValue(t.message)
                }

                override fun onResponse(call: Call<MainDeliverySubscriptionResponse>, response: Response<MainDeliverySubscriptionResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d(LOG_TAG,"SubscrViewModel getDeliverySubscriptionsForBranch onResponse: sessionId=${result?.sessionId}")
                        Log.d(LOG_TAG,"SubscrViewModel getDeliverySubscriptionsForBranch onResponse: errorNote=${result?.errorNote}")

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

    // Получить список входящих полей подписки, доступных для уведомления конкретного типа (Начало процедуры создания подписки)
    fun getInputFields() {
        val sessionId = repository.getSessionId()
        Log.d(LOG_TAG, "SubscrViewModel getInputFields sessionId=$sessionId")

        if (sessionId == null) {
            // TODO Текущая сессия прервана. Войдите заново.
            errorLiveEvent.postValue("sessionId is null")
        } else {
            val branchShort = repository.getBranchShort() ?: return

            val objAnyList = ArrayList<ObjAny>()

            //view.showProgress()
            repository.getInputFields(sessionId, branchShort).enqueue(object : Callback<MainResponse> {
                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.d(LOG_TAG, "SubscrViewModel getInputFields onFailure: ${t.message}")
                    handleRequestFailure(t)
                    errorLiveEvent.postValue(t.message)
                }

                override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d(LOG_TAG,"SubscrViewModel getInputFields onResponse: errorNote=${result?.errorNote}")

                        if (result != null) {
                            if (!result.errorNote.isNullOrEmpty()) {
                                errorLiveEvent.postValue(result.errorNote)
                            }

                            val mapper = ObjAnyResponseMapper()

                            result.elements?.forEach { element ->
                                mapper.map(element as ObjAnyResponse)?.let { element2 ->
                                    objAnyList.add(element2)
                                }
                            }

                            inputFieldsLiveEvent.postValue(objAnyList)
                        }
                    }
                }
            })
        }
    }

    // Прочитать значения для текущего поля (Процедура создания подписки)
    fun getFieldValues(idField: String, onGetFieldValue: (ArrayList<ObjParam>) -> Unit) {
        val objParamList = ArrayList<ObjParam>()
        val sessionId = repository.getSessionId()
        Log.d(LOG_TAG, "SubscrViewModel getInputFields sessionId=$sessionId")

        if (sessionId == null) {
            // TODO Текущая сессия прервана. Войдите заново.
            errorLiveEvent.postValue("sessionId is null")
        } else {
            val branchShort = repository.getBranchShort() ?: return

            //view.showProgress()
            repository.getFieldValues(sessionId, branchShort, idField).enqueue(object : Callback<MainResponse> {
                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.d(LOG_TAG, "SubscrViewModel getInputFields onFailure: ${t.message}")
                    handleRequestFailure(t)
                    errorLiveEvent.postValue(t.message)
                }

                override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d(LOG_TAG,"SubscrViewModel getInputFields onResponse: sessionId=${result?.sessionId}")
                        Log.d(LOG_TAG,"SubscrViewModel getInputFields onResponse: errorNote=${result?.errorNote}")

                        if (result != null) {
                            if (!result.errorNote.isNullOrEmpty()) {
                                errorLiveEvent.postValue(result.errorNote)
                            }

                            val mapper = ObjParamResponseMapper()

                            result.elements?.forEach { element ->
                                mapper.map(element as ObjParamResponse)?.let { element2 ->
                                    objParamList.add(element2)
                                }
                            }

                            onGetFieldValue(objParamList)
                            //getFieldValuesLiveEvent.postValue(objParamList)
                        }
                    }
                }
            })
        }
    }

    // Создать новую подписку со значениями по умолчанию -> 1.5. create_delivery_subscription_for_branch
    fun createSubscription(map: HashMap<String, ObjParam>) {
        val sessionId = repository.getSessionId()
        Log.d(LOG_TAG, "SubscrViewModel getInputFields sessionId=$sessionId")

        if (sessionId == null) {
            // TODO Текущая сессия прервана. Войдите заново.
            errorLiveEvent.postValue("sessionId is null")
        } else {
            val branchShort = repository.getBranchShort() ?: return
            //view.showProgress()
            repository.createSubscription(sessionId, branchShort, map).enqueue(object : Callback<MainDeliverySubscriptionResponse> {
                override fun onFailure(call: Call<MainDeliverySubscriptionResponse>, t: Throwable) {
                    Log.d(LOG_TAG, "SubscrViewModel createSubscription onFailure: ${t.message}")
                    handleRequestFailure(t)
                    errorLiveEvent.postValue(t.message)
                }

                override fun onResponse(call: Call<MainDeliverySubscriptionResponse>, response: Response<MainDeliverySubscriptionResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d(LOG_TAG,"SubscrViewModel createSubscription onResponse: errorNote=${result?.errorNote}")

                        if (result != null) {
                            if (!result.errorNote.isNullOrEmpty()) {
                                errorLiveEvent.postValue(result.errorNote)
                            }

                            // TODO проверить: нужно ли ещё это
                            //val res = result. replace("obj_param_objs", "obj_subscription_objs")

                            createSubscriptionLiveEvent.postValue("")
                        }
                    }
                }
            })
        }
    }

    // Выполнить 1.6. delete_delivery_subscription_for_branch
    fun delSubscript(idSubscription: String)  {
        val sessionId = repository.getSessionId()
        Log.d(LOG_TAG, "SubscrViewModel delSubscript sessionId=$sessionId")

        if (sessionId == null) {
            // TODO Текущая сессия прервана. Войдите заново.
            errorLiveEvent.postValue("sessionId is null")
        } else {
            val branchShort = repository.getBranchShort() ?: return

            repository.deleteDeliverySubscriptionForBranch(sessionId, branchShort, idSubscription).enqueue(object : Callback<MainResponse> {
                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.d(LOG_TAG, "SubscrViewModel delSubscript onFailure: ${t.message}")
                    handleRequestFailure(t)
                    errorLiveEvent.postValue(t.message)
                }

                override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d(LOG_TAG,"SubscrViewModel delSubscript onResponse: errorNote=${result?.errorNote}")

                        if (result != null) {
                            if (!result.errorNote.isNullOrEmpty()) {
                                errorLiveEvent.postValue(result.errorNote)
                            } else {
                                deleteDeliverySubscriptionLiveEvent.postValue("")
                            }
                        }
                    }
                }
            })
        }
    }

    // Выполнить команду 1.7. update_delivery_subscription_for_branch
    fun updateSubscr(subscriptionId: String,  description: String, isActive: Int) {
        val sessionId = repository.getSessionId()
        Log.d(Constants.LOG_TAG, "SubscrViewModel updateSubscr sessionId=$sessionId")

        if (sessionId == null) {
            // TODO Текущая сессия прервана. Войдите заново.
            errorLiveEvent.postValue("sessionId is null")
        } else {
            val branchShort = repository.getBranchShort() ?: return
            Log.d(Constants.LOG_TAG,"SubscrViewModel updateSubscr branchShort=$branchShort")

            val objParamObjsList = ArrayList<DeliveSubscriptionForBranch>()
            repository.updateDeliverySubscriptionForBranch(sessionId, branchShort, subscriptionId,
                description, isActive).enqueue(object : Callback<MainDeliverySubscriptionResponse> {
                override fun onFailure(call: Call<MainDeliverySubscriptionResponse>, t: Throwable) {
                    Log.d(LOG_TAG, "SubscrViewModel updateSubscr onFailure: ${t.message}")
                    errorLiveEvent.postValue(t.message)
                }

                override fun onResponse(call: Call<MainDeliverySubscriptionResponse>, response: Response<MainDeliverySubscriptionResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d(LOG_TAG,"SubscrViewModel updateSubscr onResponse: sessionId=${result?.sessionId}")
                        Log.d(LOG_TAG,"SubscrViewModel updateSubscr onResponse: errorNote=${result?.errorNote}")

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

                            activeDeliverySubscriptionsLiveEvent.postValue(objParamObjsList)
                        }
                    }
                }
            })
        }
    }

}