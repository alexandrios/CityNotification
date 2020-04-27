package com.chelinvest.notification.ui.branch

import android.content.Context
import android.util.Log
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.additional.EspressoIdlingResource
import com.chelinvest.notification.additional.resolvedLaunch
import com.chelinvest.notification.interactor.GetDeliveryBranchInteractor
import com.chelinvest.notification.model.ObjParam
import com.chelinvest.notification.ui.presenter.Presenter

class BranchPresenter : Presenter() {

    fun getDelivetypeExp(context: Context, view: IBranchView, onGetBranch: (ArrayList<ObjParam>) -> Unit) = resolvedLaunch(block = {

        val session_id = Preferences.getInstance().getSessionId(context)
        Log.wtf("session_id", "[BranchPresenter] session_id=" + session_id)

        if (session_id == null) {
            // вернуть пустой список
            onGetBranch(ArrayList())
        } else {

            view.showProgressDialog()
            val response = GetDeliveryBranchInteractor.getInstance().loadDeliveryBranches(context, session_id).await()
            view.hideProgressDialog()

            onGetBranch(response)
        }
    }, onError = { ex ->
        view.hideProgressDialog()
        onError(context, view, ex)
    })

}