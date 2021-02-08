package com.chelinvest.notification.ui.fragments.subscr.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.chelinvest.notification.R
import com.chelinvest.notification.databinding.FragmentEditSubscrBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import com.chelinvest.notification.ui.BaseFragment
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

        // Обработка нажатия на кнопку назад
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            hideKeyboard()
            if (isChanged()) {
                showSaveDialog()
            } else {
                findNavController().popBackStack()
            }
        }

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
        binding.descriptEditText.setText(subscrInfo.name) //; binding.descrEditText.setText(subscrInfo.name)
        binding.activeSwitch.isChecked = subscrInfo.value == "Y"

        binding.vBackButton.setOnClickListener {
            if (isChanged()) {
                showSaveDialog()
            } else {
                Log.wtf(LOG_TAG, "EditSubscrFragment setEditSave(false)")
                hideKeyboard()
                viewModel.setEditSave(false)
                findNavController().popBackStack()
            }
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

    private fun isCorrectDescription(): Boolean = binding.descriptEditText.getText().isNotBlank()

    // Сравнивает поля с их предыдущими значениями. True - если что-то изменилось.
    private fun isChanged(): Boolean {
        return !(oldDescription == binding.descriptEditText.text.toString() &&
                 oldActive == (if (binding.activeSwitch.isChecked) "Y" else "N"))
    }

    private fun showSaveDialog() {
        val viewDialog = View.inflate(requireContext(), R.layout.dialog_edit_subscr, null)
        val dialog = MaterialDialog.Builder(requireContext())
            .canceledOnTouchOutside(true)
            .customView(viewDialog, false)
            .build()

        viewDialog.findViewById<TextView>(R.id.dialog_yes).setOnClickListener {
            dialog.dismiss()
            if (!isCorrectDescription()) {
                showExpandableError(resources.getString(R.string.empty_description_error))
            } else {
                showProgress()
                // Выполнить команду 1.7. update_delivery_subscription_for_branch
                viewModel.updateSubscr(subscrId,
                    binding.descriptEditText.text.toString(),
                    if (binding.activeSwitch.isChecked) 1 else 0)
            }
        }
        viewDialog.findViewById<TextView>(R.id.dialog_no).setOnClickListener {
            dialog.dismiss()
            Log.wtf(LOG_TAG, "EditSubscrFragment setEditSave(false)")
            viewModel.setEditSave(false)
            findNavController().popBackStack()
        }
        viewDialog.findViewById<TextView>(R.id.dialog_edit_continue).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}
