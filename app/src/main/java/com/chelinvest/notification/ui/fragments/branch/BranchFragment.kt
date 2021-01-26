package com.chelinvest.notification.ui.fragments.branch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chelinvest.notification.R
import com.chelinvest.notification.databinding.FragmentBranchBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.ui.fragments.subscr.SubscrFragment
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.Constants.BRANCH_ID
import com.chelinvest.notification.utils.Constants.BRANCH_NAME
import com.chelinvest.notification.utils.Constants.LOG_TAG

class BranchFragment : BaseFragment() {
    private lateinit var viewModel: BranchViewModel
    private lateinit var binding: FragmentBranchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(LOG_TAG, "BranchFragment -> onCreate")
        viewModel = injectViewModel(viewModelFactory)

        //retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentBranchBinding.inflate(inflater, container, false).apply {
            Log.d(LOG_TAG, "BranchFragment -> onCreateView")
            viewmodel = viewModel
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "BranchFragment -> onViewCreated")

        //TODO кнопка-тест, иллюстрирующая, как можно снова вызвать LoginFragment
        //buttonTest.setOnClickListener {
        //    findNavController().navigate(R.id.action_branchFragment_to_loginFragment
        //        ,null, NavOptions.Builder().setPopUpTo(R.id.branchFragment,true).build())
            // это можно настроить в дизайнере в nav_graph.xml для action_branchFragment_to_loginFragment -> Pop Behavior: PopUpTo, PopUpToInclusive
            // Это сделано для того, чтобы по кнопке назад не возвращаться по этой action
        //}

        viewModel.getDelivetypeExp()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(LOG_TAG, "BranchFragment -> onActivityCreated")

        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            showExpandableError(it)
        })

        viewModel.branchesLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            Log.d(LOG_TAG, "arrayList.Count=${it.count()}")
            if (it.count() == 0) {
                // TODO сообщение
                showExpandableError("список бранчей пуст")
                return@Observer
            }
            // связь с адаптером
            view?.findViewById<RecyclerView>(R.id.branchRecyclerView)?.apply {
                // Указать ему LayoutManager
                layoutManager = LinearLayoutManager(view?.context)
                // Передать список данных
                adapter = BranchAdapter(it) { branch ->

                    Log.d(LOG_TAG, "branchShort=${branch.value}")
                    viewModel.saveBranchShort(branch.value)

                    findNavController().navigate(R.id.action_typesFragment_to_subscrFragment,
                        SubscrFragment.getBundleArguments(branch.id, branch.name))
                }
            }
        })

//        viewModel.loginAgainLiveEvent.observeEvent(viewLifecycleOwner, Observer {
//            findNavController().navigate(R.id.action_typesFragment_to_loginFragment,
//                null, NavOptions.Builder().setPopUpTo(R.id.typesFragment, true).build())
//        })
    }

    override fun onPause() {
        super.onPause()
        Log.d(LOG_TAG, "BranchFragment -> onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(LOG_TAG, "BranchFragment -> onDestroyView")
    }
}
