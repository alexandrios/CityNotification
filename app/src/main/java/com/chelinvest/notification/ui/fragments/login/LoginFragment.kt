package com.chelinvest.notification.ui.fragments.login

import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.hideSoftKeyboard
import com.chelinvest.notification.additional.setColorRes
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.chelinvest.notification.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    //var passVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(LOG_TAG, "LoginFragment -> onCreate")
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(LOG_TAG, "LoginFragment -> onCreateView")
        return FragmentLoginBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(LOG_TAG, "LoginFragment -> onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        binding.viewPassImageView.setColorRes(R.color.colorPrimary)

        binding.loginButton.setOnClickListener {
            // Предотвращение повторного нажатия на кнопку
            binding.loginButton.isEnabled = false

            hideSoftKeyboard(activity)
            showProgress()

            // Запрос на сервер об авторизации пользователя
            viewModel.login(binding.userEditText.text.toString(), binding.passEditText.text.toString())
        }

        // Для просмотра набранного пароля
        (binding.viewPassImageView as View).setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    binding.passEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    binding.viewPassImageView.alpha = 1.0f
                } else if (event?.action == MotionEvent.ACTION_UP) {
                    binding.passEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    binding.viewPassImageView.alpha = 0.4f
                }
                return true
            }
        })

          // Старый вариант просмотра пароля
//        binding.viewPassImageView.setOnClickListener {
//            if (!passVisible) {
//                binding.passEditText.getEditText().inputType =
//                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
//                binding.viewPassImageView.alpha = 1.0f
//            } else {
//                binding.passEditText.getEditText().inputType =
//                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
//                binding.viewPassImageView.alpha = 0.4f
//            }
//            passVisible = !passVisible
//            binding.passEditText.getEditText().setSelection(binding.passEditText.getEditText().length());
//        }

        binding.passEditText.setOnFocusChangeListener { _, b ->
            if (!b) {
                binding.viewPassImageView.alpha = 0.4f
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(LOG_TAG, "LoginFragment -> onActivityCreated")
        super.onActivityCreated(savedInstanceState)

        viewModel.sessionLiveEvent.observeEvent(viewLifecycleOwner, Observer {

            hideProgress()
            binding.loginButton.isEnabled = true

            if(!it.error_note.isNullOrEmpty()) {
                showExpandableError(it.error_note.toString())
                return@Observer
            }

            it?.let {
                Log.d(LOG_TAG, "onActivityCreated sessionLiveEvent")
                Handler().postDelayed({
                    findNavController().navigate(R.id.action_loginFragment_to_typesFragment)
                }, 500)
            }
        })

        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            hideProgress()
            binding.loginButton.isEnabled = true
            showExpandableError(it)
        })
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "LoginFragment -> onDestroy")
        super.onDestroy()
    }
}


