package com.chelinvest.notification.ui.login

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.EspressoIdlingResource
import com.chelinvest.notification.additional.hideSoftKeyboard
import com.chelinvest.notification.additional.setColorRes
import com.chelinvest.notification.model.session.Session
import com.chelinvest.notification.ui.CustomFragment
import com.chelinvest.notification.additional.logger.Logger
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.chelinvest.notification.databinding.FragmentLoginBinding
import org.jetbrains.annotations.TestOnly

class LoginFragment : BaseFragment() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

//class LoginFragment: CustomFragment<LoginPresenter>(), ILoginView {
//
//    override fun createPresenter(): LoginPresenter = LoginPresenter()

    var passVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(LOG_TAG, "LoginFragment -> onCreate")
        viewModel = injectViewModel(viewModelFactory)

        //retainInstance = true
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentLoginBinding.inflate(inflater, container, false).apply {
            Log.d(LOG_TAG, "onCreateView")
            viewmodel = viewModel
            binding = this
        }.root
    }
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.fragment_login, container, false)
//
//        Log.wtf("LOGINFRAGMENT", "onCreateView")
//
//        return view
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.wtf("LOGINFRAGMENT", "onViewCreated")

        binding.vProgressLayout.visibility = View.INVISIBLE
        binding.viewPassImageView.setColorRes(R.color.colorLightBrown)

//        // установить сохранённую настройку, с каким шлюзом соединяемся
//        val xGateType = Preferences.getInstance().getXgateType(view.context)
//        if (xGateType == "dvv") xGateNewRB.isChecked = true else xGateMobRB.isChecked = true
//
//        // С каким шлюзом соединяемся ----------------------------
//        xGateMobRB.setOnClickListener {
//            Preferences.getInstance().saveXgateType(it.context, "jev")
//        }
//
//        xGateNewRB.setOnClickListener {
//            Preferences.getInstance().saveXgateType(it.context, "dvv")
//        }
//        // --------------------------------------------------------

        binding.loginButton.setOnClickListener {
            //EspressoIdlingResource.increment()
            // Предотвращение повторного нажатия на кнопку
            binding.loginButton.isEnabled = false
                //TODO: loginPass(it)
            Handler().postDelayed({
                if (binding.loginButton != null) {
                    binding.loginButton.isEnabled = true
                    //EspressoIdlingResource.decrement()
                }
            }, 2000)
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
/*

    override fun onResume() {
        super.onResume()

        userEditText.setText("")
        passEditText.setText("")
        Log.wtf("LOGINFRAGMENT", "onResume")
    }

    fun loginPass(view: View) {
        Preferences.getInstance().saveIsTestServer(view.context, true)

        val user: String = userEditText.getText()
        val pass: String = passEditText.getText()

        hideSoftKeyboard(activity)

        // Запрос на сервер об авторизации пользователя
        getPresenter().loginByPassword(view.context, this, user, pass)
    }

    override fun onGetSessionId(session: Session) {

        // getContext can return null when fragment isn’t attached to its host
        // https://medium.com/@shafran/fragment-getcontext-vs-requirecontext-ffc9157d6bbe
        //val context: Context = this.context ?: this.requireContext()
        val context: Context = this.activity?.applicationContext ?: return

        // Сохранить session_id
        Preferences.getInstance().saveSessionId(context, session.session_id)
        val sessionId = Preferences.getInstance().getSessionId(context)
        Log.wtf("LOGINFRAGMENT", "sessionId=$sessionId")

        if(!session.error_note.isNullOrEmpty()) {
            showExpandableError(session.error_note.toString())
        }

        vProgressLayout.visibility = View.INVISIBLE

        if (session.session_id != null) {

            // Увеличить счетчик успешных входов в приложение
            var launchCount = Preferences.getInstance().getLaunchCount(context)
            if (launchCount < 0)
                launchCount = 0
            Preferences.getInstance().saveLaunchCount(context, launchCount + 1)

            Handler().postDelayed({
                findNavController().navigate(R.id.action_loginFragment_to_branchFragment)
            }, 500)

        }
    }

    override fun showProgressDialog() {
        vProgressLayout.visibility = View.VISIBLE
    }

    override fun hideProgressDialog() {
        vProgressLayout.visibility = View.INVISIBLE
    }
*/

}


