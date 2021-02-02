package com.chelinvest.notification.ui

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.color
import com.chelinvest.notification.utils.Constants
//import com.androidadvance.topsnackbar.TSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment(), RequestListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onRequestFailure(message: String, checkOffline: Boolean) {
        Log.d(Constants.LOG_TAG, "BaseFragment onRequestFailure $message")
    }

    protected fun showKeyboard(et: EditText) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }

    protected fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken,0)
    }

    fun showExpandableMessage(msg: String) {
        context?.let { context ->
            view?.let { view ->
                val snackBar = Snackbar.make(view.findViewById(R.id.vParentLayout), msg, Snackbar.LENGTH_LONG)
                snackBar.view.setBackgroundColor(context.color(R.color.goodMessage))
                snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(
                    Color.WHITE)
                snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 3
                snackBar.show()
            }
        }
    }

    fun showExpandableError(error: String) {
        context?.let { context ->
            view?.let { view ->
                val snackBar = Snackbar.make(view.findViewById(R.id.vParentLayout), error, Snackbar.LENGTH_LONG)
                snackBar.view.setBackgroundColor(context.color(R.color.errorMessage))
                snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(
                    Color.WHITE)
                snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 3
                snackBar.show()
            }
        }
    }

    fun showProgress() {
        requireView().findViewById<View>(R.id.vProgressLayout)?.let {
            Log.d(Constants.LOG_TAG, "showProgress")
            it.visibility = View.VISIBLE
        }
    }

    fun hideProgress() {
        requireView().findViewById<View>(R.id.vProgressLayout)?.let {
            Log.d(Constants.LOG_TAG, "hideProgress")
            it.visibility = View.INVISIBLE
        }
    }
}