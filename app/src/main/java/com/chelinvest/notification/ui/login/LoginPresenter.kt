package com.chelinvest.notification.ui.login

import android.content.Context
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.resolvedLaunch
import com.chelinvest.notification.interactor.LoginInteractor
import com.chelinvest.notification.ui.presenter.Presenter

// Возможные типы возвращаемых данных метода get_agent_info
enum class ResultTypeAgentInfo {
    X_ORG_ID, X_ORG_NAME, X_ORG_INN, X_ORG_CONTACT
}

class LoginPresenter : Presenter() {

    fun loginByPassword(context: Context, view: ILoginView, user: String, pass: String) =
        resolvedLaunch(block = {

            if (user.isEmpty()) {
                view.showExpandableMessage(context.getString(R.string.login_check_input_user))
                return@resolvedLaunch
            }

            if (pass.isEmpty()) {
                view.showExpandableMessage(context.getString(R.string.login_check_input_pass))
                return@resolvedLaunch
            }

            view.showProgressDialog()
            val session =
                LoginInteractor.getInstance().loginByPasswordAsync(context, user, pass).await()
            view.hideProgressDialog()

            view.onGetSessionId(session)

        }, onError = { ex ->
            view.hideProgressDialog()
            onError(context, view, ex)
        })
}
