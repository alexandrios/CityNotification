package com.chelinvest.notification

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    /*
    @Test
    fun isAssignedSessionId() {
        val loginFragment: LoginFragment = LoginFragment()
        val session = Session("")
        session.session_id = "123456789"
        loginFragment.onGetSessionId(session)
        val savedSessionId = Preferences.getInstance().getSessionId(loginFragment.context!!)
        assertEquals(session.session_id, savedSessionId)
    }
    */
}
