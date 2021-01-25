package com.chelinvest.notification.ui.fragments.page

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.chelinvest.notification.ui.fragments.branch.BranchFragment
import com.chelinvest.notification.ui.fragments.limit.LimitFragment

class PageAdapter(fm: FragmentManager, behaviour: Int): FragmentPagerAdapter(fm, behaviour) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return if (position == 0) BranchFragment() else LimitFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) "BranchFragment" else "LimitFragment"
    }
}