package com.chelinvest.notification.ui.fragments.subscr

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.MaterialDialog
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.R
import com.chelinvest.notification.databinding.FragmentSubscrBinding
import com.chelinvest.notification.di.injectViewModel
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import com.chelinvest.notification.model.ObjAny
import com.chelinvest.notification.model.ObjParam
import com.chelinvest.notification.ui.BaseFragment
import com.chelinvest.notification.ui.CustomFragment
import com.chelinvest.notification.ui.fragments.address.AddressFragment
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager
import kotlinx.android.synthetic.main.dialog_field_values.view.*
import kotlinx.android.synthetic.main.fragment_subscr.*
import com.chelinvest.notification.ui.fragments.subscr.dialog.FieldValuesAdapter
import com.chelinvest.notification.utils.Constants
import com.chelinvest.notification.utils.Constants.BRANCH_ID
import com.chelinvest.notification.utils.Constants.BRANCH_NAME
import com.chelinvest.notification.utils.Constants.LOG_TAG
import androidx.lifecycle.Observer

class SubscrFragment : BaseFragment() {
    private lateinit var viewModel: SubscrViewModel
    private lateinit var binding: FragmentSubscrBinding

    var map = HashMap<String, ObjParam>()
    var index = 0

    private var vRecyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: SubscrAdapter? = null

    private var launchCount: Int? = null
    private var isFirst = false

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
        branchNameTextView.text = arguments?.getString(BRANCH_NAME)

        // Получить значение CheckBox "Только активные подписки" из ViewModel
        binding.vCheckBox.isChecked = viewModel.activeOnly.value ?: false

        // Получить число стартов приложения. Если первый или второй старт, то показать анимацию списка
        launchCount = Preferences.getInstance().getLaunchCount(view.context)
        launchCount?.let {
            isFirst = it in 1..2
        }

        Log.d(LOG_TAG, if(savedInstanceState == null) "savedInstanceState=null" else "savedInstanceState!=null")

        // Кнопка "Назад"
        binding.vBackButton.setOnClickListener { findNavController().popBackStack() }

        // Кнопка - вызов диалога создания нового элемента списка (агента)
        binding.vAddButton.setOnClickListener{ startToCreateSubscr() }

        // CheckBox "Только активные подписки"
        binding.vCheckBox.setOnClickListener{
            viewModel.setActiveOnly(vCheckBox.isChecked)
            // Обновить список
            doRequest{}
        }

        //------------------------------------------------------------------
        vRecyclerView = view.findViewById(R.id.subscriptRecyclerView)
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
                    else -> {
                        //Toast.makeText(view.context, press.toString(), Toast.LENGTH_SHORT).show()
                        showExpandableError(press.toString())
                    }
                }
            } // адаптер

            // Обновить список
            doRequest {}
        }

        val animator = RefactoredDefaultItemAnimator()
        animator.supportsChangeAnimations = false

        val swipeManager = RecyclerViewSwipeManager()
        val wrapperAdapter = swipeManager.createWrappedAdapter(mAdapter ?: return)

        vRecyclerView?.apply {
            layoutManager = mLayoutManager
            this.adapter = wrapperAdapter
            itemAnimator = animator
            setHasFixedSize(false)
        }

        swipeManager.attachRecyclerView(vRecyclerView ?: return)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Сохранение элемента списка агентов
        viewModel.editSaved.observe(viewLifecycleOwner, Observer<Boolean> {
            if (it) {
                Log.d(LOG_TAG, "SubscrFragment -> onViewCreated observe = $it")
                // Обновить список
                doRequest {}
            }
        })

        viewModel.errorLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            showExpandableError(it)
        })

        // Получение список подписок
        viewModel.deliverySubscriptionsLiveEvent.observeEvent(viewLifecycleOwner, Observer { array ->
            Log.d(LOG_TAG, "SubscrFragment deliverySubscriptionsLiveEvent.observeEvent array.count=${array.count()}")
            mAdapter?.update(
                if (vCheckBox.isChecked)  // если только активные - отфильтровать
                    array.filter {
                        it.value == "Y"
                    } as ArrayList<DeliveSubscriptionForBranch>
                else
                    array
            )
            Log.d(LOG_TAG, "SubscrFragment deliverySubscriptionsLiveEvent.observeEvent")
            mAdapter?.notifyDataSetChanged()
            //setPosition()
        })

        // Удаление подписки
        viewModel.deleteDeliverySubscriptionLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            // Обновить список
            doRequest {}
        })

        viewModel.inputFieldsLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            if (it.size == 0) {
                Log.d(LOG_TAG, "SubscrFragment get_input_fields_for_branch вернул пустой массив obj_any")
                showExpandableError("Список входящих полей для уведомления пуст!")
            } else {
                // Показать диалоги для всех атрибутов с выбором значения для каждого атрибута
                showDialogsRecourse(it)
            }
        })

        viewModel.createSubscriptionLiveEvent.observeEvent(viewLifecycleOwner, Observer {
            // Обновить список
            doRequest {
                // позиционирование на последний элемент списка
                vRecyclerView?.smoothSnapToPosition(mAdapter!!.getElements() - 1)
            }
        })

//        viewModel.getFieldValuesLiveEvent.observeEvent(viewLifecycleOwner, Observer {
//        })
    }

    // Обновить список
    fun doRequest(setPosition : () -> Unit) {
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

    // Начало процедуры создания подписки
    private fun startToCreateSubscr() {
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
            val contentView = LayoutInflater.from(view?.context!!).inflate(R.layout.dialog_field_values, null)
            contentView.headerTextView.text = "Выберите значение атрибута '${field.name}'"

            val dialog: MaterialDialog
            dialog = MaterialDialog.Builder(view?.context!!)
                .title("Создание подписки")
                .titleGravity(GravityEnum.CENTER)
                .customView(contentView, false)
                .negativeText(R.string.cancel)
                .build()

            val recyclerView: RecyclerView = contentView.findViewById(R.id.valuesRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(contentView.context)
            recyclerView.adapter = FieldValuesAdapter(objParamList) { selectedElement ->
                map.put(field.name, selectedElement)
                dialog.dismiss()

                // Перейти к следующему списку атрибутов
                index++
                showDialogsRecourse(objAnyList)
            }

            // Фильтрация
            val searchEditText = contentView.findViewById<EditText>(R.id.searchEditText)
            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val filterText = s.toString()
                    (recyclerView.adapter as FieldValuesAdapter).updateList(objParamList.filter { it.name.contains(filterText, ignoreCase = true) } as ArrayList<ObjParam>)
                }
            })

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
        NavHostFragment.findNavController(view as BaseFragment)
            .navigate(R.id.action_subscrFragment_to_addressFragment, bundle)
    }

    // Редактирование подписки (описание, активность)
    private fun editSubscription(view: BaseFragment, subscrInfo: DeliveSubscriptionForBranch) {
        val bundle = Bundle()
        bundle.putSerializable(Constants.SUBSCR_INFO, subscrInfo)
        NavHostFragment.findNavController(view as BaseFragment)
            .navigate(R.id.action_subscrFragment_to_editSubscrFragment, bundle)
    }
}
