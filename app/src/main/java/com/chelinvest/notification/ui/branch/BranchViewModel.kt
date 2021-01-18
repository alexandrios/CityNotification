package com.chelinvest.notification.ui.branch

import android.app.Application
import android.util.Log
import com.chelinvest.notification.additional.resolvedLaunch
import com.chelinvest.notification.data.Repository
import com.chelinvest.notification.interactor.GetDeliveryBranchInteractor
import com.chelinvest.notification.model.ObjParam
import com.chelinvest.notification.model.session.Session
import com.chelinvest.notification.ui.BaseViewModel
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.SingleLiveEvent
import javax.inject.Inject

class BranchViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : BaseViewModel(application) {

    val errorLiveEvent = SingleLiveEvent<String>()
    val sessionLiveEvent = SingleLiveEvent<Session>()

    fun getDelivetypeExp(onGetBranch: (ArrayList<ObjParam>) -> Unit) = resolvedLaunch(block = {

        val sessionId = repository.getSessionId()
        Log.d(Constants.LOG_TAG, "[BranchPresenter] sessionId=$sessionId")

        if (sessionId == null) {
            // вернуть пустой список
            //onGetBranch(ArrayList())
            errorLiveEvent.postValue("sessionId is null")
        } else {
            //view.showProgressDialog()
            //val response = GetDeliveryBranchInteractor.getInstance().loadDeliveryBranches(context, sessionId).await()
            //view.hideProgressDialog()
            //onGetBranch(response)
            errorLiveEvent.postValue(sessionId)
        }
    }, onError = { ex ->
        //view.hideProgressDialog()
        errorLiveEvent.postValue(ex.message)
        //onError(context, view, ex)
    })
}