package com.chelinvest.notification.ui.main

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.chelinvest.notification.R

open class TestActivityScreen: Screen<TestActivityScreen>() {
    val vLoginLayout: KView = KView { withId(R.id.vLoginLayout) }
    val userEditText: KEditText = KEditText { withId(R.id.userEditText) }
    val passEditText: KEditText = KEditText { withId(R.id.passEditText) }
    val loginButton: KButton = KButton { withId(R.id.loginButton) }
    //val branchRV: KRecyclerView = KRecyclerView { withId(R.id.branchRecyclerView) }
}