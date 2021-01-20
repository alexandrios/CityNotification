package com.chelinvest.notification.ui.fragments.address

import android.content.Context
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.additional.resolvedLaunch
import com.chelinvest.notification.interactor.GetDeliveryTypesForSubscriptionInteractor
import com.chelinvest.notification.model.DelivetypeAddrs
import com.chelinvest.notification.ui.presenter.Presenter


class AddressPresenter : Presenter() {

    // Получить список адресов, привязанных к подписке
    fun getDelivetypeAddrs(context: Context, idSubscription: String, view: IAddressView, onGetTypesForSubscription: (ArrayList<DelivetypeAddrs>) -> Unit) = resolvedLaunch(block = {

        val idSession = Preferences.getInstance().getSessionId(context) ?: return@resolvedLaunch
        val branchShort = Preferences.getInstance().getBranchShort(context) ?: return@resolvedLaunch

        view.showProgressDialog()
        val response = GetDeliveryTypesForSubscriptionInteractor.getInstance().loadDeliveryTypesForSubscription(context, idSession, branchShort, idSubscription).await()
        view.hideProgressDialog()

        onGetTypesForSubscription(response)

    }, onError = { ex ->
        view.hideProgressDialog()
        onError(context, view, ex)
    })

}