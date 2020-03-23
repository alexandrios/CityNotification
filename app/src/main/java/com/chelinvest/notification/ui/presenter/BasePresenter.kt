package com.chelinvest.notification.ui.presenter

import android.content.Context
import com.chelinvest.notification.ui.IView
import com.chelinvest.notification.exception.*
import javax.xml.stream.XMLStreamException

abstract class BasePresenter {

    open fun onError(context: Context, view: IView, exception: Exception) {

        when (exception) {
            is HttpException -> view.showHttpError(exception.errorMessage)
            is ResponseException -> view.showResponseError(exception.message)
            is XMLStreamException -> view.showResponseError(exception.message ?: "XMLStreamException")
            is InternalServerException -> view.showInternalServerError()
            is NetworkException -> view.showNetworkError()
            else -> view.showUnknownError()
        }

    }
}