package com.chelinvest.notification.ui.fragments.address

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.MaterialDialog
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager
import kotlinx.android.synthetic.main.dialog_field_values.view.*
import com.chelinvest.notification.R
import com.chelinvest.notification.model.DelivetypeAddrs
import com.chelinvest.notification.ui.fragments.address.dialog.DelivetypeSubscrAdapter
import com.chelinvest.notification.ui.fragments.address.edit.EditAddressFragment
import com.chelinvest.notification.ui.CustomFragment
import com.chelinvest.notification.utils.Constants.APP_PUSH
import com.chelinvest.notification.utils.Constants.SUBSCRIPTION_ID
import com.chelinvest.notification.utils.Constants.SUBSCRIPTION_NAME
import kotlinx.android.synthetic.main.fragment_address.*


class AddressFragment : CustomFragment<AddressPresenter>(), IAddressView {

    private val SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager"
    //private var isRefreshing: Boolean = false
    private var groupList: List<DelivetypeAddrs> = emptyList()

    private lateinit var idSubscription: String
    private lateinit var nameSubscription: String
    //private var filter: String = ""
    //private val expandedIds = HashSet<String>()
    //private var selectedCardMode = -1

    private var mRecyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: AddressAdapter? = null
    private var mWrappedAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    private var recyclerViewExpandableItemManager: RecyclerViewExpandableItemManager? = null

    private val model: AddressViewModel by activityViewModels()

    companion object {
        // Вариант передачи параметров во фрагмент
        fun getBundleArguments(idSubscription: String, nameSubscription: String): Bundle {
            return Bundle().apply {
                this.putString(SUBSCRIPTION_ID, idSubscription)
                this.putString(SUBSCRIPTION_NAME, nameSubscription)
            }
        }
    }

    override fun createPresenter(): AddressPresenter = AddressPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Не удалять фрагмент (onDestroy) при пересоздании активити.
        // Важно для сохранения состояния экрана при повороте устройства
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Наблюдатель за изменением LiveData editSaved (сохранение адреса)
        model.editSaved.observe(viewLifecycleOwner, androidx.lifecycle.Observer<Boolean> {
            if (it) {
                Log.wtf("ADDRESSFRAGMENT", "onViewCreated observe = " + it.toString())
                // Обновить список
                doRequest()
            }
        })

        vBackButton.setOnClickListener { findNavController().popBackStack() }

        vAddButton.setOnClickListener { startToCreateAddress() }

        idSubscription = arguments?.getString(SUBSCRIPTION_ID) ?: ""
        nameSubscription = arguments?.getString(SUBSCRIPTION_NAME) ?: ""
        subsNameTextView.setText(nameSubscription)

        //------------------------------------------------------------------
        mRecyclerView = view.findViewById(R.id.addressRecyclerView)
        mLayoutManager = LinearLayoutManager(view.context)

        //val eimSavedState = savedInstanceState?.getParcelable<Parcelable>(SAVED_STATE_EXPANDABLE_ITEM_MANAGER)
        val eimSavedState = model.recyclerViewExpandableItemManagerState.value
        recyclerViewExpandableItemManager = RecyclerViewExpandableItemManager(eimSavedState)
        recyclerViewExpandableItemManager!!.defaultGroupsExpandedState = true

        if (mAdapter == null) {
            mAdapter = AddressAdapter { delivetypeAddrs, deliveAddrBranch ->
                // Нажали на адрес - перейти в окно редактирования адреса
                val bundle = EditAddressFragment.getBundleArguments(idSubscription, delivetypeAddrs, deliveAddrBranch)
                findNavController(this as CustomFragment<*>).navigate(R.id.action_addressFragment_to_editAddressFragment, bundle)
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

        vSwipeRefreshLayout.setColorSchemeResources(R.color.tangelo)
        vSwipeRefreshLayout.setOnRefreshListener {
            groupList = emptyList()
            doRequest()
            //vSwipeRefreshLayout.isRefreshing = false
        }
    }

    fun doRequest() {
        // Получить список адресов, привязанных к подписке
        getPresenter().getDelivetypeAddrs(view?.context!!, idSubscription, this) {
            mAdapter?.update(it)
            mAdapter?.notifyDataSetChanged()
        }
    }

    override fun onPause() {
        super.onPause()
        model.setEditSave(false)
        model.setStateSave(recyclerViewExpandableItemManager?.savedState)
    }

    //override fun onSaveInstanceState(outState: Bundle) {
    //    outState.putParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER, recyclerViewExpandableItemManager?.savedState)
    //    super.onSaveInstanceState(outState)
    //    Log.wtf("ADDRESSFRAGMENT", "onSaveInstanceState")
    //}

    //override fun onViewStateRestored(savedInstanceState: Bundle?) {
    //    super.onViewStateRestored(savedInstanceState)
    //    Log.wtf("ADDRESSFRAGMENT", "onViewStateRestored")
    //}

    override fun showProgressDialog() {
        vSwipeRefreshLayout.isRefreshing = true
    }

    override fun hideProgressDialog() {
        vSwipeRefreshLayout.isRefreshing = false
    }

    // Начало процедуры создания (привязки) адреса
    private fun startToCreateAddress() {
        // Получить список типов рассылки, доступных для подписки (уведомления)
        getPresenter().getDelivetypeAddrs(view?.context!!, idSubscription, this) { delivetypeAddrs ->

            var elementAppPush = -1
            for (i in 0..delivetypeAddrs.size - 1) {
                val addr = delivetypeAddrs[i]
                if (addr.short_name.equals(APP_PUSH)) {
                    if (addr.address_list.size > 0) {
                        // Уже есть привязанное устройство, значит, убрать APP_PUSH из меню
                        elementAppPush = i
                        break
                    }
                }
            }
            // Уже есть привязанное устройство, значит, убрать APP_PUSH из меню
            if (elementAppPush > -1) {
                delivetypeAddrs.removeAt(elementAppPush)
            }

            // Вызвать диалог для выбора типа рассылки (EMAIL, SMS, APP_PUSH)
            val contentView = LayoutInflater.from(view?.context).inflate(R.layout.dialog_delivery_types, null)
            contentView.headerTextView.text = resources.getString(R.string.choose_notificaion_type)

            val dialog: MaterialDialog
            dialog = MaterialDialog.Builder(view?.context!!)
                .title(resources.getString(R.string.create_notificaion))
                .titleGravity(GravityEnum.CENTER)
                .customView(contentView, false)
                .negativeText(R.string.cancel)
                .build()

            val recyclerView: RecyclerView = contentView.findViewById(R.id.dtRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(contentView.context)
            recyclerView.adapter = DelivetypeSubscrAdapter(delivetypeAddrs) { pos ->
                dialog.dismiss()
                //Toast.makeText(this, delivetypeAddrs[pos].id + " " + delivetypeAddrs[pos].short_name + " " +
                //        delivetypeAddrs[pos].has_send_period + ": setDeliveryAddressForSubscription()", Toast.LENGTH_SHORT).show()

                // Перейти в окно добавления адреса
                val bundle = EditAddressFragment.getBundleArguments(idSubscription, delivetypeAddrs[pos], null)
                findNavController(this as CustomFragment<*>).navigate(R.id.action_addressFragment_to_editAddressFragment, bundle)
                //val intent = EditAddressFragment.getStartIntent(view?.context!!, idSubscription, delivetypeAddrs[pos], null)
                //this.startActivityForResult(intent, EDITADDRESSACTIVITY_CODE)
            }

            dialog.show()
        }
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            val result: String = data.getStringExtra("SAVED")
            if (result.equals("YES")) {
                doRequest()
            }
        }
    }
    */
}
