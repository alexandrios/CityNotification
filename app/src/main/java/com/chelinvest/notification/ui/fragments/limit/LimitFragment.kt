package com.chelinvest.notification.ui.fragments.limit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chelinvest.notification.databinding.FragmentLimitBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.utils.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class LimitFragment : BaseFragment() {
    private lateinit var viewModel: LimitViewModel
    private lateinit var binding: FragmentLimitBinding

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

        showProgress()
        GlobalScope.launch {
            viewModel.getAgentInfo()
        }
        //viewModel.getAgentLimit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(Constants.LOG_TAG, "LimitFragment -> onActivityCreated")

        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, {
            hideProgress()
            showExpandableError(it)
        })

        viewModel.agentInfoLiveEvent.observeEvent(viewLifecycleOwner, {
            binding.agentTextView.text = it?.name ?: "Агент"
        })

        viewModel.agentLimitLiveEvent.observeEvent(viewLifecycleOwner, {
            hideProgress()
            binding.limitTextView.text = String.format(Locale.US, "%,18.2f руб.", it.toDoubleOrNull() ?: "")
                .trim().replace(',', ' ')
        })
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