package com.chelinvest.notification.ui.subscr

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.MaterialDialog
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.BRANCH_ID
import com.chelinvest.notification.additional.BRANCH_NAME
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import com.chelinvest.notification.model.ObjAny
import com.chelinvest.notification.model.ObjParam
import com.chelinvest.notification.ui.CustomFragment
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager
import kotlinx.android.synthetic.main.dialog_field_values.view.*
import kotlinx.android.synthetic.main.fragment_subscr.*
import com.chelinvest.notification.ui.subscr.dialog.FieldValuesAdapter
import java.util.*

// Сохранение состояния фрагментов (Fragment)
// https://habr.com/ru/post/280586/

class SubscrFragment : CustomFragment<SubscrPresenter>(), ISubscrView {

    //val STATE_ADAPTER = "SUBSCRADAPTER"
    var map = HashMap<String, ObjParam>()
    var index = 0

    private var vRecyclerView: RecyclerView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: SubscrAdapter? = null

    private var launchCount: Int? = null
    private var isFirst = false

    private val model: SubscrViewModel by activityViewModels()
    private var needLoad: Boolean = false


    ///*
    companion object {
        /*
        // Вариант передачи параметров в активити
        fun getStartIntent(context: Context, branchId: String, branchName: String): Intent {
            return Intent(context, SubscrActivity::class.java)
                .putExtra(BRANCH_ID, branchId)
                .putExtra(BRANCH_NAME, branchName)
        }
        */

        // Вариант передачи параметров во фрагмент
        fun getBundleArguments(branchId: String, branchName: String): Bundle {
            return Bundle().apply {
                this.putString(BRANCH_ID, branchId)
                this.putString(BRANCH_NAME, branchName)
            }
        }
    }
    //*/

    override fun createPresenter(): SubscrPresenter = SubscrPresenter()

    private fun compare(element: DeliveSubscriptionForBranch): Boolean {
        return element.value.equals("YES")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        Log.wtf("SUBSCRFRAGMENT", "OnCreate")

        //if (savedInstanceState != null) {
        //    Log.wtf("SUBSCRFRAGMENT", "OnCreate restore mAdapter")
        //    mAdapter = savedInstanceState.getParcelable(STATE_ADAPTER)
        //}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.wtf("SUBSCRFRAGMENT", "onCreateView")
        return inflater.inflate(R.layout.fragment_subscr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchCount = Preferences.getInstance().getLaunchCount(view.context)
        launchCount?.let {
            isFirst = it in 1..2
        }

        Log.wtf("SUBSCRFRAGMENT", "onViewCreated")
        Log.wtf("SUBSCRFRAGMENT", if(savedInstanceState == null) "savedInstanceState=null" else "savedInstanceState!=null")

        vBackButton.setOnClickListener { findNavController().popBackStack() }
        vAddButton.setOnClickListener{ startToCreateSubscr() }
        vCheckBox.setOnClickListener{
            // Обновить список
            Log.wtf("SUBSCRFRAGMENT", "vCheckBox")
            doRequest{}
        }

        //------------------------------------------------------------------
        vRecyclerView = view.findViewById(R.id.subscriptRecyclerView)
        mLayoutManager = LinearLayoutManager(view.context)


        model.saved.observe(viewLifecycleOwner, androidx.lifecycle.Observer<Boolean> {
            needLoad = it
        })

        if (mAdapter == null) {
            Log.wtf("SUBSCRFRAGMENT", "onViewCreated new mAdapter")
            mAdapter = SubscrAdapter(isFirst) { elementSubscr, id, pos, press ->
                when (press) {
                    0 -> {
                        // Перейти в настройку адресов конкретного агента
                        getPresenter().moveToTypesForSubscription(
                            view.context,
                            elementSubscr.id,
                            elementSubscr.name
                        )
                    }
                    1 -> {
                        // Редактирование подписки (описание, активность)
                        getPresenter().editSubscription(
                            view.context,
                            this as ISubscrView,
                            elementSubscr
                        )
                    }
                    2 -> {
                        // Удаление подписки
                        showDialogDelete(view.context, elementSubscr.name + " ?") {

                            // получить позицию списка, на которую позиционировать список в случае удаления элемента
                            //val nextPos = mAdapter?.getNextId(id)

                            // выполнить операцию удаления
                            getPresenter().delSubscript(view.context, this as ISubscrView, id) {
                                if (!it.equals("")) {
                                    Toast.makeText(view.context, it, Toast.LENGTH_SHORT).show()
                                } else {
                                    // Обновить список
                                    doRequest {}
                                    /*
                                    // позиционирование на элемент списка с индексом nextPos
                                    nextPos?.let {
                                        vRecyclerView?.smoothSnapToPosition(nextPos, LinearSmoothScroller.SNAP_TO_END)
                                    }
                                }
                                */
                                    // Удалить из списка адаптера
                                    /*
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
                        }
                    }
                    else -> {
                        Toast.makeText(view.context, press.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } // адаптер

            needLoad = true
        }
        //else {
        //    needLoad = false
        //}

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

        swipeManager.attachRecyclerView(vRecyclerView!!)

        // Использование ItemTouchHelper для перетаскивания пунктов списка и свайпа для удаления
        //val callback : ItemTouchHelper.Callback = SimpleItemTouchHelperCallback(adapter)
        //val touchHelper = ItemTouchHelper(callback)
        //touchHelper.attachToRecyclerView(recyclerView)
        //------------------------------------------------------------------

        /*
        vSwipeRefreshLayout.setColorSchemeResources(R.color.tangelo)
        vSwipeRefreshLayout.setOnRefreshListener {
            //vSwipeRefreshLayout.isRefreshing = false
            onSwipeRefresh()
        }
        */

        val nameBranch = arguments?.getString(BRANCH_NAME)
        branchNameTextView.setText(nameBranch)

        if (needLoad) {
            // Обновить список
            doRequest {}
        }
    }


    // Обновить список
    fun doRequest(setPosition : () -> Unit) {
        // Получить список подписок
        getPresenter().getSubscript(this.view?.context!!, this)  {array->
            mAdapter?.update(
                if (vCheckBox.isChecked)  // если только активные - отфильтровать
                    array.filter {
                        it.value.equals("Y")
                    } as ArrayList<DeliveSubscriptionForBranch>
                else
                    array
            )
            Log.wtf("SUBSCRFRAGMENT", "doRequest -> notifyDataSetChanged()")
            mAdapter?.notifyDataSetChanged()
            setPosition()
        }
    }

    /*
    fun onSwipeRefresh() {
        // Обновить список
        //doRequest { vSwipeRefreshLayout.isRefreshing = false }
    }

    override fun showProgressDialog() {
        //vSwipeRefreshLayout.isRefreshing = true
    }

    override fun hideProgressDialog() {
        //vSwipeRefreshLayout.isRefreshing = false
    }
    */

    /* --
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EDITSUBSCRACTIVITY_CODE -> { // после редактирования подписки (описание, активность)
                if (resultCode == RESULT_OK) {
                    // обновить элемент списка

                    // Обновить список
                    doRequest{}
                }
                //else {
                    // просто закрыть меню (сдвинуть элемент на место)
                //}
            }
        }
    }
    --  */

    fun showDialogDelete(context: Context, text: String, onPositive: (() -> Unit)) {
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

        val idSession = Preferences.getInstance().getSessionId(this.view?.context!!)
        val branchShort = Preferences.getInstance().getBranchShort(this.view?.context!!)

        // Получить список входящих полей подписки, доступных для уведомления конкретного типа
        getPresenter().getInputFields(this.view?.context!!, this, idSession!!, branchShort!!) {
            if (it.size == 0) {
                Log.wtf("InputFields", "[SubscrFragment] get_input_fields_for_branch вернул пустой массив obj_any")
                Toast.makeText(view?.context, "Список входящих полей для уведомления $branchShort пуст!", Toast.LENGTH_LONG).show()
            } else {
                // Показать диалоги для всех атрибутов с выбором значения для каждого атрибута
                showDialogsRecourse(it)
            }
        }
    }

    private fun showDialogsRecourse(objAnyList: ArrayList<ObjAny>) {

        val idSession = Preferences.getInstance().getSessionId(this.view?.context!!)
        val branchShort = Preferences.getInstance().getBranchShort(this.view?.context!!)

        // Если все списки атрибутов исчерпаны, выполнить создание подписки, используя map выбранных атрибутов
        if (index > objAnyList.size - 1) {
            // Создать новую подписку со значениями по умолчанию -> 1.5. create_delivery_subscription_for_branch
            getPresenter().createSubscr(this.view?.context!!, this, idSession!!, branchShort!!, map) {
                if (!it.isNullOrEmpty()) {
                    Toast.makeText(this.view?.context!!, it, Toast.LENGTH_SHORT).show()
                } else {
                    // Обновить список
                    doRequest {
                        // позиционирование на последний элемент списка
                        vRecyclerView?.smoothSnapToPosition(mAdapter!!.getElements() - 1)
                    }
                }
            }
            return
        }

        val field: ObjAny = objAnyList[index]

        // Прочитать значения для текущего поля
        getPresenter().getFieldValues(this.view?.context!!, this, idSession!!, branchShort!!, field.id) { objParamList ->
            // Вызвать диалог(и) для выбора агента (или чего-то ещё)
            val contentView = LayoutInflater.from(this.view?.context!!).inflate(R.layout.dialog_field_values, null)
            contentView.headerTextView.text = "Выберите значение атрибута '${field.name}'"

            val dialog: MaterialDialog
            dialog = MaterialDialog.Builder(this.view?.context!!)
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

            //--
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
            //--

            dialog.show()
        }
    }


    // Для прокрутки до нужного пункта
    fun RecyclerView.smoothSnapToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
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
        Log.wtf("SUBSCRFRAGMENT", "onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.wtf("SUBSCRFRAGMENT", "onDestroyView")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //outState.putParcelable(STATE_ADAPTER, mAdapter)
        super.onSaveInstanceState(outState)
        Log.wtf("SUBSCRFRAGMENT", "onSaveInstanceState")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.wtf("SUBSCRFRAGMENT", "onViewStateRestored")
    }
}
