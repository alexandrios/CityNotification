package com.chelinvest.notification.ui.fragments.address

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chelinvest.notification.model.DeliveAddrBranch
import com.chelinvest.notification.model.DelivetypeAddrs
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder
import com.chelinvest.notification.R
import com.chelinvest.notification.utils.Constants.APP_PUSH
import com.chelinvest.notification.utils.Constants.EMAIL
import com.chelinvest.notification.utils.Constants.SMS

class AddressAdapter(
    private val moveToEditAddress: (DelivetypeAddrs, DeliveAddrBranch) -> Unit
) : AbstractExpandableItemAdapter<AddressAdapter.GroupViewHolder, AddressAdapter.ChildViewHolder>() {

    private val elements = ArrayList<DelivetypeAddrs>()

    init {
        // this is required for expandable feature
        setHasStableIds(true)
    }

    @Synchronized
    fun update(elements: List<DelivetypeAddrs>) {
        this.elements.clear()
        this.elements.addAll(elements)
    }

    override fun getGroupCount(): Int = elements.size

    override fun getChildCount(groupPosition: Int): Int {
        return elements[groupPosition].address_list.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return elements[groupPosition].id.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return elements[groupPosition].address_list[childPosition].id.toLong()
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_dtfs_group, parent, false))
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_dtfs_child, parent, false))
    }

    override fun onBindGroupViewHolder(holder: GroupViewHolder, groupPosition: Int, viewType: Int) {
        holder.bind(groupPosition)
    }

    override fun onBindChildViewHolder(holder: ChildViewHolder, groupPosition: Int, childPosition: Int, viewType: Int) {
        holder.bind(groupPosition, childPosition)
    }

    override fun onCheckCanExpandOrCollapseGroup(holder: GroupViewHolder, groupPosition: Int, x: Int, y: Int, expand: Boolean): Boolean = true

    open inner class GroupViewHolder(itemView: View) : AbstractExpandableItemViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.nameTextView)
        private val vImageView = itemView.findViewById<ImageView>(R.id.vImageView)
        private val periodImageView = itemView.findViewById<ImageView>(R.id.periodImageView)
        private val vExpandedImageView = itemView.findViewById<ImageView>(R.id.vExpandedImageView)

        open fun bind(groupPosition: Int) {
            val item: DelivetypeAddrs = elements[groupPosition]
            name.text = item.name
            periodImageView.alpha = if (item.has_send_period == "1") 1.0f else 0.0f

            val isCollapsed = expandStateFlags and ExpandableItemConstants.STATE_FLAG_IS_EXPANDED == 0
            vExpandedImageView.rotation = if (isCollapsed) 0f else 180f

            val countTextView = itemView.findViewById<TextView>(R.id.vCountTextView)
            val count = item.address_list.size
            if (count <= 0) {
                countTextView.visibility = View.VISIBLE
                countTextView.text = ""
                vExpandedImageView.visibility = View.INVISIBLE
            } else {
                countTextView.visibility = View.VISIBLE
                countTextView.text = if (count < 100) count.toString() else "99"
                vExpandedImageView.visibility = View.VISIBLE
            }

            when (item.short_name) {
                EMAIL -> vImageView.setImageResource(R.drawable.ic_email)
                SMS -> vImageView.setImageResource(R.drawable.ic_message)
                APP_PUSH -> vImageView.setImageResource(R.drawable.ic_phone)
                else -> vImageView.visibility = View.INVISIBLE
            }
        }
    }

    open inner class ChildViewHolder(itemView: View) : AbstractExpandableItemViewHolder(itemView) {
        private val addrId = itemView.findViewById<TextView>(R.id.addrIdTextView)
        private val addrDeliveTypeId = itemView.findViewById<TextView>(R.id.addrDeliveTypeIdTextView)
        private val addrAddress = itemView.findViewById<TextView>(R.id.addrAddressTextView)
        private val addrIsValid = itemView.findViewById<TextView>(R.id.addrIsValidTextView)
        private val addrIsConfirmed = itemView.findViewById<TextView>(R.id.addrIsConfirmedTextView)
        private val addrPeriod = itemView.findViewById<TextView>(R.id.addrPeriod)

        open fun bind(groupPosition: Int, childPosition: Int) {
            val item = elements[groupPosition].address_list[childPosition]
            item.apply {
                addrId.visibility = View.GONE
                addrDeliveTypeId.visibility = View.GONE

                if (elements[groupPosition].short_name == APP_PUSH) {
                    addrAddress.text = formatappPushaddress(address)
                } else {
                    addrAddress.text = address
                }

                addrIsValid.visibility = View.GONE
                addrIsConfirmed.visibility = View.GONE

                if (elements[groupPosition].has_send_period == "1") {
                    addrPeriod.visibility = View.VISIBLE
                    addrPeriod.text = formatTimePeriod(start_hour, finish_hour, timezone)
                } else {
                    addrPeriod.visibility = View.GONE
                }
            }

            val clickableLayout = itemView.findViewById<View>(R.id.vClickableLayout)
            clickableLayout.setOnClickListener {
                moveToEditAddress(elements[groupPosition], item)
            }

        }

        private fun formatTimePeriod(startHour: String, finishHour: String, timezone: String): String {
            return "с $startHour по $finishHour (UTC${if(timezone.toInt() >= 0) "+$timezone" else timezone})"
        }

        private fun formatappPushaddress(address: String): String {
            //return String.format("%s ... %s", address.substring(0, 5), address.substring(address.length - 5))
            return Build.DEVICE
        }
    }

/* Другие возможные константы класса Build:
BOARD - название основной платы, например "goldfish";
BRAND - имя бренда, например "GSmart";
CPU_ABI - название набора команд машинного кода, например "armeabi";
DEVICE - название промышленного образца, например "msm1234_sku8";
DISPLAY - идентификатор сборки, предназначенный для отображения пользователю;
FINGERPRINT - строка, которая однозначно идентифицирует это устройство;
HOST - в документации нет информации по этой константе;
ID - или номер списка изменений, при котором было выпущено устройство, или метка вроде "M4-rc20";
MANUFACTURER - производитель устройства;
MODEL - название продукта, видимое для пользователя;
PRODUCT - имя продукта;
TAGS - теги, разделенные запятыми, описывающие сборку, например, "unsigned,debug";
TIME - в документации нет информации по этой константе. Судя по всему, дата производства;
TYPE - тип сборки, например "user" или "eng";
USER - в документации нет информации по этой константе. Чаще всего возвращается строка "android".
*/


// header & footer - they are not currently used
/*
    inner class UnsHeaderGroupViewHolder(itemView: View) : GroupViewHolder(itemView) {
        override fun bind(groupPos: Int) {
            val isCollapsed = expandStateFlags and ExpandableItemConstants.STATE_FLAG_IS_EXPANDED == 0
            itemView.findViewById<View>(com.chelinvest.notification.R.id.vExpandedImageView).rotation = if (isCollapsed) 0f else 180f

            val uns = elements[groupPos / 2]

            val addressCount = HashMap<DeliveAddrBranch?, Int>()
            uns.address_list.forEach { unc ->
                //val address = unc.address
                val count = addressCount[unc] ?: 0
                addressCount[unc] = count + 1
            }
            val addressCountList = addressCount.entries.toMutableList()
            addressCountList.sortWith(Comparator { o1, o2 ->
                if (o1.key == null)
                    return@Comparator 1
                if (o2.key == null)
                    return@Comparator -1
                o2.value.compareTo(o1.value)
            })

            fun entryToPair(entry: Map.Entry<DeliveAddrBranch?, Int>) = Pair(entry.key, entry.value)

            fun formatAddress(pair: Pair<DeliveAddrBranch?, Int>, isSingle: Boolean): Triple<String, Int, Int> {
                val address = pair.first
                val count = pair.second
                val text = if (address == null)
                    "${if (isSingle) "Услуг" else "Ещё"} - "
                else
                    "${if (isSingle) "Услуг" else "Ещё"} - "
                    //"${address.formatUncAddress()} - "
                val text2 = "$text$count шт."
                return Triple(text2, text.length, text2.length)
            }

            itemView.findViewById<TextView>(com.chelinvest.notification.R.id.vAddressesTextView).text = when (addressCountList.size) {
                0 -> ""
                1 -> {
                    val triple = formatAddress(entryToPair(addressCountList[0]), true)
                    val spannableString = SpannableString(triple.first)
                    spannableString.setSpan(
                        StyleSpan(Typeface.BOLD), triple.second, triple.third,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    spannableString
                }
                2 -> {
                    val triple1 = formatAddress(entryToPair(addressCountList[0]), false)
                    val triple2 = formatAddress(entryToPair(addressCountList[1]), false)

                    val spannableString = SpannableString("${triple1.first} ${triple2.first}")
                    spannableString.setSpan(
                        StyleSpan(Typeface.BOLD), triple1.second,
                        triple1.third, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    val startPos = triple1.first.length + 1
                    spannableString.setSpan(
                        StyleSpan(Typeface.BOLD), startPos + triple2.second,
                        startPos + triple2.third, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                    spannableString
                }
                else -> {
                    var otherCount = 0
                    for (i in 2 until addressCountList.size)
                        otherCount += addressCountList[i].value

                    val triple1 = formatAddress(entryToPair(addressCountList[0]), false)
                    val triple2 = formatAddress(entryToPair(addressCountList[1]), false)
                    val triple3 = formatAddress(Pair(null, otherCount), false)

                    val spannableString = SpannableString("${triple1.first} ${triple2.first} ${triple3.first}")
                    spannableString.setSpan(
                        StyleSpan(Typeface.BOLD), triple1.second,
                        triple1.third, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    var startPos = triple1.first.length + 1
                    spannableString.setSpan(
                        StyleSpan(Typeface.BOLD), startPos + triple2.second,
                        startPos + triple2.third, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    startPos += triple2.first.length + 1
                    spannableString.setSpan(
                        StyleSpan(Typeface.BOLD), startPos + triple3.second,
                        startPos + triple3.third, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                    spannableString
                }
            }

            itemView.findViewById<TextView>(com.chelinvest.notification.R.id.vTextView).text = "Док. №${uns.id}"
            itemView.findViewById<TextView>(com.chelinvest.notification.R.id.vDateTextView).text = /*uns.uncCheckBaseList.getMinDate()?.toString("dd.MM.yyyy") ?: ""*/ "date"
            itemView.setOnClickListener {
                collapseListener(groupPos)
            }
        }
    }

    inner class UnsFooterGroupViewHolder(itemView: View) : GroupViewHolder(itemView) {
        override fun bind(groupPos: Int) {
            val uns = elements[groupPos / 2]

            val sumTextView = itemView.findViewById<MoneyTextView>(com.chelinvest.notification.R.id.vTextView)
            if (isFilterActive) {
                var sum = 0.0
                uns.address_list.forEach { unc ->
                    sum += unc.id.toFloat()
                }
                sumTextView.amount = sum.toFloat()
                sumTextView.fullAmount = uns.id.toFloat()
            } else {
                sumTextView.amount = uns.id.toFloat()
                sumTextView.fullAmount = null
            }

            itemView.findViewById<View>(com.chelinvest.notification.R.id.vPrintButton).setOnClickListener {
                //printUns(uns)
                Toast.makeText(itemView.context, "printUns(uns)", Toast.LENGTH_SHORT).show()
            }
            itemView.setOnClickListener {
                collapseListener(groupPos)
            }
        }
    }

    private val expandableChilds = HashSet<Long>()

    inner class UncChildViewHolder(itemView: View) : ChildViewHolder(itemView) {
        override fun bind(groupPos: Int, childPos: Int) {
            val unsExp = elements[groupPos / 2]
            val uncCheckBase = unsExp.address_list[childPos]

            itemView.vNameTextView.text = uncCheckBase.address
            itemView.vSumTextView.amount = uncCheckBase.id.toFloat()

            val address = uncCheckBase.delive_type.id.toString()
            val otdel = unsExp.name

            itemView.vIdTextView.text = uncCheckBase.id
            if (address == null) {
                itemView.vAddressLayout.visibility = View.GONE
            } else {
                itemView.vAddressLayout.visibility = View.VISIBLE
                itemView.vAddressTextView.text = address
            }
            if (otdel == null) {
                itemView.vOtdelLayout.visibility = View.GONE
            } else {
                itemView.vOtdelLayout.visibility = View.VISIBLE
                itemView.vOtdelTextView.text = otdel
            }

            itemView.vClickableLayout.setOnClickListener {
                val isExpanded = !itemView.vExpandableLayout.isExpanded
                itemView.vExpandableLayout.toggle()
                itemView.vExpandedTextView.text = if (isExpanded) "Скрыть" else "Подробнее"
                if (isExpanded)
                    expandableChilds.add(itemId)
                else
                    expandableChilds.remove(itemId)
            }

            itemView.vPrintButton.setOnClickListener {
                //printTicket(uncCheckBase)
                Toast.makeText(itemView.context, "printTicket(uncCheckBase)", Toast.LENGTH_SHORT).show()
            }

            if (expandableChilds.contains(itemId))
                itemView.vExpandableLayout.expand(false)
            else
                itemView.vExpandableLayout.collapse(false)

            val isExpanded = itemView.vExpandableLayout.isExpanded
            itemView.vExpandedTextView.text = if (isExpanded) "Скрыть" else "Подробнее"
        }
    }
*/

}