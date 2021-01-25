package com.chelinvest.notification.ui.fragments.types

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.ui.fragments.branch.BranchFragment
import com.chelinvest.notification.ui.fragments.limit.LimitFragment

class ViewPager2Adapter(fragment: BaseFragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BranchFragment()
            else -> LimitFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}
