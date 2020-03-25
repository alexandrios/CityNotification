package com.chelinvest.notification.ui.login

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
import com.chelinvest.notification.additional.hideSoftKeyboard
import com.chelinvest.notification.additional.setColorRes
import com.chelinvest.notification.model.session.Session
import com.chelinvest.notification.ui.CustomFragment
import kotlinx.android.synthetic.main.change_xgate_fragment.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_menu.loginButton
import com.chelinvest.notification.additional.logger.Logger

class LoginFragment: CustomFragment<LoginPresenter>(), ILoginView {

    override fun createPresenter(): LoginPresenter = LoginPresenter()

    var passVisible = false
    var user: String? = null
    var pass: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.wtf("LOGINFRAGMENT", "onCreate")
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        Log.wtf("LOGINFRAGMENT", "onCreateView")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.wtf("LOGINFRAGMENT", "onViewCreated start")

        vProgressLayout.visibility = View.INVISIBLE
        viewPassImageView.setColorRes(R.color.colorLightBrown)

        // установить сохранённую настройку, с каким шлюзом соединяемся
        val xGateType = Preferences.getInstance().getXgateType(view.context)
        if (xGateType == "dvv") xGateNewRB.isChecked = true else xGateMobRB.isChecked = true

        // С каким шлюзом соединяемся ----------------------------
        xGateMobRB.setOnClickListener {
            Preferences.getInstance().saveXgateType(it.context, "jev")
        }

        xGateNewRB.setOnClickListener {
            Preferences.getInstance().saveXgateType(it.context, "dvv")
        }
        // --------------------------------------------------------

        loginButton.setOnClickListener {
            loginPass(it)
        }

        viewPassImageView.setOnClickListener {
            if (!passVisible) {
                passEditText.getEditText().inputType =
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                viewPassImageView.alpha = 1.0f
            } else {
                passEditText.getEditText().inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                viewPassImageView.alpha = 0.4f
            }
            passVisible = !passVisible
            passEditText.getEditText().setSelection(passEditText.getEditText().length());
        }

        passEditText.setOnFocusChangeListener { _, b ->
            if (!b) {
                viewPassImageView.alpha = 0.4f
            }
        }

        Log.wtf("LOGINFRAGMENT", "onViewCreated end")
    }

    override fun onResume() {
        super.onResume()

        userEditText.setText(user ?: "")
        passEditText.setText(pass ?: "")
        Log.wtf("LOGINFRAGMENT", "onResume")
    }

    override fun onPause() {
        super.onPause()

        user = userEditText.getText()
        pass = passEditText.getText()
        Log.wtf("LOGINFRAGMENT", "onPause")
    }

    // Запрос на сервер об авторизации пользователя
    private fun loginPass(view: View) {
        Preferences.getInstance().saveIsTestServer(view.context, true)

        val user: String = "pam" // userEditText.getText()
        val pass: String = "ceramica1" // passEditText.getText()

        hideSoftKeyboard(activity)

        getPresenter().loginByPassword(view.context, this, user, pass)
    }

    override fun onGetSessionId(session: Session) {

        Logger.Log(session.toString())

        // Сохранить session_id
        Preferences.getInstance().saveSessionId(activity?.baseContext!!, session.session_id)
        val sessionId = Preferences.getInstance().getSessionId(activity?.baseContext!!)
        Log.wtf("sessionId", "[LOGINFRAGMENT] sessionId=$sessionId")

        if(!session.error_note.isNullOrEmpty()) {
            // TODO: вывести session.error_note
            showExpandableError(session.error_note.toString())
        }

        vProgressLayout.visibility = View.INVISIBLE

        if (session.session_id != null) {

            // Увеличить счетчик успешных входов в приложение
            var launchCount = Preferences.getInstance().getLaunchCount(activity?.baseContext!!)
            if (launchCount < 0)
                launchCount = 0
            Preferences.getInstance().saveLaunchCount(activity?.baseContext!!, launchCount + 1)

            Handler().postDelayed({
                //saveTryAndFinish()
                findNavController().navigate(R.id.action_loginFragment_to_branchFragment)
            }, 500)

        }
    }

    /*
    override fun saveTryAndFinish() {
        // Записать флаг, что была попытка авторизации
        Preferences.getInstance().saveTryLogin(activity?.baseContext!!, true)
        //activity?.finish()
        findNavController().navigate(R.id.action_loginFragment_to_menuFragment)
    }
    */


    override fun showProgressDialog() {
        vProgressLayout.visibility = View.VISIBLE
    }

    override fun hideProgressDialog() {
        vProgressLayout.visibility = View.INVISIBLE
    }

}


