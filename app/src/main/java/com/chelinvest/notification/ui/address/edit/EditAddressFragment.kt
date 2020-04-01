package com.chelinvest.notification.ui.address.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.*
import com.chelinvest.notification.model.DeliveAddrBranch
import com.chelinvest.notification.model.DelivetypeAddrs
import com.chelinvest.notification.presentation.screens.address.edit.EditAddressPresenter
import com.chelinvest.notification.presentation.screens.address.edit.fragment.PushFragment
import com.chelinvest.notification.presentation.screens.address.edit.fragment.SmsFragment
import com.chelinvest.notification.ui.CustomFragment
import com.chelinvest.notification.ui.address.AddressViewModel
import com.chelinvest.notification.ui.address.IAddressView
import com.chelinvest.notification.ui.address.edit.fragment.EmailFragment
import com.chelinvest.notification.ui.custom.ModifiedEditText
import kotlinx.android.synthetic.main.fragment_edit_address.*
import kotlinx.android.synthetic.main.fragment_edit_address.vBackButton


class EditAddressFragment : CustomFragment<EditAddressPresenter>(), IAddressView {

    enum class AddEdit {
        add, edit
    }

    private val model: AddressViewModel by activityViewModels()

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
        /*
        fun getStartIntent(context: Context, idSubscription: String, group: DelivetypeAddrs, model: DeliveAddrBranch?): Intent {
            return Intent(context, EditAddressActivity::class.java)
                .putExtra(SUBSCRIPTION, idSubscription)
                .putExtra(DELIVERY_TYPE, group)
                .putExtra(ADDRESS_MODEL, model)
        }
        */
        // Вариант передачи параметров во фрагмент
        fun getBundleArguments(idSubscription: String, group: DelivetypeAddrs, model: DeliveAddrBranch?): Bundle {
            return Bundle().apply {
                this.putString(SUBSCRIPTION, idSubscription)
                this.putSerializable(DELIVERY_TYPE, group)
                this.putSerializable(ADDRESS_MODEL, model)
            }
        }
    }

    override fun createPresenter() = EditAddressPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Не удалять фрагмент (onDestroy) при пересоздании активити.
        // Важно для сохранения состояния экрана при повороте устройства
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vBackButton.setOnClickListener { findNavController().popBackStack() }

        saveTextView.setOnClickListener {
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
        addEditType = if (addressData == null) AddEdit.add else AddEdit.edit

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

    fun checkHourRange(text: String?): Unit? {
        val hour = text?.toIntOrNull()
        hour?.let {
            if (it < 0 || it > 23) {
                showExpandableError("Необходимо указать значение в диапазоне от 0 до 23")
            }
        }
        return null
    }

    fun checkTimeZone(text: String?): Unit? {
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

        var address: String = ""
        //if (deliveType != PUSH) {
            val addrEditText = view?.findViewById<ModifiedEditText>(R.id.addressEditText)
            address = addrEditText?.getText() ?: ""
        //}

        // Проверить корректность адреса
        if (getPresenter().verifyAddress(view?.context!!, this, deliveType!!, address)) {

            if (hasSendPeriod == "1") {
                if (!getPresenter().verifyTimeRange(view?.context!!, this,
                    startHourEditText.getText(), finishHourEditText.getText(), timeZoneEditText.getText())) {
                    return
                } else {
                    startHour = startHourEditText.getText().toIntOrNull()
                    finishHour = finishHourEditText.getText().toIntOrNull()
                    timeZone = timeZoneEditText.getText().toIntOrNull()
                }
            }

            // Выполнить команду 1.8. set_delivery_address_for_subscription
            getPresenter().setDeliveryAddressForSubscription(view?.context!!,this,
                idSubscription!!, address, deliveType!!, oldAddress, null,
                startHour, finishHour, timeZone) {

                if (it != "1") {
                    showExpandableError(it)
                    //Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                }
                else {
                    model.setEditSave(true)
                    findNavController().popBackStack()
                    //val intent = Intent()
                    //intent.putExtra("SAVED", "YES")
                    //setResult(RESULT_OK, intent)
                    //finish()
                }
            }
        }
    }


}
