package com.chelinvest.notification.ui.fragments.limit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.chelinvest.notification.R
import com.chelinvest.notification.databinding.FragmentLimitBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.Constants.BRANCH_NAME
import java.util.*

class LimitFragment : BaseFragment() {
    private lateinit var viewModel: LimitViewModel
    private lateinit var binding: FragmentLimitBinding

//    companion object {
//        fun create() = LimitFragment()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(Constants.LOG_TAG, "LimitFragment -> onCreate")
        viewModel = injectViewModel(viewModelFactory)

        //retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentLimitBinding.inflate(inflater, container, false).apply {
            Log.d(Constants.LOG_TAG, "LimitFragment -> onCreateView")
            viewmodel = viewModel
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(Constants.LOG_TAG, "LimitFragment -> onViewCreated")

//        binding.vBackButton.setOnClickListener { findNavController().popBackStack() }
//        binding.vAddButton.visibility = View.INVISIBLE
        //binding.branchNameTextView.text = arguments?.getString(BRANCH_NAME)

        viewModel.getAgentInfo()
        viewModel.getAgentLimit()

//        getPresenter().getAgentLimit(view.context, this) { orgName, limit ->
//            limit?.let {
//                agentTextView.text = orgName?.name ?: "Агент"
//                limitTextView.text = String.format("%,18.2f руб.", it.toDoubleOrNull() ?: "").trim() //.replace(',', ' ')
//            }
//        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(Constants.LOG_TAG, "LimitFragment -> onActivityCreated")

        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            //binding.vProgressLayout.visibility = View.INVISIBLE
            showExpandableError(it)
        })

        viewModel.agentInfoLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            binding.agentTextView.text = it?.name ?: "Агент"
        })

        viewModel.agentLimitLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            binding.limitTextView.text = String.format(Locale.ROOT, "%,18.2f руб.", it.toDoubleOrNull() ?: "")
                .trim().replace(',', ' ')
        })

//        viewModel.loginAgainLiveEvent.observeEvent(viewLifecycleOwner, Observer {
//            findNavController().navigate(R.id.action_typesFragment_to_loginFragment)
//                //null, NavOptions.Builder().setPopUpTo(R.id.typesFragment, true).build())
//        })
    }

    override fun onPause() {
        super.onPause()
        Log.d(Constants.LOG_TAG, "LimitFragment -> onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(Constants.LOG_TAG, "LimitFragment -> onDestroyView")
    }
}


