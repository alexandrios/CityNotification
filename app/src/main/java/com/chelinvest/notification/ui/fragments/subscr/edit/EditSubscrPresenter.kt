package com.chelinvest.notification.ui.fragments.subscr.edit

import android.content.Context
import android.util.Log
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.additional.resolvedLaunch
import com.chelinvest.notification.interactor.UpdateDeliverySubscriptionForBranchInteractor
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import com.chelinvest.notification.ui.presenter.Presenter
import com.chelinvest.notification.ui.fragments.subscr.ISubscrView
import kotlinx.android.synthetic.main.fragment_edit_subscr.*

class EditSubscrPresenter : Presenter() {

    // Выполнить команду 1.7. update_delivery_subscription_for_branch
    fun updateSubscr(context: Context, view: ISubscrView, onUpdateSubscription: (ArrayList<DeliveSubscriptionForBranch>) -> Unit) = resolvedLaunch(block = {
        val activity = (view as EditSubscrFragment)
        val idSubscr = activity.subscrId

        val description = activity.descriptEditText.getText()
        val isActive = if (activity.activeSwitch.isChecked) 1 else  0
        val idSession = Preferences.getInstance().getSessionId(context)
        Log.wtf("idSession", "[EditSubscrPresenter] idSession=" + idSession)
        val branchShort = Preferences.getInstance().getBranchShort(context) ?: return@resolvedLaunch

        if (idSession == null) {
            // вернуть пустой список
            onUpdateSubscription(ArrayList())
        } else {

            view.showProgressDialog()
            val response = UpdateDeliverySubscriptionForBranchInteractor.getInstance().update(
                context, idSession, branchShort, idSubscr, description, isActive).await()
            view.hideProgressDialog()

            onUpdateSubscription(response)
        }
    }, onError = { ex ->
        view.hideProgressDialog()
        onError(context, view, ex)
    })

}