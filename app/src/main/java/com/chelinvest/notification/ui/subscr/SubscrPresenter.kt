package com.chelinvest.notification.ui.subscr

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.chelinvest.notification.Preferences
import androidx.navigation.fragment.findNavController
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.SUBSCR_INFO
import com.chelinvest.notification.additional.resolvedLaunch
import com.chelinvest.notification.interactor.*
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import com.chelinvest.notification.model.ObjAny
import com.chelinvest.notification.model.ObjParam
import com.chelinvest.notification.ui.CustomFragment
import com.chelinvest.notification.ui.address.AddressFragment
import com.chelinvest.notification.ui.presenter.Presenter

class SubscrPresenter: Presenter() {

    // Получить список входящих полей подписки, доступных для уведомления конкретного типа (Начало процедуры создания подписки)
    fun getInputFields(context: Context, view: ISubscrView, idSession: String, branchShort: String, onGetInputFields: (ArrayList<ObjAny>) -> Unit) = resolvedLaunch(block = {

        //view.showProgressDialog()
        onGetInputFields(GetInputFieldsForBranchInteractor.getInstance().loadFieldsForBranch(context, idSession, branchShort).await())
        //view.hideProgressDialog()

    }, onError = { ex ->
        //view.hideProgressDialog()
        onError(context, view, ex)
    })


    // Прочитать значения для текущего поля (Процедура создания подписки)
    fun getFieldValues(context: Context, view: ISubscrView, idSession: String, branchShort: String, idField: String, onGetFieldValue: (ArrayList<ObjParam>) -> Unit) = resolvedLaunch(block = {

        view.showProgressDialog()
        onGetFieldValue(GetFieldValuesForFieldInteractor.getInstance().loadValuesForField(context, idSession, branchShort, idField).await())
        view.hideProgressDialog()

    }, onError = { ex ->
        view.hideProgressDialog()
        onError(context, view, ex)
    })


    // Создать новую подписку со значениями по умолчанию -> 1.5. create_delivery_subscription_for_branch
    fun createSubscr(context: Context,
                     view: ISubscrView,
                     idSession: String,
                     branchShort: String,
                     map: HashMap<String, ObjParam>,
                     onCreateSubscr: (String?) -> Unit) = resolvedLaunch(block = {

        view.showProgressDialog()
        onCreateSubscr(CreateDeliverySubscriptionForBranchInteractor.getInstance().create(context, idSession, branchShort, map).await())
        view.hideProgressDialog()

    }, onError = { ex ->
        view.hideProgressDialog()
        onError(context, view, ex)
    })


    // Получить список подписок: get_delivery_subscription_for_branch
    fun getSubscript(context: Context, view: ISubscrView, onGetSubscription: (ArrayList<DeliveSubscriptionForBranch>) -> Unit) = resolvedLaunch(block = {

        val idSession = Preferences.getInstance().getSessionId(context)
        Log.wtf("idSession", "[SubscrPresenter] getSubscript() idSession=$idSession")

        val branchShort = Preferences.getInstance().getBranchShort(context) ?: return@resolvedLaunch

        if (idSession == null) {
            // вернуть пустой список
            onGetSubscription(ArrayList())
        } else {

            view.showProgressDialog()
            val response = GetDeliverySubscriptionForBranchInteractor.getInstance().loadDeliverySubscriptionForBranch(context, idSession, branchShort).await()
            view.hideProgressDialog()

            onGetSubscription(response)
        }
    }, onError = { ex ->
        view.hideProgressDialog()
        onError(context, view, ex)
    })


    // Выполнить 1.6. delete_delivery_subscription_for_branch
    fun delSubscript(context: Context, view: ISubscrView, idSubscription: String, onDelSubscription: (errorNote: String) -> Unit) = resolvedLaunch(block = {

        val idSession = Preferences.getInstance().getSessionId(context)
        Log.wtf("idSession", "[SubscrPresenter] idSession=$idSession")

        val branchShort = Preferences.getInstance().getBranchShort(context) ?: return@resolvedLaunch

        if (idSession == null) {
            // вернуть пустой список
            onDelSubscription("Сессия пустая")
        } else {

            view.showProgressDialog()
            val response = DeleteDeliverySubscriptionForBranchInteractor.getInstance().delete(context, idSession, branchShort, idSubscription).await()
            view.hideProgressDialog()

            onDelSubscription(response ?: "" )
        }
    }, onError = { ex ->
        view.hideProgressDialog()
        onError(context, view, ex)
    })


    // Перейти в настройку адресов конкретного агента
    fun moveToTypesForSubscription (view: ISubscrView, idSubscription: String, nameSubscription: String) {
        val bundle = AddressFragment.getBundleArguments(idSubscription, nameSubscription)
        findNavController(view as CustomFragment<*>).navigate(R.id.action_subscrFragment_to_addressFragment, bundle)
    }


    // Редактирование подписки (описание, активность)
    fun editSubscription(view: ISubscrView, subscrInfo: DeliveSubscriptionForBranch) {
        val bundle = Bundle()
        bundle.putSerializable(SUBSCR_INFO, subscrInfo)
        findNavController(view as CustomFragment<*>).navigate(R.id.action_subscrFragment_to_editSubscrFragment, bundle)
    }

}