package com.chelinvest.notification.ui.branch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.R
import com.chelinvest.notification.databinding.FragmentBranchBinding
import com.chelinvest.notification.databinding.FragmentLoginBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.model.ObjParam
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.ui.CustomFragment
import com.chelinvest.notification.ui.login.LoginViewModel
import com.chelinvest.notification.ui.subscr.SubscrFragment
import com.chelinvest.notification.utils.Constants.BRANCH_ID
import com.chelinvest.notification.utils.Constants.BRANCH_NAME
import com.chelinvest.notification.utils.Constants.LIMIT_VALUE
import com.chelinvest.notification.utils.Constants.LOG_TAG


//class BranchFragment : CustomFragment<BranchPresenter>(), IBranchView {
class BranchFragment : BaseFragment() {
    private lateinit var viewModel: BranchViewModel
    private lateinit var binding: FragmentBranchBinding

    //override fun createPresenter(): BranchPresenter = BranchPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(LOG_TAG, "BranchFragment -> onCreate")
        viewModel = injectViewModel(viewModelFactory)

        //retainInstance = true
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
//            inflater.inflate(R.layout.fragment_branch, container, false)

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


        viewModel.getDelivetypeExp {

        }
/*

        view.context?.let {
            // запрос доступных бранчей
            getPresenter().getDelivetypeExp(it, this) { arrayList ->
                // Добавить в список бранчей пункт "Просмотр остатка лимита по своему агенту"
                arrayList.add(ObjParam("0", "Просмотр остатка лимита по своему агенту", LIMIT_VALUE))

                //EspressoIdlingResource.decrement()
                // связь с адаптером
                view.findViewById<RecyclerView>(R.id.branchRecyclerView)?.apply {
                    // Указать ему LayoutManager
                    layoutManager = LinearLayoutManager(view.context)
                    // Передать список данных
                    adapter = BranchAdapter(arrayList) { branch ->

                        Log.d(LOG_TAG, "branchShort=${branch.value}")
                        Preferences.getInstance().saveBranchShort(it, branch.value)

                        val bundle = Bundle()
                        bundle.putString(BRANCH_ID, branch.id)
                        bundle.putString(BRANCH_NAME, branch.name)
                        when (branch.value) {
                            LIMIT_VALUE -> {
                                findNavController().navigate(R.id.action_branchFragment_to_limitFragment, bundle)
                            }
                            else -> {
                                //findNavController().navigate(R.id.action_branchFragment_to_subscrFragment, bundle)
                                // Вариант передачи параметров во фрагмент с использованием статического метода
                                findNavController().navigate(R.id.action_branchFragment_to_subscrFragment,
                                    SubscrFragment.getBundleArguments(branch.id, branch.name))
                            }
                        }
                    }
                }
            }
        }
 */

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(LOG_TAG, "BranchFragment -> onActivityCreated")

        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            showExpandableError(it)
        })
    }

}
