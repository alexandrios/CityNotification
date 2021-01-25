package com.chelinvest.notification.ui.fragments.page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chelinvest.notification.R
import com.chelinvest.notification.databinding.FragmentBranchBinding
import com.chelinvest.notification.databinding.FragmentPageBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.ui.fragments.branch.BranchAdapter
import com.chelinvest.notification.ui.fragments.branch.BranchViewModel
import com.chelinvest.notification.ui.fragments.subscr.SubscrFragment
import com.chelinvest.notification.utils.Constants
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_page.*

class PageFragment: BaseFragment() {
    private lateinit var viewModel: PageViewModel
    private lateinit var binding: FragmentPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(Constants.LOG_TAG, "PageFragment -> onCreate")
        viewModel = injectViewModel(viewModelFactory)

        //retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentPageBinding.inflate(inflater, container, false).apply {
            Log.d(Constants.LOG_TAG, "PageFragment -> onCreateView")
            viewmodel = viewModel
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(Constants.LOG_TAG, "PageFragment -> onViewCreated")

        //TODO кнопка-тест, иллюстрирующая, как можно снова вызвать LoginFragment
        //buttonTest.setOnClickListener {
        //    findNavController().navigate(R.id.action_branchFragment_to_loginFragment
        //        ,null, NavOptions.Builder().setPopUpTo(R.id.branchFragment,true).build())
        // это можно настроить в дизайнере в nav_graph.xml для action_branchFragment_to_loginFragment -> Pop Behavior: PopUpTo, PopUpToInclusive
        // Это сделано для того, чтобы по кнопке назад не возвращаться по этой action
        //}

//        binding.tabLayout.apply {
//            addTab(TabLayout.Tab().setText("Лимиты"))
//            addTab(TabLayout.Tab().setText("Бранчи"))
//        }

        // TODO хорошо ли это?
        binding.viewPager.adapter = PageAdapter(requireActivity().supportFragmentManager, 1)

        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(Constants.LOG_TAG, "PageFragment -> onActivityCreated")

//        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, Observer {
//            showExpandableError(it)
//        })

    }
}