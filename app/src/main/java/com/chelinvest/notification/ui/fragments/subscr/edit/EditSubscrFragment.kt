package com.chelinvest.notification.ui.fragments.subscr.edit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.chelinvest.notification.R
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
    lateinit var oldDescription: String
    lateinit var oldActive: String

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

        oldDescription = subscrInfo.name
        oldActive = subscrInfo.value
        binding.descriptEditText.setText(subscrInfo.name)
        binding.activeSwitch.isChecked = subscrInfo.value == "Y"

        binding.vBackButton.setOnClickListener {
            Log.wtf(LOG_TAG, "EditSubscrFragment setEditSave(false)")

            if (isChanged()) {
                showSaveDialog(view.context, "text: String") {
                }
            } else {
                viewModel.setEditSave(false)
                findNavController().popBackStack()
            }
        }

//        binding.saveTextView.setOnClickListener {
//            // Выполнить команду 1.7. update_delivery_subscription_for_branch
//            viewModel.updateSubscr(subscrId, binding.descriptEditText.getText(), if (binding.activeSwitch.isChecked) 1 else  0)
//        }
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

    private fun isChanged(): Boolean {
        return !(oldDescription == binding.descriptEditText.getText() &&
                 oldActive == (if (binding.activeSwitch.isChecked) "Y" else "N"))
    }

    private fun showSaveDialog(context: Context, text: String, onPositive: (() -> Unit)) {
        MaterialDialog.Builder(context)
            .title(R.string.edit_subscr_save_question)
            .titleColor(ContextCompat.getColor(context, R.color.tomato))
            .iconRes(R.drawable.ic_warning_red_24dp)
            .content(text)
            .contentColor(ContextCompat.getColor(context, R.color.black))
            .canceledOnTouchOutside(true)
            .positiveText(R.string.yes)
            .negativeText(R.string.no)
            .neutralText(R.string.edit_subscr_continue_choice)
            .onPositive { _, _ ->
                onPositive()
            }
            .build()
            .show()
    }
}
