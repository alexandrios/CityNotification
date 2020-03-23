package com.chelinvest.notification.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chelinvest.notification.R
import com.chelinvest.notification.ui.CustomFragment

class MenuFragment: CustomFragment<MenuPresenter>(), IMenuView {

    override fun createPresenter(): MenuPresenter = MenuPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_menu, container, false)
}
