package com.chelinvest.notification.ui.login

import com.chelinvest.notification.model.session.Session
import com.chelinvest.notification.ui.IView

interface ILoginView : IView {
    fun onGetSessionId(session: Session)
}