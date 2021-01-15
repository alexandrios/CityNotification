package com.chelinvest.notification.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.chelinvest.notification.utils.Constants
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
}