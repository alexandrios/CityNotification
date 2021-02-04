package com.chelinvest.notification.ui.fragments.address

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager
import kotlinx.android.synthetic.main.dialog_field_values.view.*
import com.chelinvest.notification.R
import com.chelinvest.notification.databinding.FragmentAddressBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.model.DelivetypeAddrs
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.ui.fragments.address.dialog.DelivetypeSubscrAdapter
import com.chelinvest.notification.ui.fragments.address.edit.EditAddressFragment
import com.chelinvest.notification.utils.Constants.APP_PUSH
import com.chelinvest.notification.utils.Constants.LOG_TAG
import com.chelinvest.notification.utils.Constants.SUBSCRIPTION_ID
import com.chelinvest.notification.utils.Constants.SUBSCRIPTION_NAME

class AddressFragment : BaseFragment() {
    private lateinit var viewModel: AddressViewModel
    private lateinit var binding: FragmentAddressBinding
    private var groupList: List<DelivetypeAddrs> = emptyList()
    private var delivetypeAddrsList = ArrayList<DelivetypeAddrs>()
    private lateinit var idSubscription: String
    private lateinit var nameSubscription: String
    private var mRecyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: AddressAdapter? = null
    private var mWrappedAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    private var recyclerViewExpandableItemManager: RecyclerViewExpandableItemManager? = null

    companion object {
        // Вариант передачи параметров во фрагмент
        fun getBundleArguments(idSubscription: String, nameSubscription: String): Bundle {
            return Bundle().apply {
                this.putString(SUBSCRIPTION_ID, idSubscription)
                this.putString(SUBSCRIPTION_NAME, nameSubscription)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(LOG_TAG, "AddressFragment -> onCreate")
        viewModel = injectViewModel(viewModelFactory)

        // Не удалять фрагмент (onDestroy) при пересоздании активити.
        // Важно для сохранения состояния экрана при повороте устройства
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return FragmentAddressBinding.inflate(inflater, container, false).apply {
            Log.d(LOG_TAG, "AddressFragment -> onCreateView")
            viewmodel = viewModel
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "AddressFragment -> onViewCreated")

        // Наблюдатель за изменением LiveData editSaved (сохранение адреса)
        viewModel.editSaved.observe(viewLifecycleOwner, androidx.lifecycle.Observer<Boolean> {
            if (it) {
                Log.d(LOG_TAG, "AddressFragment editSaved observe = $it")
                // Обновить список
                doRequest()
            }
        })

        binding.vBackButton.setOnClickListener { findNavController().popBackStack() }

        binding.vAddButton.setOnClickListener { startToCreateAddress() }

        idSubscription = arguments?.getString(SUBSCRIPTION_ID) ?: ""
        nameSubscription = arguments?.getString(SUBSCRIPTION_NAME) ?: ""
        binding.subLabelTextView.text = nameSubscription

        //------------------------------------------------------------------
        mRecyclerView = view.findViewById(R.id.addressRecyclerView)
        mLayoutManager = LinearLayoutManager(view.context)

        val eimSavedState = viewModel.recyclerViewExpandableItemManagerState.value
        recyclerViewExpandableItemManager = RecyclerViewExpandableItemManager(eimSavedState)
        recyclerViewExpandableItemManager!!.defaultGroupsExpandedState = false

        if (mAdapter == null) {
            mAdapter = AddressAdapter { delivetypeAddrs, deliveAddrBranch ->
                // Нажали на адрес - перейти в окно редактирования адреса
                val bundle = EditAddressFragment.getBundleArguments(idSubscription, delivetypeAddrs, deliveAddrBranch)
                findNavController(this).navigate(R.id.action_addressFragment_to_editAddressFragment, bundle)
            }

            doRequest()
        }

        mWrappedAdapter = recyclerViewExpandableItemManager!!.createWrappedAdapter(mAdapter!!)

        val animator = RefactoredDefaultItemAnimator()
        animator.supportsChangeAnimations = false

        mRecyclerView?.apply {
            layoutManager = mLayoutManager
            adapter = mWrappedAdapter
            itemAnimator = animator
            setHasFixedSize(false)
        }

        recyclerViewExpandableItemManager!!.attachRecyclerView(mRecyclerView ?: return)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Сохранение элемента списка агентов
        viewModel.editSaved.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it) {
                Log.d(LOG_TAG, "AddressFragment editSaved.observe = $it")
                // Обновить список
                doRequest()
            }
        })

        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            setEnabledAddButton(true)
            showExpandableError(it)
        })

        // Получение списка подписок
        viewModel.delivetypeAddrsLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            delivetypeAddrsList.clear()
            delivetypeAddrsList.addAll(it)
            mAdapter?.update(it)
            mAdapter?.notifyDataSetChanged()
            setEnabledAddButton(true)
        })
    }

    private fun doRequest() {
        // Получить список адресов, привязанных к подписке
        viewModel.getDelivetypeAddrs(idSubscription)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setEditSave(false)
        viewModel.setStateSave(recyclerViewExpandableItemManager?.savedState)
    }

    private fun setEnabledAddButton(value: Boolean) {
        Log.d(LOG_TAG, "AddressFragment setEnabledAddButton($value)")
        binding.vAddButton.isEnabled = value
        if (!value) showProgress() else hideProgress()
    }

    // Начало процедуры создания (привязки) адреса
    private fun startToCreateAddress() {
        setEnabledAddButton(false)
        val list = ArrayList<DelivetypeAddrs>()
        list.addAll(delivetypeAddrsList)

        var elementAppPush = -1
        for (i in 0 until list.size) {
            val address = list[i]
            if (address.short_name == APP_PUSH) {
                if (address.address_list.isNotEmpty()) {
                    // Уже есть привязанное устройство, значит, убрать APP_PUSH из меню
                    elementAppPush = i
                    break
                }
            }
        }
        // Уже есть привязанное устройство, значит, убрать APP_PUSH из меню
        if (elementAppPush > -1) {
            list.removeAt(elementAppPush)
        }

        // Вызвать диалог для выбора типа рассылки (EMAIL, SMS, APP_PUSH)
        val contentView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delivery_types, null)
        contentView.headerTextView.text = resources.getString(R.string.choose_notification_type)

        // get colors of the background from the active theme
        val typedValue = TypedValue()
        binding.root.context.theme.resolveAttribute(R.attr.backgroundColor, typedValue, true)
        val backgroundColor = typedValue.resourceId

        val dialog = MaterialDialog.Builder(requireContext())
            .backgroundColor(ContextCompat.getColor(binding.root.context, backgroundColor))
            .customView(contentView, false)
            .negativeText(R.string.cancel)
            .negativeColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
            .onNegative { _, _ ->  setEnabledAddButton(true)}
            .canceledOnTouchOutside(true)
            .cancelListener { setEnabledAddButton(true) }
            .build()

        val recyclerView: RecyclerView = contentView.findViewById(R.id.dtRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(contentView.context)
        recyclerView.adapter = DelivetypeSubscrAdapter(list) { pos ->
            dialog.dismiss()
            // Перейти в окно добавления адреса
            val bundle = EditAddressFragment.getBundleArguments(idSubscription, list[pos], null)
            findNavController(this).navigate(
                R.id.action_addressFragment_to_editAddressFragment,
                bundle
            )
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "AddressFragment -> onResume")
        viewModel.getEditSave()
    }
}
