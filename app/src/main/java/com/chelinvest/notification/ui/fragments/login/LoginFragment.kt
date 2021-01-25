package com.chelinvest.notification.ui.fragments.login

import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
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

    var passVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(LOG_TAG, "LoginFragment -> onCreate")
        viewModel = injectViewModel(viewModelFactory)

        // Не удалять фрагмент (onDestroy) при пересоздании активити.
        // Важно для сохранения состояния экрана при повороте устройства
        //retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentLoginBinding.inflate(inflater, container, false).apply {
            Log.d(LOG_TAG, "LoginFragment -> onCreateView")
            viewmodel = viewModel
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "LoginFragment -> onViewCreated")

        binding.vProgressLayout.visibility = View.INVISIBLE
        binding.viewPassImageView.setColorRes(R.color.colorLightBrown)

        binding.loginButton.setOnClickListener {
            // Предотвращение повторного нажатия на кнопку
            binding.loginButton.isEnabled = false

            hideSoftKeyboard(activity)
            binding.vProgressLayout.visibility = View.VISIBLE

            // Запрос на сервер об авторизации пользователя
            viewModel.login(binding.userEditText.getText(), binding.passEditText.getText())
            //viewModel.login("pam", "ceramica1")
            //viewModel.login()
            //viewModel.loginByPassword("pam", "ceramica1")
        }

        binding.viewPassImageView.setOnClickListener {
            if (!passVisible) {
                binding.passEditText.getEditText().inputType =
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.viewPassImageView.alpha = 1.0f
            } else {
                binding.passEditText.getEditText().inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.viewPassImageView.alpha = 0.4f
            }
            passVisible = !passVisible
            binding.passEditText.getEditText().setSelection(binding.passEditText.getEditText().length());
        }

        binding.passEditText.setOnFocusChangeListener { _, b ->
            if (!b) {
                binding.viewPassImageView.alpha = 0.4f
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(LOG_TAG, "LoginFragment -> onActivityCreated")

        viewModel.sessionLiveEvent.observeEvent(viewLifecycleOwner, Observer {

            binding.vProgressLayout.visibility = View.INVISIBLE
            binding.loginButton.isEnabled = true

            if(!it.error_note.isNullOrEmpty()) {
                showExpandableError(it.error_note.toString())
                return@Observer
            }

            it?.let {
                Log.d(LOG_TAG, "onActivityCreated sessionLiveEvent")
                Handler().postDelayed({
                    //findNavController().navigate(R.id.action_loginFragment_to_branchFragment)
                    findNavController().navigate(R.id.action_loginFragment_to_typesFragment)
                }, 500)
            }
        })

        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            binding.vProgressLayout.visibility = View.INVISIBLE
            binding.loginButton.isEnabled = true
            showExpandableError(it)
        })
    }


    /*
        override fun onResume() {
            super.onResume()

            userEditText.setText("")
            passEditText.setText("")
            Log.d("LOGINFRAGMENT", "onResume")
        }

        override fun showProgressDialog() {
            vProgressLayout.visibility = View.VISIBLE
        }

        override fun hideProgressDialog() {
            vProgressLayout.visibility = View.INVISIBLE
        }
    */

}


