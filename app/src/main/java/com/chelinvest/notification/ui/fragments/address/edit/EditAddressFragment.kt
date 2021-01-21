package com.chelinvest.notification.ui.fragments.address.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chelinvest.notification.R
import com.chelinvest.notification.databinding.FragmentEditAddressBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.model.DeliveAddrBranch
import com.chelinvest.notification.model.DelivetypeAddrs
import com.chelinvest.notification.presentation.screens.address.edit.fragment.PushFragment
import com.chelinvest.notification.presentation.screens.address.edit.fragment.SmsFragment
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.ui.fragments.address.edit.fragment.EmailFragment
import com.chelinvest.notification.ui.custom.ModifiedEditText
import com.chelinvest.notification.utils.Constants.ADDRESS_DATA
import com.chelinvest.notification.utils.Constants.ADDRESS_MODEL
import com.chelinvest.notification.utils.Constants.APP_PUSH_ID
import com.chelinvest.notification.utils.Constants.DELIVERY_TYPE
import com.chelinvest.notification.utils.Constants.EMAIL_ID
import com.chelinvest.notification.utils.Constants.FRAGMENT_TAG
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.chelinvest.notification.utils.Constants.SMS_ID
import com.chelinvest.notification.utils.Constants.SUBSCRIPTION
import kotlinx.android.synthetic.main.fragment_edit_address.*

class EditAddressFragment : BaseFragment() {
        private lateinit var viewModel: EditAddressViewModel
        private lateinit var binding: FragmentEditAddressBinding

    enum class AddEdit {
        ADDRESS_ADD,
        ADDRESS_EDIT
    }

    var group: DelivetypeAddrs? = null
    var addressData: DeliveAddrBranch? = null
    private var idSubscription: String? = null
    private var hasSendPeriod: String? = null
    private var deliveType: String? = null
    private var oldAddress: String? = null
    private var startHour: Int? = null
    private var finishHour: Int? = null
    private var timeZone: Int? = null

    var addEditType: AddEdit? = null

    companion object {
        // Вариант передачи параметров во фрагмент
        fun getBundleArguments(idSubscription: String, group: DelivetypeAddrs, model: DeliveAddrBranch?): Bundle {
            return Bundle().apply {
                this.putString(SUBSCRIPTION, idSubscription)
                this.putSerializable(DELIVERY_TYPE, group)
                this.putSerializable(ADDRESS_MODEL, model)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(LOG_TAG, "EditAddressFragment -> onCreate")
        viewModel = injectViewModel(viewModelFactory)

        // Не удалять фрагмент (onDestroy) при пересоздании активити.
        // Важно для сохранения состояния экрана при повороте устройства
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return FragmentEditAddressBinding.inflate(inflater, container, false).apply {
            Log.d(LOG_TAG, "EditAddressFragment -> onCreateView")
            viewmodel = viewModel
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "EditAddressFragment -> onViewCreated")

        binding.vBackButton.setOnClickListener { findNavController().popBackStack() }

        binding.saveTextView.setOnClickListener {
            setDeliveryAddressForSubscription()
        }

        idSubscription = arguments?.getString(SUBSCRIPTION)
        // информация о типе уведомления
        group = arguments?.getSerializable(DELIVERY_TYPE) as DelivetypeAddrs
        // информация о адресе подписки или null
        addressData = arguments?.getSerializable(ADDRESS_MODEL) as DeliveAddrBranch?

        // указывать ли для подписки период отправления
        hasSendPeriod = group?.has_send_period
        // тип подписки (Email, SMS, Push)
        deliveType = group?.id
        // вызвана activity для редактирования или добавления
        addEditType = if (addressData == null) AddEdit.ADDRESS_ADD else AddEdit.ADDRESS_EDIT

        if (addressData != null) {
            oldAddress = addressData!!.address
        }

        // Спрятать или показать layout для периода отправления
        if (hasSendPeriod == "1") {

            // Если model==null, то предлагаем значения по умолчанию
            startHourEditText.setText(addressData?.start_hour ?: "9")
            finishHourEditText.setText(addressData?.finish_hour ?: "22")
            timeZoneEditText.setText(addressData?.timezone ?: "5")

            periodLayout.visibility = View.VISIBLE

            startHourEditText.onTextChanged = { text ->
                checkHourRange(text)
            }

            finishHourEditText.onTextChanged = { text ->
                checkHourRange(text)
            }

            timeZoneEditText.onTextChanged = {
                checkTimeZone(it)
            }
        } else {
            periodLayout.visibility = View.INVISIBLE
        }

        createFragment()
        callOnShow(FRAGMENT_TAG)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            showExpandableError(it)
        })

        viewModel.setDeliveAddressLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            if (it != "1") {
                showExpandableError(it)
            }
            else {
                viewModel.setEditSave(true)
                findNavController().popBackStack()
            }
        })
    }

    private fun createFragment() {
        childFragmentManager.beginTransaction().apply {
            when (deliveType) {
                EMAIL_ID -> {
                    emailContainer.visibility = View.VISIBLE
                    smsContainer.visibility = View.INVISIBLE
                    pushContainer.visibility = View.INVISIBLE
                    val eMailFragment = EmailFragment.create()
                    eMailFragment.arguments = Bundle().apply { putSerializable(ADDRESS_DATA, addressData) }
                    replace(R.id.emailContainer, eMailFragment, FRAGMENT_TAG)
                }
                SMS_ID -> {
                    emailContainer.visibility = View.INVISIBLE
                    smsContainer.visibility = View.VISIBLE
                    pushContainer.visibility = View.INVISIBLE
                    val smsFragment = SmsFragment.create()
                    smsFragment.arguments = Bundle().apply { putSerializable(ADDRESS_DATA, addressData) }
                    replace(R.id.smsContainer, smsFragment, FRAGMENT_TAG)
                }
                APP_PUSH_ID -> {
                    emailContainer.visibility = View.INVISIBLE
                    smsContainer.visibility = View.INVISIBLE
                    pushContainer.visibility = View.VISIBLE
                    val pushFragment = PushFragment.create()
                    pushFragment.arguments = Bundle().apply { putSerializable(ADDRESS_DATA, addressData) }
                    replace(R.id.pushContainer, pushFragment, FRAGMENT_TAG)
                }
            }
            commitNowAllowingStateLoss()
        }
    }


    private fun callOnShow(tag: String) {
        childFragmentManager.findFragmentByTag(tag)?.onStart()
    }

    private fun checkHourRange(text: String?): Unit? {
        val hour = text?.toIntOrNull()
        hour?.let {
            if (it < 0 || it > 23) {
                showExpandableError("Необходимо указать значение в диапазоне от 0 до 23")
            }
        }
        return null
    }

    private fun checkTimeZone(text: String?): Unit? {
        val hour = text?.toIntOrNull()
        hour?.let {
            if (it < -11 || it > 12) {
                showExpandableError("Необходимо указать значение в диапазоне от -11 до 12")
            }
        }
        return null
    }

    // Создать (или привязать существующий) адрес (email, sms, push) к подписке
    private fun setDeliveryAddressForSubscription() {

        var address = ""
        val addrEditText = view?.findViewById<ModifiedEditText>(R.id.addressEditText)
        address = addrEditText?.getText() ?: ""

        // Проверить корректность адреса
        if (viewModel.verifyAddress(deliveType!!, address)) {

            if (hasSendPeriod == "1") {
                if (!viewModel.verifyTimeRange(startHourEditText.getText(), finishHourEditText.getText(), timeZoneEditText.getText())) {
                    return
                } else {
                    startHour = startHourEditText.getText().toIntOrNull()
                    finishHour = finishHourEditText.getText().toIntOrNull()
                    timeZone = timeZoneEditText.getText().toIntOrNull()
                }
            }

            // Выполнить команду 1.8. set_delivery_address_for_subscription
            viewModel.setDeliveryAddressForSubscription(idSubscription!!, address, deliveType!!, oldAddress,
                null, startHour, finishHour, timeZone)
        }
    }

}
