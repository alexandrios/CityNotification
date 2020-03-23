package com.chelinvest.notification.ui.limit

import android.content.Context
import android.util.Log
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.additional.resolvedLaunch
import com.chelinvest.notification.interactor.CheckAgentLimitInteractor
import com.chelinvest.notification.interactor.GetAgentInfoInteractor
import com.chelinvest.notification.model.OrgName
import com.chelinvest.notification.ui.presenter.Presenter

class LimitPresenter : Presenter() {

    fun getAgentLimit(context: Context, view: ILimitView, onGetLimit: (OrgName?, String?) -> Unit) = resolvedLaunch(block = {

        val session_id = Preferences.getInstance().getSessionId(context)
        Log.wtf("session_id", "[LimitPresenter.getAgentLimit] session_id=" + session_id)

        if (session_id == null) {
            onGetLimit(null, null)
        } else {
            view.showProgressDialog()
            val responseInfo = GetAgentInfoInteractor.getInstance().loadAgentInfo(context, session_id).await()
            val response = CheckAgentLimitInteractor.getInstance().loadAgentLimit(context, session_id).await()
            view.hideProgressDialog()

            onGetLimit(responseInfo, response)
        }
    }, onError = { ex ->
        view.hideProgressDialog()
        onError(context, view, ex)
    })

}
