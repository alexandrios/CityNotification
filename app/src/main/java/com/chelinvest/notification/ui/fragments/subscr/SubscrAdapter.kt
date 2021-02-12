package com.chelinvest.notification.ui.fragments.subscr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.chelinvest.notification.R
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import com.chelinvest.notification.model.ObjParamV01
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder
import org.joda.time.DateTime
import kotlin.random.Random

class SubscrAdapter(
    private var isFirst: Boolean,
    private val goToSubscription: (DeliveSubscriptionForBranch, id: String, pos: Int, press: Int) -> Unit
) : RecyclerView.Adapter<SubscrAdapter.ViewHolder>(), SwipeableItemAdapter<SubscrAdapter.ViewHolder>
    //, Parcelable
{
    //
    // Использование ItemTouchHelper для перетаскивания пунктов списка и свайпа для удаления
    //, ItemTouchHelperAdapter {

    // класс для внутреннего списка. Добавляет свойство pinned - открыто ли меню у элемента
    private class MyItem(val id: String, val name: String, var value: String, val objList: List<ObjParamV01>) {
        var pinned: Boolean = false
    }

    private val elements = ArrayList<DeliveSubscriptionForBranch>()
    // внутренний список
    private var subscripts: ArrayList<MyItem> = ArrayList()
    // Random-объект для авто-свайпа элементов при первом запуске приложения (для показа меню элементов)
    private val random = Random(DateTime.now().millis)
    // layoutManager - сейчас не используется, оставлен для примера
    private var layoutManager: RecyclerView.LayoutManager? = null
    // Определяет, у какого элемента открыто "засвайпленное" меню
    private var pinnedPosition = -1

    /*
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
    ) {
        pinnedPosition = parcel.readInt()
    }
    */

    init {
        // this is required for swiping feature
        setHasStableIds(true)
    }

    open class ViewHolder(view: View) : AbstractSwipeableItemViewHolder(view) {
        // for swiping feature
        val containerView: FrameLayout = view.findViewById(R.id.container)
        val behindView: RelativeLayout = view.findViewById(R.id.behind_views)

        // for swiping feature
        override fun getSwipeableContainerView(): View {
            return containerView
        }

        val holdName: TextView = view.findViewById(R.id.subscriptNameTextView)
        val activeSwitch: SwitchCompat = view.findViewById(R.id.activeSwitch)
    }

    @Synchronized
    fun update(elements: List<DeliveSubscriptionForBranch>) {
        this.elements.clear()
        this.elements.addAll(elements)

        // Создать внутренний список
        subscripts.clear()
        for (el in elements) {
            subscripts.add(MyItem(el.id, el.name, el.value, el.objList))
        }

        notifyDataSetChanged()
    }

    // Изменение активности подписки в списке (switch)
    fun changeItemValueById(element: DeliveSubscriptionForBranch) {
        for (i in 0 until elements.size) {
            if (elements[i].id == element.id) {
                elements[i].value = element.value
                subscripts[i].value = element.value
                notifyDataSetChanged()
                return
            }
        }
    }

    // для позиционирования в конец списка
    fun getElements() = this.elements.size

    // для позиционирования: получение позиции следующего элемента (планировалось: после удаления элемента - не пригодилось)
    fun getNextId(id: String): Int? {
        var result: Int? = null
        for (i: Int in 0 until elements.size) {
            if (elements[i].id == id) {
                if (i < elements.size - 1) {
                    result = i + 1
                    break
                }
                else {
                    if (i > 0) {
                        result = i - 1
                        break
                    }
                }
            }
        }

        return result
    }

    override fun getItemCount(): Int {
        return subscripts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)     {
        holder.holdName.text = subscripts[position].name
        holder.activeSwitch.isChecked = subscripts[position].value == "Y"

        /*
        Здесь не нужно создавать обработчик нажатий
        Во-первых, создается объект на каждый Bind. Во-вторых, берется position элемента, которая у него была до onBindViewHolder().
        Но элемент с помощью notify() может быть перемещен/удален и его position, следовательно, изменится.

        holder.delButton.setOnClickListener {
            this.goToSubscription(this.subscripts[position].id, position, 2)
        }

        holder.editButton.setOnClickListener {
            this.goToSubscription(this.subscripts[position].id, position, 1)
        }

         holder.activeSwitch.setOnClickListener {
            val b = (it as SwitchCompat).isChecked
            Log.d(LOG_TAG, "$b = ${holder.holdName.text}")
        }
        */

        /* // Эффекты при свайпинге
        val state = holder.swipeState
        val bg: Int
        if  (state.isActive) {
            bg = R.drawable.bg_service_binding_button
        } else if (state.isSwiping) {
            bg = R.drawable.bg_modified_edit_text
        } else {
            bg = R.drawable.bg_modified_error_edit_text
        }
        //holder.containerView.setBackgroundColor(bg)
        //holder.behindView.setBackgroundResource(bg)
        holder.containerView.setBackgroundResource(bg)
        */

        // set swiping properties
        holder.maxLeftSwipeAmount = -0.5f
        holder.maxRightSwipeAmount = 0f
        holder.swipeItemHorizontalSlideAmount = if (subscripts[position].pinned) -0.3f else 0f

        // Or, it can be specified in pixels instead of proportional value.
        /*
        val widthOpen = holder.editButton.width + holder.delButton.width + 20
        val density = holder.itemView.getResources().getDisplayMetrics().density
        val pinnedDistance = (density * widthOpen) // в единицах dp

        holder.setProportionalSwipeAmountModeEnabled(false);
        holder.setMaxLeftSwipeAmount(-pinnedDistance)
        holder.setMaxRightSwipeAmount(0f)
        holder.setSwipeItemHorizontalSlideAmount(if (subscripts[position].pinned) -pinnedDistance else 0f)
        */

        /*
        // Почему-то не работает определитель того, целиком ли показан элемент списка
        layoutManager?.let {
            isFirst = !it.isViewPartiallyVisible(holder.itemView, false, true) && isFirst
        }
        */
    }

    // Событие перед тем как показать холдер (элемент списка)
    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)

        if (isFirst) {
            // Показать, что можно делать свайп
            holder.behindView.visibility = View.VISIBLE
            val arr: IntArray = intArrayOf(0, 0)
            holder.containerView.getLocationOnScreen(arr)
            val shift = -1 * random.nextInt(300)
            holder.containerView.animate().x(shift.toFloat()).setDuration(250).withEndAction {
                holder.containerView.animate().x(arr[0].toFloat())
            }
        }
    }

    // Событие перед тем как спрятать холдер (элемент списка)
    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
    }

    // Создаем слушателя для recyclerView на скроллинг и передаем ему адаптер
    private class ScrollListener(val adapter: SubscrAdapter) : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            // Собственно только для этого и городился огород:
            // Больше не показывать свайп-подсказку
            adapter.isFirst = false
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        // Получаем ссылку на layoutManager (зачем? уже не нужно...)
        layoutManager = recyclerView.layoutManager

        // Добавляем слушателя для recyclerView на скроллинг
        recyclerView.addOnScrollListener(
            ScrollListener(
                this
            )
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_subscript, parent, false)

        // Обработка нажатий на элементы
        return ViewHolder(view).listener { pos, press ->
            if (pos != NO_POSITION) {
                val item = subscripts[pos]
                goToSubscription(elements[pos], item.id, pos, press)
            }
        }
    }

    private fun <T : RecyclerView.ViewHolder> T.listener(event: (Int, Int) -> Unit): T {

        // Нажатие на текстовый layout
        itemView.findViewById<LinearLayout>(R.id.textLayout).setOnClickListener {
            event.invoke(adapterPosition, 0)
        }

        // Нажатие на иконку edit
        itemView.findViewById<ImageView>(R.id.edit_button).setOnClickListener {
            event.invoke(adapterPosition, 1)
        }

        // Нажатие на иконку delete
        itemView.findViewById<ImageView>(R.id.del_button).setOnClickListener {
            event.invoke(adapterPosition, 2)
        }

        // Нажатие на switch "Подписка активна"
        itemView.findViewById<SwitchCompat>(R.id.activeSwitch).setOnClickListener {
            event.invoke(adapterPosition, 3)
        }

        return this
    }

    // ---- for swiping feature ----
    // https://advancedrecyclerview.h6ah4i.com/swipeable/tutorial/
    // for swipe with buttons
    // https://github.com/h6ah4i/android-advancedrecyclerview/blob/develop/example/src/main/java/com/h6ah4i/android/example/advrecyclerview/demo_s_button/SwipeableWithButtonExampleAdapter.java
    // swipe with buttons
    // https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28

    // requires static value, it means need to keep the same value even if the item position has been changed
    override fun getItemId(position: Int): Long {
        return subscripts[position].id.toLong()
    }

    private class SwipeLeftResultAction(var adapter: SubscrAdapter?, val pos: Int) : SwipeResultActionMoveToSwipedDirection() {

        var setPinned: Boolean = false

        override fun onPerformAction() {
            super.onPerformAction()

            val item: MyItem = adapter!!.subscripts[pos]
            if (!item.pinned) {
                item.pinned = true
                adapter?.notifyItemChanged(pos)
                setPinned = true

                adapter?.apply {
                    if (pinnedPosition > -1 && pinnedPosition < getElements() && pinnedPosition != pos) {
                        val oldPinnedItem: MyItem = subscripts[pinnedPosition]
                        if (oldPinnedItem.pinned) {
                            oldPinnedItem.pinned = false
                            notifyItemChanged(pinnedPosition)
                        }
                    }
                }

                adapter?.pinnedPosition = pos
            }
        }

        override fun onSlideAnimationEnd() {
            super.onSlideAnimationEnd()

            /*
            if (setPinned && adapter?.eventListener != null) {
                adapter!!.eventListener?.onItemPinned(pos)

                adapter!!.goToSubscription(adapter!!.subscripts[pos].id, pos, 5)
            }
            */
        }

        override fun onCleanUp() {
            super.onCleanUp()
            adapter = null
        }
    }

    private class UnpinResultAction(var adapter: SubscrAdapter?, val pos: Int) : SwipeResultActionMoveToSwipedDirection() {
        override fun onPerformAction() {
            super.onPerformAction()

            val item: MyItem = adapter!!.subscripts[pos]
            if (item.pinned) {
                item.pinned = false
                adapter?.notifyItemChanged(pos)
                adapter?.pinnedPosition = -1
            }
        }

        override fun onCleanUp() {
            super.onCleanUp()

            adapter = null
        }
    }

    override fun onSwipeItem(holder: ViewHolder, position: Int, result: Int): SwipeResultAction? {
        // Return sub class of the SwipeResultAction.
        //
        // Available base (abstract) classes are;
        // - SwipeResultActionDefault
        // - SwipeResultActionMoveToSwipedDirection
        // - SwipeResultActionRemoveItem
        // - SwipeResultActionDoNothing

        // The argument "result" can be one of the followings;
        //
        // - Swipeable.RESULT_CANCELED
        // - Swipeable.RESULT_SWIPED_LEFT
        // (- Swipeable.RESULT_SWIPED_UP)
        // (- Swipeable.RESULT_SWIPED_RIGHT)
        // (- Swipeable.RESULT_SWIPED_DOWN)

        return when (result) {
            SwipeableItemConstants.RESULT_SWIPED_LEFT -> {
                SwipeLeftResultAction(
                    this,
                    position
                )
            }
            SwipeableItemConstants.RESULT_CANCELED -> {
                SwipeResultActionDefault()
            }
            else -> {
                if (position != NO_POSITION) {
                    UnpinResultAction(
                        this,
                        position
                    )
                } else {
                    null
                }
            }
        }
    }

    override fun onGetSwipeReactionType(holder: ViewHolder, position: Int, x: Int, y: Int): Int {
        // Make swipeable to LEFT direction
        return SwipeableItemConstants.REACTION_CAN_SWIPE_BOTH_H or SwipeableItemConstants.REACTION_CAN_NOT_SWIPE_BOTH_V
    }

    override fun onSwipeItemStarted(holder: ViewHolder, position: Int) {
        notifyDataSetChanged()
    }

    override fun onSetSwipeBackground(holder: ViewHolder, position: Int, type: Int) {
        // You can set background color/resource to holder.itemView.

        // The argument "type" can be one of the followings;
        // - Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND
        // - Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND
        // (- Swipeable.DRAWABLE_SWIPE_UP_BACKGROUND)
        // (- Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND)
        // (- Swipeable.DRAWABLE_SWIPE_DOWN_BACKGROUND)
        /*
        if (type == SwipeableItemConstants.DRAWABLE_SWIPE_LEFT_BACKGROUND) {
            holder.itemView.setBackgroundColor(Color.YELLOW)
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }
        */
        if (type == SwipeableItemConstants.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND) {
            holder.behindView.visibility = View.GONE
        } else {
            holder.behindView.visibility = View.VISIBLE
        }
    }

    /*
    // Использование ItemTouchHelper для перетаскивания пунктов списка и свайпа для удаления
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(subscripts, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(subscripts, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        subscripts.removeAt(position)
        notifyItemRemoved(position)
    }
    */

    /*
    -- Parcelable interface implementation (для того, чтобы сохранять адаптер в savedInstanceState)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isFirst) 1 else 0)
        parcel.writeInt(pinnedPosition)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubscrAdapter> {
        override fun createFromParcel(parcel: Parcel): SubscrAdapter {
            return SubscrAdapter(parcel)
        }

        override fun newArray(size: Int): Array<SubscrAdapter?> {
            return arrayOfNulls(size)
        }
    }
    */

}

