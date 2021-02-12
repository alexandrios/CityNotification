package com.chelinvest.notification

import android.view.View
import com.chelinvest.notification.ui.fragments.login.LoginFragment
import com.chelinvest.notification.ui.fragments.subscr.SubscrFragment
import kotlinx.android.synthetic.main.fragment_login.*
import org.junit.Assert.*
import org.junit.Test

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

        val loginFragment = LoginFragment()
        assertFalse("Must be false", loginFragment.passEditText.visibility == View.VISIBLE)

        val subscrFragment = SubscrFragment()
        assertTrue("Must be 0", subscrFragment.index == 0)
        assertTrue("Must be java.util.HashMap",
            subscrFragment.map::class.qualifiedName == "java.util.HashMap")

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
