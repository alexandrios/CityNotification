package com.chelinvest.notification.ui.fragments.types

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.chelinvest.notification.R
import com.chelinvest.notification.databinding.FragmentTypesBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.ui.fragments.branch.BranchFragment
import com.chelinvest.notification.ui.fragments.limit.LimitFragment
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.google.android.material.bottomnavigation.BottomNavigationView

class TypesFragment: BaseFragment() {
    private lateinit var viewModel: TypesViewModel
    private lateinit var binding: FragmentTypesBinding
    private var curMenuId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(LOG_TAG, "TypesFragment -> onCreate")
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return FragmentTypesBinding.inflate(inflater, container, false).apply {
            Log.d(LOG_TAG, "TypesFragment -> onCreateView")
            viewmodel = viewModel
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "TypesFragment -> onViewCreated")

        // кнопка-тест, иллюстрирующая, как можно снова вызвать LoginFragment
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


        // ViewPager using
        //binding.viewPager.adapter = TypesAdapter(childFragmentManager)
        //binding.tabLayout.setupWithViewPager(binding.viewPager)

        // ViewPager2 using
//        binding.viewPager.isUserInputEnabled = false // to disable swiping in viewpager2
//        binding.viewPager.adapter = ViewPager2Adapter(this)
//        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            when (position) {
//                0 -> {
//                    tab.text = "Branches"
//                    tab.setIcon(R.drawable.ic_create_black_24dp)
//                }
//                1 -> {
//                    tab.text = "Limits"
//                    tab.setIcon(R.drawable.ic_create_black_24dp)
//                }
//            }
//        }.attach()

        binding.bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            if (curMenuId != id) {
                curMenuId = id
                if (id == R.id.branches) {
                    loadFragment(BranchFragment(), id)
                    return@OnNavigationItemSelectedListener true
                } else if (id == R.id.limits) {
                    loadFragment(LimitFragment(), id)
                    return@OnNavigationItemSelectedListener true
                }
            }
            true
        })

        // Убрать ToolTips [не работает]
//        binding.bottomNavigation.menu.forEach {
//            TooltipCompat.setTooltipText(binding.bottomNavigation.findViewById<View>(it.itemId), null)
//        }

//        var selectedItem = viewModel.getSelectedItem()
//        if (selectedItem == 0) selectedItem = R.id.limits
//        Log.d(LOG_TAG, "TypesFragment -> selectedItem=$selectedItem")
//        binding.bottomNavigation.menu.findItem(selectedItem).isChecked = true
//        when (selectedItem) {
//            R.id.limits ->  loadFragment(LimitFragment())
//            else -> loadFragment(BranchFragment())
//        }
    }

    private fun loadFragment(fragment: Fragment, id: Int) {
        binding.titleTextView.text = when (id) {
            R.id.limits -> resources.getString(R.string.limit_subtitle_text)
            else -> resources.getString(R.string.branch_title_text)
        }
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(LOG_TAG, "TypesFragment -> onActivityCreated")
        super.onActivityCreated(savedInstanceState)

        // Получить значение последнего выбранного пункта меню
        // Это значение сохраняется в onDestroyView()
        // [отказался от этой схемы, так как LoginFragment удаляется из стека, и значение не сохраняется!]
//        var selectedItem =
//            findNavController().getBackStackEntry(R.id.typesFragment).
//            savedStateHandle.get<Int>(SELECTED_ITEM)
        var selectedItem = viewModel.getSelectedItem()
        Log.d(LOG_TAG, "TypesFragment -> selectedItem=$selectedItem")
        // Если значение пусто (первый запуск), то присвоить по умолчанию
        if (selectedItem == 0) selectedItem = R.id.limits
                Log.d(LOG_TAG, "TypesFragment -> selectedItem=$selectedItem")
        Log.d(LOG_TAG, "TypesFragment -> menu.size=${binding.bottomNavigation.menu.size()}")

        var menuItem = binding.bottomNavigation.menu.findItem(selectedItem)
        Log.d(LOG_TAG, "TypesFragment -> menuItem=$menuItem")
        if (menuItem == null) menuItem = binding.bottomNavigation.menu[0]
        Log.d(LOG_TAG, "TypesFragment -> menuItem=$menuItem")

        menuItem.isChecked = true
        selectedItem = menuItem.itemId
        curMenuId = selectedItem
        // Загрузить сооответствующий фрагмент
        when (selectedItem) {
            R.id.limits -> loadFragment(LimitFragment(), selectedItem)
            else -> loadFragment(BranchFragment(), selectedItem)
        }

        viewModel.loginAgainLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_typesFragment_to_loginFragment,
                null, NavOptions.Builder().setPopUpTo(R.id.typesFragment, true).build())
        })
    }

    override fun onPause() {
        super.onPause()
        Log.d(LOG_TAG, "TypesFragment -> onPause")
    }

    override fun onDestroyView() {
        Log.d(LOG_TAG, "TypesFragment -> onDestroyView")

        // Сохранить значение последнего выбранного пункта меню
        // [отказался от этой схемы, так как LoginFragment удаляется из стека, и значение не сохраняется!]
//        findNavController()
//            .getBackStackEntry(R.id.typesFragment)
//            .savedStateHandle
//            .set(SELECTED_ITEM, binding.bottomNavigation.selectedItemId)

        viewModel.setSelectedItem(binding.bottomNavigation.selectedItemId)
        Log.d(LOG_TAG, "TypesFragment -> save selectedItem=${binding.bottomNavigation.selectedItemId}")
        super.onDestroyView()
    }

}