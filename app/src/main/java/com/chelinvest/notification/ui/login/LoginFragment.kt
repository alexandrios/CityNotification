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
import org.jetbrains.annotations.TestOnly

class LoginFragment: CustomFragment<LoginPresenter>(), ILoginView {

    override fun createPresenter(): LoginPresenter = LoginPresenter()

    var passVisible = false

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

        Log.wtf("LOGINFRAGMENT", "onViewCreated")

        vProgressLayout.visibility = View.INVISIBLE
        viewPassImageView.setColorRes(R.color.colorLightBrown)

        /*
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
        */

        loginButton.setOnClickListener {
            // Предотвращение повторного нажатия на кнопку
            loginButton.isEnabled = false
            loginPass(it)
            Handler().postDelayed({
                if (loginButton != null) {
                    loginButton.isEnabled = true
                }
            }, 2000)
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
    }

    override fun onResume() {
        super.onResume()

        userEditText.setText("")
        passEditText.setText("")
        Log.wtf("LOGINFRAGMENT", "onResume")
    }

    private fun loginPass(view: View) {
        Preferences.getInstance().saveIsTestServer(view.context, true)

        val user: String = userEditText.getText()
        val pass: String = passEditText.getText()

        hideSoftKeyboard(activity)

        // Запрос на сервер об авторизации пользователя
        getPresenter().loginByPassword(view.context, this, user, pass)
    }

    override fun onGetSessionId(session: Session) {

        // Сохранить session_id
        Preferences.getInstance().saveSessionId(this.context!!, session.session_id)
        val sessionId = Preferences.getInstance().getSessionId(this.context!!)
        Log.wtf("LOGINFRAGMENT", "sessionId=$sessionId")

        if(!session.error_note.isNullOrEmpty()) {
            showExpandableError(session.error_note.toString())
        }

        vProgressLayout.visibility = View.INVISIBLE

        if (session.session_id != null) {

            // Увеличить счетчик успешных входов в приложение
            var launchCount = Preferences.getInstance().getLaunchCount(this.context!!)
            if (launchCount < 0)
                launchCount = 0
            Preferences.getInstance().saveLaunchCount(this.context!!, launchCount + 1)

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

}


