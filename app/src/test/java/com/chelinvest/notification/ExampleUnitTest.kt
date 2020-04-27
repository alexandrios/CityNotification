package com.chelinvest.notification

import androidx.test.core.app.ApplicationProvider
import com.chelinvest.notification.model.session.Session
import com.chelinvest.notification.ui.login.LoginFragment
import com.chelinvest.notification.ui.login.LoginPresenter
import com.chelinvest.notification.ui.main.MainActivity
import io.grpc.Context
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
/*import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner*/


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

/*    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }*/

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        val loginFragment: LoginFragment = LoginFragment()
        assertFalse("Must be false", loginFragment.passVisible)
    }


/*
    // Проверка сохранения сессии в Preference и получения из него
    @Test
    fun isAssignedSessionId() {
        val loginFragment: LoginFragment = LoginFragment()
        val session = Session("")
        session.session_id = "799015700838348"
        loginFragment.onGetSessionId(session)
        val savedSessionId = Preferences.getInstance()
            .getSessionId(ApplicationProvider.getApplicationContext())
        assertEquals(session.session_id, savedSessionId)
    }
*/

}
