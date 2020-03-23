package com.chelinvest.notification.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.androidadvance.topsnackbar.TSnackbar
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.color
import com.chelinvest.notification.additional.gradientColor
import com.chelinvest.notification.additional.string
import com.chelinvest.notification.ui.presenter.Presenter

abstract class CustomActivity<out T: Presenter> : AppCompatActivity(), IView {

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
        progressDialog = MaterialDialog.Builder(this)
            .content(R.string.please_wait)
            .progress(true, 0)
            .cancelable(false)
            //.backgroundColor(resources.getColor(R.color.colorPrimary))
            .show()
    }

    override fun hideProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    override fun showUpdateSessionIdProgressDialog() {
        updateSessionIdProgressDialog = MaterialDialog.Builder(this)
            .content(R.string.please_wait_session_update)
            .progress(true, 0)
            .cancelable(false)
            .show()
    }

    override fun hideUpdateSessionIdProgressDialog() {
        updateSessionIdProgressDialog?.dismiss()
        updateSessionIdProgressDialog = null
    }

    protected fun showDialogError(text: String, onPositive: (() -> Unit)? = null) {
        MaterialDialog.Builder(this)
            .title(R.string.error)
            .content(text)
            .canceledOnTouchOutside(true)
            .positiveText(R.string.close)
            .onPositive { _, _ ->
                onPositive?.let { it() }
            }
            .build()
            .show()
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }

    protected fun showDialogError(stringId: Int, onPositive: (() -> Unit)? = null) = showDialogError(string(stringId), onPositive)


    override fun showExpandableMessage(msg: String) {
        val layOut = findViewById<View>(R.id.vParentLayout)
        if (layOut == null) {
            val text = "Не найден элемент 'vParentLayout'\n" + msg
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
            return
        }
        val snackbar = TSnackbar.make(layOut, msg, TSnackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(color(R.color.goodMessage))
        snackbar.view.findViewById<TextView>(com.androidadvance.topsnackbar.R.id.snackbar_text).setTextColor(
            Color.WHITE)
        snackbar.show()
    }

    override fun showExpandableError(error: String) {
        val layOut = findViewById<View>(R.id.vParentLayout)
        if (layOut == null) {
            val text = "Не найден элемент 'vParentLayout'\n" + error
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
            return
        }
        val snackbar = TSnackbar.make(layOut, error, TSnackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(color(R.color.errorMessage))
        snackbar.view.findViewById<TextView>(com.androidadvance.topsnackbar.R.id.snackbar_text).setTextColor(Color.WHITE)
        snackbar.show()
    }

    override fun showExpandableMessage(stringId: Int) {
        showExpandableMessage(string(stringId))
    }

    override fun showExpandableError(stringId: Int) {
        showExpandableError(string(stringId))
    }

    override fun finishWithResult(intent: Intent, requestCode: Int) {
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    protected fun setStatusBarColor(colorInd: Int) {
        val color = if (colorInd < 0 || colorInd > 4) color(R.color.tangelo) else gradientColor(colorInd)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.statusBarColor = color
    }

}
