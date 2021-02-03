package com.chelinvest.notification.ui.fragments.address.edit

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
import com.chelinvest.notification.databinding.FragmentEditAddressBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.model.DeliveAddrBranch
import com.chelinvest.notification.model.DelivetypeAddrs
import com.chelinvest.notification.ui.fragments.address.edit.fragment.PushFragment
import com.chelinvest.notification.ui.fragments.address.edit.fragment.SmsFragment
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.ui.fragments.address.edit.fragment.EmailFragment
import com.chelinvest.notification.ui.custom.ModifiedEditText
import com.chelinvest.notification.utils.Constants.ADDRESS_DATA
import com.chelinvest.notification.utils.Constants.ADDRESS_FCM_TOKEN
import com.chelinvest.notification.utils.Constants.ADDRESS_MODEL
import com.chelinvest.notification.utils.Constants.APP_PUSH_ID
import com.chelinvest.notification.utils.Constants.DEFAULT_FINISH_HOUR
import com.chelinvest.notification.utils.Constants.DEFAULT_START_HOUR
import com.chelinvest.notification.utils.Constants.DEFAULT_TIME_ZONE
import com.chelinvest.notification.utils.Constants.DELIVERY_TYPE
import com.chelinvest.notification.utils.Constants.DELIVE_NAME
import com.chelinvest.notification.utils.Constants.EMAIL_ID
import com.chelinvest.notification.utils.Constants.FRAGMENT_TAG
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.chelinvest.notification.utils.Constants.SMS_ID
import com.chelinvest.notification.utils.Constants.SUBSCRIPTION
import kotlinx.android.synthetic.main.fragment_edit_address.*
import kotlinx.android.synthetic.main.fragment_edit_address.view.*

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
    private var deliveName: String? = null

    private var oldAddress: String? = null
    private var oldStartHour: String? = null
    private var oldFinishHour: String? = null
    private var oldTimeZone: String? = null

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

        // Обработка нажатия на кнопку назад
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            hideKeyboard()
            if (isChanged()) {
                showSaveDialog()
            } else {
                findNavController().popBackStack()
            }
        }
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

        binding.vBackButton.setOnClickListener {
            hideKeyboard()
            if (isChanged()) {
                showSaveDialog()
            } else {
                Log.wtf(LOG_TAG, "EditAddressFragment setEditSave(false)")
                viewModel.setEditSave(false)
                findNavController().popBackStack()
            }
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
        // наименование типа подписки
        deliveName = group?.name
        // для редактирования или добавления
        addEditType = if (addressData == null) AddEdit.ADDRESS_ADD else AddEdit.ADDRESS_EDIT

        addressData?.let {
            oldAddress = it.address
            oldStartHour = it.start_hour
            oldFinishHour = it.finish_hour
            oldTimeZone = it.timezone
        }

        // Спрятать или показать layout для периода отправления
        if (hasSendPeriod == "1") {
            // Если model==null, то предлагаем значения по умолчанию
            startHourEditText.setText(addressData?.start_hour ?: DEFAULT_START_HOUR)
            finishHourEditText.setText(addressData?.finish_hour ?: DEFAULT_FINISH_HOUR)
            timeZoneEditText.setText(addressData?.timezone ?: DEFAULT_TIME_ZONE)

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
            hideProgress()
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
                    eMailFragment.arguments = Bundle().apply {
                        putSerializable(ADDRESS_DATA, addressData)
                        putString(DELIVE_NAME, deliveName)
                    }
                    replace(R.id.emailContainer, eMailFragment, FRAGMENT_TAG)
                }
                SMS_ID -> {
                    emailContainer.visibility = View.INVISIBLE
                    smsContainer.visibility = View.VISIBLE
                    pushContainer.visibility = View.INVISIBLE
                    val smsFragment = SmsFragment.create()
                    smsFragment.arguments = Bundle().apply {
                        putSerializable(ADDRESS_DATA, addressData)
                        putString(DELIVE_NAME, deliveName)
                    }
                    replace(R.id.smsContainer, smsFragment, FRAGMENT_TAG)
                }
                APP_PUSH_ID -> {
                    emailContainer.visibility = View.INVISIBLE
                    smsContainer.visibility = View.INVISIBLE
                    pushContainer.visibility = View.VISIBLE
                    val pushFragment = PushFragment.create()
                    pushFragment.arguments = Bundle().apply {
                        putSerializable(ADDRESS_DATA, addressData)
                        putString(DELIVE_NAME, deliveName)
                        putSerializable(ADDRESS_FCM_TOKEN, viewModel.getFCMToken())
                    }
                    replace(R.id.pushContainer, pushFragment, FRAGMENT_TAG)
                }
            }
            commitNowAllowingStateLoss()
        }
    }

    private fun callOnShow(tag: String) {
        childFragmentManager.findFragmentByTag(tag)?.onStart()
    }

    // Сравнивает поля с их предыдущими значениями. True - если что-то изменилось.
    private fun isChanged(): Boolean {
        val address = view?.findViewById<ModifiedEditText>(R.id.addressEditText)?.getText() ?: ""
        var result = oldAddress != address

        if (!result && hasSendPeriod == "1") {
            result = !(oldStartHour == startHourEditText.getText() &&
                        oldFinishHour == finishHourEditText.getText() &&
                        oldTimeZone == timeZoneEditText.getText())
        }

        return result
    }

    private fun showSaveDialog() {
        val viewDialog = View.inflate(requireContext(), R.layout.dialog_edit_subscr, null)
        val dialog = MaterialDialog.Builder(requireContext())
            .canceledOnTouchOutside(true)
            .customView(viewDialog, false)
            .build()

        viewDialog.findViewById<TextView>(R.id.dialog_yes).setOnClickListener {
            dialog.dismiss()
            // TODO:
            viewModel.getTimeZone()
            setDeliveryAddressForSubscription()
        }
        viewDialog.findViewById<TextView>(R.id.dialog_no).setOnClickListener {
            dialog.dismiss()
            Log.wtf(LOG_TAG, "EditAddressFragment setEditSave(false)")
            viewModel.setEditSave(false)
            findNavController().popBackStack()
        }
        viewDialog.findViewById<TextView>(R.id.dialog_edit_continue).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
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
        val addrEditText = view?.findViewById<ModifiedEditText>(R.id.addressEditText)
        val address = addrEditText?.getText() ?: ""

        // Проверить корректность адреса
        if (viewModel.verifyAddress(deliveType!!, address)) {

            if (hasSendPeriod == "1") {
                if (!viewModel.verifyTimeRange(startHourEditText.getText(), finishHourEditText.getText(), timeZoneEditText.getText())) {
                    //TODO error message
                    return
                } else {
                    startHour = startHourEditText.getText().toIntOrNull()
                    finishHour = finishHourEditText.getText().toIntOrNull()
                    timeZone = timeZoneEditText.getText().toIntOrNull()
                }
            }

            showProgress()
            // Выполнить команду 1.8. set_delivery_address_for_subscription
            viewModel.setDeliveryAddressForSubscription(idSubscription!!, address, deliveType!!, oldAddress,
                null, startHour, finishHour, timeZone)
        }
    }

}
