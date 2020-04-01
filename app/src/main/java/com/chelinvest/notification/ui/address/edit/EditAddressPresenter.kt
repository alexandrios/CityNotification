package com.chelinvest.notification.presentation.screens.address.edit

import android.content.Context
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.resolvedLaunch
import com.chelinvest.notification.interactor.SetDeliveryAddressInteractor
import com.chelinvest.notification.additional.*
import com.chelinvest.notification.ui.address.IAddressView
import com.chelinvest.notification.ui.presenter.Presenter


class EditAddressPresenter : Presenter() {

    fun verifyAddress(context: Context, view: IAddressView, type: String, address: String): Boolean {

        when (type) {
            EMAIL_ID -> {
                if (!isEmailValid(address)) {
                //if (!address.contains("@")) {
                    //Toast.makeText(context,"Необходимо указать правильный Email", Toast.LENGTH_LONG).show()
                    view.showExpandableError(context.resources.getString(R.string.edit_email_verify))
                    return false
                }
            }
            SMS_ID -> {
                if (!isPhoneValid(address) || !address.contains("+7")) {
                    //Toast.makeText(context,"Необходимо указать правильный номер телефона", Toast.LENGTH_LONG).show()
                    view.showExpandableError(context.resources.getString(R.string.edit_sms_verify))
                    return false
                }
            }
            APP_PUSH_ID -> {
                if (address.isEmpty()) {
                    view.showExpandableError(context.resources.getString(R.string.edit_push_verify))
                    return false
                }
            }
        }

        return true
    }

    fun verifyTimeRange(context: Context, view: IAddressView, startHour: String, finishHour: String, timeZone: String): Boolean {

        val startH = startHour.toIntOrNull()
        val finishH = finishHour.toIntOrNull()
        val timeZ = timeZone.toIntOrNull()

        if (startH == null) {
            view.showExpandableError(context.resources.getString(R.string.edit_start_hour_descr_settings))
            return false
        }

        if (finishH == null) {
            view.showExpandableError(context.resources.getString(R.string.edit_finish_hour_descr_settings))
            return false
        }

        if (timeZ == null) {
            view.showExpandableError(context.resources.getString(R.string.edit_time_zone_descr_settings))
            return false
        }

        if (startH >= finishH) {
            view.showExpandableError(context.resources.getString(R.string.edit_time_start_more_then_finish_hour))
            return false
        }

        return true
    }

    // Создать (или привязать существующий) адрес (email, sms, push) к подписке
    fun setDeliveryAddressForSubscription(context: Context,
                                          view: IAddressView,
                                          idSubscription: String,
                                          address: String,
                                          idDelivetype: String,
                                          oldAddress: String?,
                                          isConfirm: String?,
                                          startHour: Int?,
                                          finishHour: Int?,
                                          timeZone: Int?,
                                          onSetAddress: (String) -> Unit) = resolvedLaunch(block = {

        val idSession = Preferences.getInstance().getSessionId(context) ?: return@resolvedLaunch
        val branchShort = Preferences.getInstance().getBranchShort(context) ?: return@resolvedLaunch

        view.showProgressDialog()
        // Выполнить команду 1.8. set_delivery_address_for_subscription
        val result = SetDeliveryAddressInteractor.getInstance().setAddress(
            context, idSession, branchShort, idSubscription, address, idDelivetype, oldAddress, isConfirm, startHour, finishHour, timeZone).await()
        view.hideProgressDialog()

        onSetAddress(result)

    }, onError = { ex ->
        view.hideProgressDialog()
        onError(context, view, ex)
    })
}