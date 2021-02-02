package com.chelinvest.notification.ui.fragments.subscr

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.chelinvest.notification.R
import com.chelinvest.notification.databinding.FragmentSubscrBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import com.chelinvest.notification.model.ObjAny
import com.chelinvest.notification.model.ObjParam
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.ui.fragments.address.AddressFragment
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager
import kotlinx.android.synthetic.main.dialog_field_values.view.*
import com.chelinvest.notification.ui.fragments.subscr.dialog.FieldValuesAdapter
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.Constants.BRANCH_ID
import com.chelinvest.notification.utils.Constants.BRANCH_NAME
import com.chelinvest.notification.utils.Constants.LOG_TAG
import androidx.lifecycle.Observer
import com.chelinvest.notification.ui.custom.ModifiedEditText
import com.chelinvest.notification.utils.Constants.SUBSCR_INFO

class SubscrFragment : BaseFragment() {
    private lateinit var viewModel: SubscrViewModel
    private lateinit var binding: FragmentSubscrBinding

    var map = HashMap<String, ObjParam>()
    var index = 0

    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: SubscrAdapter? = null

    private var launchCount: Int? = null
    private var isFirst = false

    private var isGoToLast = false

    ///*
    companion object {
        // Вариант передачи параметров во фрагмент
        fun getBundleArguments(branchId: String, branchName: String): Bundle {
            return Bundle().apply {
                this.putString(BRANCH_ID, branchId)
                this.putString(BRANCH_NAME, branchName)
            }
        }
    }
    //*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(LOG_TAG, "SubscrFragment -> onCreate")
        viewModel = injectViewModel(viewModelFactory)

        // Не удалять фрагмент (onDestroy) при пересоздании активити.
        // Важно для сохранения состояния экрана при повороте устройства
        //retainInstance = true

        // Восстановление mAdapter из savedInstanceState (не актуально, так как mAdapter и так никуда не пропадает)
        //if (savedInstanceState != null) {
        //    mAdapter = savedInstanceState.getParcelable(STATE_ADAPTER)
        //}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return FragmentSubscrBinding.inflate(inflater, container, false).apply {
            Log.d(LOG_TAG, "SubscrFragment -> onCreateView")
            viewmodel = viewModel
            binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "SubscrFragment -> onViewCreated")

        // Заголовок фрагмента
        binding.subLabelTextView.text = arguments?.getString(BRANCH_NAME)

        // Получить значение CheckBox "Только активные подписки" из ViewModel
//        binding.vCheckBox.isChecked = viewModel.activeOnly.value ?: false
        binding.onlyActiveSwitch.isChecked = viewModel.activeOnly.value ?: false

        // Получить число стартов приложения. Если первый или второй старт, то показать анимацию списка
        launchCount = viewModel.getLaunchCount()
        launchCount?.let {
            isFirst = it in 1..2
        }

        Log.d(LOG_TAG, if(savedInstanceState == null) "savedInstanceState=null" else "savedInstanceState!=null")

        // Кнопка "Назад"
        binding.vBackButton.setOnClickListener { findNavController().popBackStack() }

        // Кнопка - вызов диалога создания нового элемента списка (агента)
        binding.vAddButton.setOnClickListener { startToCreateSubscr() }

        // CheckBox "Только активные подписки"
//        binding.vCheckBox.setOnClickListener{
//            viewModel.setActiveOnly(vCheckBox.isChecked)
//            // Обновить список
//            doRequest{}
//        }

        //------------------------------------------------------------------
        mLayoutManager = LinearLayoutManager(view.context)

        if (mAdapter == null) {
            Log.d(LOG_TAG, "SubscrFragment -> onViewCreated new mAdapter")
            mAdapter = SubscrAdapter(isFirst) { elementSubscr, id, pos, press ->
                when (press) {
                    0 -> {
                        // Перейти в настройку адресов конкретного агента
                        moveToTypesForSubscription(this, elementSubscr.id, elementSubscr.name)
                    }
                    1 -> {
                        // Редактирование подписки (описание, активность)
                        editSubscription(this, elementSubscr)
                    }
                    2 -> {
                        // Удаление подписки
                        showDialogDelete(view.context, elementSubscr.name + " ?") {

                            // получить позицию списка, на которую позиционировать список в случае удаления элемента
                            //val nextPos = mAdapter?.getNextId(id)

                            // выполнить операцию удаления
                            viewModel.delSubscript(id)

                                /*
                                    // позиционирование на элемент списка с индексом nextPos
                                    nextPos?.let {
                                        vRecyclerView?.smoothSnapToPosition(nextPos, LinearSmoothScroller.SNAP_TO_END)
                                    }
                                    // Удалить из списка адаптера
                                    vRecyclerView?.adapter?.notifyItemRemoved(pos)
                                    vRecyclerView.invalidate()
                                    vRecyclerView?.adapter?.notifyItemRangeChanged(
                                        pos,
                                        elementSubscr.size
                                    )
                                    vRecyclerView?.invalidate()
                                    vRecyclerView?.adapter?.notifyDataSetChanged()
                                    updateRecyclerView()
                                */
                        }
                    }
                    3 -> {
                        // Нажатие на switch "Подписка активна"
                        //showDialogChangeActive(view.context, elementSubscr.name + " ?") {
                            Log.d(LOG_TAG, "${elementSubscr.value} = ${elementSubscr.name}")
                            // Выполнить команду 1.7. update_delivery_subscription_for_branch
                            viewModel.updateSubscr(
                                elementSubscr.id,
                                elementSubscr.name,
                                if (elementSubscr.value == "Y") 0 else 1
                            )
                        //}
                    }
                    else -> {
                        showExpandableError(press.toString())
                    }
                }
            } // адаптер

            // Обновить список
            Log.d(LOG_TAG, "mAdapter == null -> refreshList()")
            refreshList()
        }

        val animator = RefactoredDefaultItemAnimator()
        animator.supportsChangeAnimations = false

        val swipeManager = RecyclerViewSwipeManager()
        val wrapperAdapter = swipeManager.createWrappedAdapter(mAdapter ?: return)

        binding.subscriptRecyclerView.apply {
            layoutManager = mLayoutManager
            this.adapter = wrapperAdapter
            itemAnimator = animator
            setHasFixedSize(false)
        }

        swipeManager.attachRecyclerView(binding.subscriptRecyclerView ?: return)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Сохранение элемента списка агентов
        viewModel.editSaved.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it) {
                Log.d(LOG_TAG, "SubscrFragment  editSaved.observe = $it")
                // Обновить список
                refreshList()
            }
        })

        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            isGoToLast = false
            hideProgress()
            showExpandableError(it)
            setEnabledAddButton(true)
        })

        // Получение списка подписок
        viewModel.deliverySubscriptionsLiveEvent.observeEvent(viewLifecycleOwner, Observer { array ->
            hideProgress()
            Log.d(LOG_TAG, "SubscrFragment deliverySubscriptionsLiveEvent.observeEvent array.count=${array.count()}")
            mAdapter?.update(
                if (binding.onlyActiveSwitch.isChecked)  // если только активные - отфильтровать
                    array.filter {
                        it.value == "Y"
                    } as ArrayList<DeliveSubscriptionForBranch>
                else
                    array
            )

            //mAdapter?.notifyDataSetChanged()
            if (isGoToLast) {
                isGoToLast = false
                // позиционирование на последний элемент списка
                binding.subscriptRecyclerView.smoothSnapToPosition(mAdapter!!.getElements() - 1)
            }
            setEnabledAddButton(true)
        })

        // Изменение активности подписки
        viewModel.activeDeliverySubscriptionsLiveEvent.observeEvent(viewLifecycleOwner, Observer { array ->
            mAdapter?.changeItemValueById(array[0])
        })

        // Удаление подписки
        viewModel.deleteDeliverySubscriptionLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            Log.d(LOG_TAG, "SubscrFragment deleteDeliverySubscriptionLiveEvent.observeEvent")
            // Обновить список
            refreshList()
        })

        // Список атрибутов для добавления новой подписки
        viewModel.inputFieldsLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            if (it.size == 0) {
                Log.d(LOG_TAG, "SubscrFragment get_input_fields_for_branch вернул пустой массив obj_any")
                showExpandableError("Список входящих полей для уведомления пуст!")
                setEnabledAddButton(true)
            } else {
                // Показать диалоги для всех атрибутов с выбором значения для каждого атрибута
                showDialogsRecourse(it)
            }
        })

        // Создание новой подписки
        viewModel.createSubscriptionLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            Log.d(LOG_TAG, "SubscrFragment createSubscriptionLiveEvent.observeEvent")
            isGoToLast = true
            // Обновить список
            refreshList()
        })

        // Показать только активные/все подписки
        viewModel.activeOnly.observeEvent(viewLifecycleOwner, Observer {
            Log.d(LOG_TAG, "SubscrFragment activeOnly.observeEvent")
            // Обновить список
            refreshList()
        })
    }

    private fun setEnabledAddButton(value: Boolean) {
        Log.d(LOG_TAG, "SubscrFragment setEnabledAddButton($value)")
        binding.vAddButton.isEnabled = value
        //binding.vAddButton.visibility = if (value) View.VISIBLE else View.INVISIBLE
        if (!value) showProgress() else hideProgress()
    }

    // Обновить список
    private fun refreshList() {
        Log.d(LOG_TAG, "refreshList")
        showProgress()
        // Получить список подписок: get_delivery_subscription_for_branch
        viewModel.getDeliverySubscriptionsForBranch()
    }

    // Диалог удаления элемента списка
    private fun showDialogDelete(context: Context, text: String, onPositive: (() -> Unit)) {
        MaterialDialog.Builder(context)
            .title(R.string.del_subs)
            .titleColor(ContextCompat.getColor(context, R.color.tomato))
            .iconRes(R.drawable.ic_warning_red_24dp)
            .content(text)
            .contentColor(ContextCompat.getColor(context, R.color.black))
            .canceledOnTouchOutside(true)
            .positiveText(R.string.yes)
            .negativeText(R.string.no)
            .onPositive { _, _ ->
                onPositive()
            }
            .build()
            .show()
    }

    private fun showDialogChangeActive(context: Context, text: String, onPositive: (() -> Unit)) {
        MaterialDialog.Builder(context)
            .title(R.string.del_subs)
            .titleColor(ContextCompat.getColor(context, R.color.tomato))
            .iconRes(R.drawable.ic_warning_red_24dp)
            .content(text)
            .contentColor(ContextCompat.getColor(context, R.color.black))
            .canceledOnTouchOutside(true)
            .positiveText(R.string.yes)
            .negativeText(R.string.no)
            .onPositive { _, _ ->
                onPositive()
            }
            .build()
            .show()
    }

    // Начало процедуры создания подписки
    private fun startToCreateSubscr() {
        setEnabledAddButton(false)
        index = 0
        map.clear()

        // Получить список входящих полей подписки, доступных для уведомления конкретного типа
        viewModel.getInputFields()
    }

    private fun showDialogsRecourse(objAnyList: ArrayList<ObjAny>) {
        // Если все списки атрибутов исчерпаны, выполнить создание подписки, используя map выбранных атрибутов
        if (index > objAnyList.size - 1) {
            // Создать новую подписку со значениями по умолчанию -> 1.5. create_delivery_subscription_for_branch
            viewModel.createSubscription(map)
            return
        }

        val field: ObjAny = objAnyList[index]

        // Прочитать значения для текущего поля
        viewModel.getFieldValues(field.id) { objParamList ->
            // Вызвать диалог(и) для выбора агента (или чего-то ещё)
            val contentView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_field_values, null)
            contentView.headerTextView.text = String.format(resources.getString(R.string.choose_attr_value), field.name)

            // get colors of the background from the active theme
            val typedValue = TypedValue()
            binding.root.context.theme.resolveAttribute(R.attr.backgroundColor, typedValue, true)
            val backgroundColor = typedValue.resourceId

            val dialog = MaterialDialog.Builder(requireContext())
                //.title(resources.getString(R.string.dialog_create_subscr_title))
                //.titleGravity(GravityEnum.CENTER)
                .backgroundColor(ContextCompat.getColor(binding.root.context, backgroundColor))
                .customView(contentView, false)
                .negativeText(R.string.cancel)
                .negativeColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                .onNegative { _, _ ->  setEnabledAddButton(true)}
                .canceledOnTouchOutside(true)
                .cancelListener { setEnabledAddButton(true) }
                .build()

            val recyclerView: RecyclerView = contentView.findViewById(R.id.valuesRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(contentView.context)
            recyclerView.adapter = FieldValuesAdapter(objParamList) { selectedElement ->
                map[field.name] = selectedElement
                dialog.dismiss()

                // Перейти к следующему списку атрибутов
                index++
                showDialogsRecourse(objAnyList)
            }

            // Фильтрация
            val searchEditText = contentView.findViewById<ModifiedEditText>(R.id.searchEditText)
            searchEditText.onTextChanged = { str ->
                (recyclerView.adapter as FieldValuesAdapter).updateList(objParamList.filter {
                    it.name.contains(str!!, ignoreCase = true)
                } as ArrayList<ObjParam>)
            }

            dialog.show()
        }
    }

    // Для прокрутки до нужного пункта
    private fun RecyclerView.smoothSnapToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
        val smoothScroller = object: LinearSmoothScroller(this.context) {
            override fun getVerticalSnapPreference(): Int {
                return snapMode
            }

            override fun getHorizontalSnapPreference(): Int {
                return snapMode
            }
        }
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }

    override fun onPause() {
        super.onPause()
        Log.d(LOG_TAG, "SubscrFragment -> onPause")
        viewModel.setEditSave(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(LOG_TAG, "SubscrFragment -> onDestroyView")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(LOG_TAG, "SubscrFragment -> onSaveInstanceState")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(LOG_TAG, "SubscrFragment -> onViewStateRestored")
    }

    // Перейти в настройку адресов конкретного агента
    private fun moveToTypesForSubscription (view: BaseFragment, idSubscription: String, nameSubscription: String) {
        val bundle = AddressFragment.getBundleArguments(idSubscription, nameSubscription)
        NavHostFragment.findNavController(view)
            .navigate(R.id.action_subscrFragment_to_addressFragment, bundle)
    }

    // Редактирование подписки (описание, активность)
    private fun editSubscription(view: BaseFragment, subscrInfo: DeliveSubscriptionForBranch) {
        val bundle = Bundle()
        bundle.putSerializable(SUBSCR_INFO, subscrInfo)
        NavHostFragment.findNavController(view)
            .navigate(R.id.action_subscrFragment_to_editSubscrFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "SubscrFragment -> onResume")
        viewModel.getEditSave()
    }
}
