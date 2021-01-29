package com.chelinvest.notification.ui

import android.content.Intent

interface IView {
    fun showHttpError(msg: String)
    fun showResponseError(msg: String)
    fun showResponseError(stringId: Int)
    fun showInternalServerError()
    fun showNetworkError()
    fun showUnknownError()
    fun showProgress()
    fun hideProgress()
    fun showUpdateSessionIdProgressDialog()
    fun hideUpdateSessionIdProgressDialog()
    fun showExpandableMessage(msg: String)
    fun showExpandableError(error: String)
    fun showExpandableMessage(stringId: Int)
    fun showExpandableError(stringId: Int)
    fun finishWithResult(intent: Intent, requestCode: Int)
}