package com.chelinvest.notification.ui.fragments.types

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chelinvest.notification.R
import com.chelinvest.notification.databinding.FragmentTypesBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.utils.Constants
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener


class TypesFragment: BaseFragment() {
    private lateinit var viewModel: TypesViewModel
    private lateinit var binding: FragmentTypesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(Constants.LOG_TAG, "PageFragment -> onCreate")
        viewModel = injectViewModel(viewModelFactory)

        //retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return FragmentTypesBinding.inflate(inflater, container, false).apply {
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


//        val tab1 = binding.tabLayout.newTab()
//        tab1.text = "Лимиты"
//        tab1.setIcon(R.drawable.common_full_open_on_phone)
//        binding.tabLayout.addTab(tab1)
//
//        val tab2 = binding.tabLayout.newTab()
//        tab2.text = "Бранчи"
//        tab2.setIcon(R.drawable.common_full_open_on_phone)
//        binding.tabLayout.addTab(tab2)
//
//        val adapter = TypesAdapter(requireActivity().supportFragmentManager)
//        binding.viewPager.adapter = adapter
//        // addOnPageChangeListener event change the tab on slide
//        binding.viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding.tabLayout))

        // TODO хорошо ли это?
        binding.viewPager.adapter = TypesAdapter(requireActivity().supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(Constants.LOG_TAG, "PageFragment -> onActivityCreated")

//        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, Observer {
//            showExpandableError(it)
//        })

    }

    override fun onPause() {
        super.onPause()
        Log.d(Constants.LOG_TAG, "PageFragment -> onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(Constants.LOG_TAG, "PageFragment -> onDestroyView")
    }
}