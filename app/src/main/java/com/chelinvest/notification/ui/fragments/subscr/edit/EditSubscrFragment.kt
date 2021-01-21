package com.chelinvest.notification.ui.fragments.subscr.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chelinvest.notification.databinding.FragmentEditSubscrBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.ui.fragments.subscr.SubscrViewModel
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.chelinvest.notification.utils.Constants.SUBSCR_INFO

class EditSubscrFragment : BaseFragment() {
    private lateinit var viewModel: EditSubscrViewModel
    private lateinit var binding: FragmentEditSubscrBinding

    lateinit var subscrId: String

    /*
    companion object {
        fun getStartIntent(context: Context, subscrInfo: DeliveSubscriptionForBranch): Intent {
            return Intent(context, EditSubscrFragment::class.java)
                .putExtra(SUBSCR_INFO, subscrInfo)
        }
    }
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(LOG_TAG, "EditSubscrFragment -> onCreate")
        viewModel = injectViewModel(viewModelFactory)

        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentEditSubscrBinding.inflate(inflater, container, false).apply {
            Log.d(LOG_TAG, "EditSubscrFragment -> onCreateView")
            viewmodel = viewModel
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val subscrInfo =  arguments?.getSerializable(SUBSCR_INFO)
        subscrId = (subscrInfo as DeliveSubscriptionForBranch).id

        binding.descriptEditText.setText(subscrInfo.name)
        binding.activeSwitch.isChecked = subscrInfo.value == "Y"

        binding.backImageView.setOnClickListener {
            Log.wtf(LOG_TAG, "EditSubscrFragment setEditSave(false)")
            viewModel.setEditSave(false)
            findNavController().popBackStack()
        }

        binding.saveTextView.setOnClickListener {
            // Выполнить команду 1.7. update_delivery_subscription_for_branch
            viewModel.updateSubscr(subscrId, binding.descriptEditText.getText(), if (binding.activeSwitch.isChecked) 1 else  0)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            showExpandableError(it)
        })

        // Получение списка подписок
        viewModel.deliverySubscriptionsLiveEvent.observeEvent(viewLifecycleOwner, Observer { list ->
            Log.wtf(LOG_TAG, "EditSubscrFragment list.size=${list.size}")
            if (list.size > 0) {
                Log.wtf(LOG_TAG, "EditSubscrFragment setEditSave(true)")
                viewModel.setEditSave(true)
            }
            findNavController().popBackStack()
        })
    }

}
