package com.chelinvest.notification.ui

import android.content.Intent
import android.graphics.Color
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.androidadvance.topsnackbar.TSnackbar
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.color
import com.chelinvest.notification.additional.string
import com.chelinvest.notification.ui.presenter.Presenter

abstract class CustomFragment<out T: Presenter> : Fragment(), IView {

    protected abstract fun createPresenter(): T

    private var presenter: T? = null

    protected fun getPresenter(): T {
        synchronized(this) {
            if (presenter == null)
                presenter = createPresenter()
            return presenter!!
        }
    }

    private var progressDialog: MaterialDialog? = null
    private var updateSessionIdProgressDialog: MaterialDialog? = null

    override fun showHttpError(msg: String) = showExpandableError(msg)

    override fun showResponseError(msg: String) = showExpandableError(msg)

    override fun showResponseError(stringId: Int) = showExpandableError(stringId)

    override fun showInternalServerError() = showExpandableError(R.string.internal_server_error)

    override fun showNetworkError() = showExpandableError(R.string.network_error)

    override fun showUnknownError() = showExpandableError(R.string.unknown_error)

    override fun showProgressDialog() {
        context?.let { context ->
            progressDialog = MaterialDialog.Builder(context)
                .content(R.string.please_wait)
                .progress(true, 0)
                .cancelable(false)
                .show()
        }
    }

    override fun hideProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    override fun showUpdateSessionIdProgressDialog() {
        context?.let { context ->
            updateSessionIdProgressDialog = MaterialDialog.Builder(context)
                .content(R.string.please_wait_session_update)
                .progress(true, 0)
                .cancelable(false)
                .show()
        }
    }

    override fun hideUpdateSessionIdProgressDialog() {
        updateSessionIdProgressDialog?.dismiss()
        updateSessionIdProgressDialog = null
    }

    protected fun showDialogError(text: String) {
        context?.let { context ->
            MaterialDialog.Builder(context)
                .title(R.string.error)
                .content(text)
                .canceledOnTouchOutside(true)
                .positiveText(R.string.close)
                .build()
                .show()
        }
    }

    protected fun showDialogError(stringId: Int) {
        context?.let { context ->
            showDialogError(context.string(stringId))
        }
    }

    override fun showExpandableMessage(msg: String) {
        context?.let { context ->
            view?.let { view ->
                val snackbar = TSnackbar.make(view.findViewById(R.id.vParentLayout), msg, TSnackbar.LENGTH_LONG)
                snackbar.view.setBackgroundColor(context.color(R.color.goodMessage))
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(
                    Color.WHITE)
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 3
                snackbar.show()
            }
        }
    }

    override fun showExpandableError(error: String) {
        context?.let { context ->
            view?.let { view ->
                val snackbar = TSnackbar.make(view.findViewById(R.id.vParentLayout), error, TSnackbar.LENGTH_LONG)
                snackbar.view.setBackgroundColor(context.color(R.color.errorMessage))
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(Color.WHITE)
                snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 3
                snackbar.show()
            }
        }
    }

    override fun showExpandableMessage(stringId: Int) {
        context?.let { context ->
            showExpandableMessage(context.string(stringId))
        }
    }

    override fun showExpandableError(stringId: Int) {
        context?.let { context ->
            showExpandableError(context.string(stringId))
        }
    }

    override fun finishWithResult(intent: Intent, requestCode: Int) {}

}