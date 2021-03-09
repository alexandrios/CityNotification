package com.chelinvest.notification.ui.fragments.address.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.chelinvest.notification.R
import com.chelinvest.notification.databinding.FragmentEditAddressBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.model.DeliveAddrBranch
import com.chelinvest.notification.model.DelivetypeAddrs
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.ui.fragments.address.edit.fragment.EmailFragment
import com.chelinvest.notification.ui.fragments.address.edit.fragment.PushFragment
import com.chelinvest.notification.ui.fragments.address.edit.fragment.SmsFragment
import com.chelinvest.notification.utils.Constants.ADDRESS_DATA
import com.chelinvest.notification.utils.Constants.ADDRESS_FCM_TOKEN
import com.chelinvest.notification.utils.Constants.ADDRESS_MODEL
import com.chelinvest.notification.utils.Constants.ADDRESS_TEXT
import com.chelinvest.notification.utils.Constants.APP_PUSH_ID
import com.chelinvest.notification.utils.Constants.DEFAULT_FINISH_HOUR
import com.chelinvest.notification.utils.Constants.DEFAULT_START_HOUR
import com.chelinvest.notification.utils.Constants.DEFAULT_TIME_ZONE
import com.chelinvest.notification.utils.Constants.DEFAULT_TIME_ZONE_INT
import com.chelinvest.notification.utils.Constants.DELIVERY_TYPE
import com.chelinvest.notification.utils.Constants.DELIVE_NAME
import com.chelinvest.notification.utils.Constants.EDIT_TEXT
import com.chelinvest.notification.utils.Constants.EMAIL_ID
import com.chelinvest.notification.utils.Constants.FRAGMENT_TAG
import com.chelinvest.notification.utils.Constants.HOUR_FINISH
import com.chelinvest.notification.utils.Constants.HOUR_START
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.chelinvest.notification.utils.Constants.SMS_ID
import com.chelinvest.notification.utils.Constants.SUBSCRIPTION
import com.google.android.material.slider.RangeSlider

class EditAddressFragment : BaseFragment() {
        private lateinit var viewModel: EditAddressViewModel
        private lateinit var binding: FragmentEditAddressBinding

    enum class AddEdit {
        ADDRESS_ADD,
        ADDRESS_EDIT
    }

    private var group: DelivetypeAddrs? = null
    private var addressData: DeliveAddrBranch? = null
    private var idSubscription: String? = null
    private var hasSendPeriod: String? = null
    private var deliveType: String? = null
    private var deliveName: String? = null

    private var oldAddress: String? = null
    private var oldStartHour: String? = null
    private var oldFinishHour: String? = null
    private var oldTimeZone: String? = null
    private var newTimeZone: String? = null

    private var startHour: Int? = null
    private var finishHour: Int? = null
    private var timeZone: Int? = null

    private var addEditType: AddEdit? = null

    companion object {
        // Вариант передачи параметров во фрагмент
        fun getBundleArguments(
            idSubscription: String,
            group: DelivetypeAddrs,
            model: DeliveAddrBranch?
        ): Bundle {
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
        //retainInstance = true

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEditAddressBinding.inflate(inflater, container, false).apply {
            Log.d(LOG_TAG, "EditAddressFragment -> onCreateView")
            viewmodel = viewModel
            binding = this
        }.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ADDRESS_TEXT, view?.findViewById<EditText>(R.id.addressEditText)?.text.toString())
        //outState.putString(HOUR_START, binding.startHourEditText.text.toString())
        //outState.putString(HOUR_FINISH, binding.finishHourEditText.text.toString())
        outState.putString(HOUR_START, binding.timeRangeSlider.values[0].toString())
        outState.putString(HOUR_FINISH, binding.timeRangeSlider.values[0].toString())

        Log.d(LOG_TAG, "EditAddressFragment -> onSaveInstanceState: $outState")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(LOG_TAG, "EditAddressFragment -> onViewCreated")
        Log.d(LOG_TAG, "EditAddressFragment -> arguments: $arguments")
        super.onViewCreated(view, savedInstanceState)

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
        Log.d(LOG_TAG, "EditAddressFragment -> addressData: ${addressData?.address} ${addressData?.start_hour} ${addressData?.finish_hour}")

        // указывать ли для подписки период отправления
        hasSendPeriod = group?.has_send_period
        // тип подписки (Email, SMS, Push)
        deliveType = group?.id
        // наименование типа подписки
        deliveName = group?.name
        // для редактирования или добавления
        addEditType = if (addressData == null) AddEdit.ADDRESS_ADD else AddEdit.ADDRESS_EDIT
        if (addEditType == AddEdit.ADDRESS_ADD)
            binding.titleTextView.text = resources.getString(R.string.create_notification_title_text)
        else
            binding.titleTextView.text = resources.getString(R.string.edit_notification_title_text)

        addressData?.let {
            oldAddress = it.address
            oldStartHour = it.start_hour
            oldFinishHour = it.finish_hour
            oldTimeZone = it.timezone
            newTimeZone = oldTimeZone
        }

        // Спрятать или показать layout для периода отправления
        if (hasSendPeriod == "1") {
            // Проверить, есть ли сохранённые данные перед пересозданием фрагмента
            Log.d(LOG_TAG, "EditAddressFragment -> onViewCreated: savedInstanceState = $savedInstanceState")
            val sHStart: String
            val sHFinish: String
            if (savedInstanceState != null) {
                sHStart = savedInstanceState.getString(HOUR_START) ?: (addressData?.start_hour ?: DEFAULT_START_HOUR)
                sHFinish = savedInstanceState.getString(HOUR_FINISH) ?: (addressData?.finish_hour ?: DEFAULT_FINISH_HOUR)
                //binding.startHourEditText.setText(savedInstanceState.getString(HOUR_START) ?: (addressData?.start_hour ?: DEFAULT_START_HOUR))
                //binding.finishHourEditText.setText(savedInstanceState.getString(HOUR_FINISH) ?: (addressData?.finish_hour ?: DEFAULT_FINISH_HOUR))
            } else {
                // Если model==null, то предлагаем значения по умолчанию
                sHStart = addressData?.start_hour ?: DEFAULT_START_HOUR
                sHFinish = addressData?.finish_hour ?: DEFAULT_FINISH_HOUR
                //binding.startHourEditText.setText(addressData?.start_hour ?: DEFAULT_START_HOUR)
                //binding.finishHourEditText.setText(addressData?.finish_hour ?: DEFAULT_FINISH_HOUR)
            }
            binding.timeRangeSlider.setValues(sHStart.toFloat(), sHFinish.toFloat())
            setPeriodTextView(binding.timeRangeSlider.values)
            //Log.d(LOG_TAG, "startHourEditText = ${binding.startHourEditText.text}")
            //Log.d(LOG_TAG, "finishHourEditText = ${binding.finishHourEditText.text}")

            binding.periodLayout.visibility = View.VISIBLE

            //binding.startHourEditText.doOnTextChanged { text, _, _, _ ->
            //    viewModel.checkHourRange(text.toString())
            //}

            //binding.finishHourEditText.doOnTextChanged { text, _, _, _ ->
            //    viewModel.checkHourRange(text.toString())
            //}

            binding.timeRangeSlider.addOnChangeListener { slider, value, fromUser ->
                Log.d(LOG_TAG, "timeRangeSlider: value=$value, fromUser=$fromUser, start=${slider.values[0]}, end = ${slider.values[1]}")
                setPeriodTextView(slider.values)
            }

            // Spinner for TimeZone
            val timeZonesMap = viewModel.getTimeZone()
//            val adapter = ArrayAdapter(
//                requireContext(),
//                android.R.layout.simple_spinner_item,
//                timeZonesMap.keys.toList()
//            )
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            val adapter = ArrayAdapter(
                requireContext(), R.layout.spinner_row, R.id.timeZoneTextView, timeZonesMap.keys.toList()
            )

            with(binding.timeZoneSpinner) {
                this.adapter = adapter
                // заголовок диалога списка
                //prompt = getString(R.string.edit_dialog_timezone_title)
                //setPromptId(R.string.edit_dialog_timezone_title)
                // установить указатель списка на нужный элемент
                setSelection(
                    viewModel.getTimeZonePosition(
                        timeZonesMap,
                        addressData?.timezone?.toIntOrNull() ?: DEFAULT_TIME_ZONE_INT
                    )
                )
                // listener при выборе элемента списка
                onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        // position - позиция нажатого элемента
                        val key = timeZonesMap.keys.toList()[position]
                        val value = timeZonesMap[key]
                        val timeZoneHour: Int = value?.div(3600000) ?: DEFAULT_TIME_ZONE_INT

                        // Сохранить предпочтение по часовому поясу
                        viewModel.setPreferTimeZoneMap(timeZoneHour, key)

                        // Запомнить текущий выбранный часовой пояс
                        newTimeZone = timeZoneHour.toString()
                        // Проверить выбранный часовой пояс
                        viewModel.checkTimeZone(timeZoneHour.toString())
                    }

                    override fun onNothingSelected(arg0: AdapterView<*>?) {}
                }
            }

        } else {
            binding.periodLayout.visibility = View.INVISIBLE
        }

        createFragment(savedInstanceState)
        callOnShow(FRAGMENT_TAG)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, {
            hideProgress()
            showExpandableError(it)
        })

        viewModel.setDeliveAddressLiveEvent.observeEvent(viewLifecycleOwner, {
            if (it != "1") {
                showExpandableError(it)
            } else {
                viewModel.setEditSave(true)
                findNavController().popBackStack()
            }
        })
    }

    private fun setPeriodTextView(values: List<Float>) {
        binding.periodTextView.text = String.format("%s: %s %d %s %d %s",
            resources.getString(R.string.edit_period_slider_descr_settings),
            resources.getString(R.string.edit_period_from),
            values[0].toInt(),
            resources.getString(R.string.edit_period_to),
            values[1].toInt(),
            resources.getString(R.string.edit_period_hours))
    }

    private fun createFragment(savedInstanceState: Bundle?) {
        childFragmentManager.beginTransaction().apply {
            when (deliveType) {
                EMAIL_ID -> {
                    binding.emailContainer.visibility = View.VISIBLE
                    binding.smsContainer.visibility = View.INVISIBLE
                    binding.pushContainer.visibility = View.INVISIBLE
                    val eMailFragment = EmailFragment.create()
                    eMailFragment.arguments = Bundle().apply {
                        putSerializable(ADDRESS_DATA, addressData)
                        putString(DELIVE_NAME, deliveName)
                        if (savedInstanceState != null) {
                            putString(EDIT_TEXT, savedInstanceState.getString(ADDRESS_TEXT))
                        }
                    }
                    replace(R.id.emailContainer, eMailFragment, FRAGMENT_TAG)
                }
                SMS_ID -> {
                    binding.emailContainer.visibility = View.INVISIBLE
                    binding.smsContainer.visibility = View.VISIBLE
                    binding.pushContainer.visibility = View.INVISIBLE
                    val smsFragment = SmsFragment.create()
                    smsFragment.arguments = Bundle().apply {
                        putSerializable(ADDRESS_DATA, addressData)
                        putString(DELIVE_NAME, deliveName)
                        if (savedInstanceState != null) {
                            putString(EDIT_TEXT, savedInstanceState.getString(ADDRESS_TEXT))
                        }
                    }
                    replace(R.id.smsContainer, smsFragment, FRAGMENT_TAG)
                }
                APP_PUSH_ID -> {
                    binding.emailContainer.visibility = View.INVISIBLE
                    binding.smsContainer.visibility = View.INVISIBLE
                    binding.pushContainer.visibility = View.VISIBLE
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
        val address = requireView().findViewById<EditText>(R.id.addressEditText)?.text.toString()
        var result = oldAddress != address

//        if (!result && hasSendPeriod == "1") {
//            result = !(oldStartHour == binding.startHourEditText.text.toString() &&
//                        oldFinishHour == binding.finishHourEditText.text.toString() &&
//                        oldTimeZone == timeZoneString())
//        }
        if (!result && hasSendPeriod == "1") {
            result = !(oldStartHour?.toInt() == binding.timeRangeSlider.values[0].toInt() &&
                    oldFinishHour?.toInt() == binding.timeRangeSlider.values[1].toInt() &&
                    oldTimeZone == timeZoneString())
        }

        return result
    }

    private fun timeZoneString(): String {
        return newTimeZone ?: DEFAULT_TIME_ZONE
    }

    private fun showSaveDialog() {
        val viewDialog = View.inflate(requireContext(), R.layout.dialog_edit_subscr, null)
        val dialog = MaterialDialog.Builder(requireContext())
            .canceledOnTouchOutside(true)
            .customView(viewDialog, false)
            .build()

        viewDialog.findViewById<TextView>(R.id.dialog_yes).setOnClickListener {
            dialog.dismiss()
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

    // Создать (или привязать существующий) адрес (email, sms, push) к подписке
    private fun setDeliveryAddressForSubscription() {
        val addrEditText = view?.findViewById<EditText>(R.id.addressEditText)
        val address = addrEditText?.text.toString()

        // Проверить корректность адреса
        if (viewModel.verifyAddress(deliveType!!, address)) {
            if (hasSendPeriod == "1") {
                //if (viewModel.checkHourRange(startHourEditText.text.toString()))
                if (!viewModel.verifyTimeRange(
                        //binding.startHourEditText.text.toString(),
                        //binding.finishHourEditText.text.toString(),
                        timeZoneString()
                    )) {
                    return
                } else {
                    //startHour = binding.startHourEditText.text.toString().toIntOrNull()
                    //finishHour = binding.finishHourEditText.text.toString().toIntOrNull()
                    startHour = binding.timeRangeSlider.values[0].toInt()
                    finishHour = binding.timeRangeSlider.values[1].toInt()
                    timeZone = timeZoneString().toIntOrNull()
                }
            }

            showProgress()
            // Выполнить команду 1.8. set_delivery_address_for_subscription
            viewModel.setDeliveryAddressForSubscription(
                idSubscription!!, address, deliveType!!, oldAddress,
                null, startHour, finishHour, timeZone
            )
        }
    }
}
