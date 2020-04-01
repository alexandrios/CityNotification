package com.chelinvest.notification.ui.address.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chelinvest.notification.model.DelivetypeAddrs
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.APP_PUSH
import com.chelinvest.notification.additional.EMAIL
import com.chelinvest.notification.additional.SMS

class DelivetypeSubscrAdapter(
    private val delivetypeAddrs: ArrayList<DelivetypeAddrs>,
    private val onSelected: (pos: Int) -> Unit
) : RecyclerView.Adapter<DelivetypeSubscrAdapter.ViewHolder>() {

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val holdId = view.findViewById<TextView>(R.id.dtsIdTextView)
        val holdName = view.findViewById<TextView>(R.id.dtsNameTextView)
        val holdShort = view.findViewById<TextView>(R.id.dtsShortTextView)
        val holdValue = view.findViewById<TextView>(R.id.dtsValueTextView)
        val holdHasSendPeriod = view.findViewById<TextView>(R.id.dtsHasSendPeriodTextView)
        val holdImage = view.findViewById<ImageView>(R.id.dtsImageView)
    }

    override fun getItemCount(): Int {
        return delivetypeAddrs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)     {
        holder.holdId.text = delivetypeAddrs[position].id
        holder.holdName.text = delivetypeAddrs[position].name
        holder.holdShort.text = delivetypeAddrs[position].short_name
        holder.holdValue.text = delivetypeAddrs[position].value_name
        holder.holdHasSendPeriod.text = delivetypeAddrs[position].has_send_period
        holder.holdImage.setImageResource(
            when (delivetypeAddrs[position].short_name) {
                EMAIL -> R.drawable.ic_email
                SMS -> R.drawable.outline_sms_black_18dp
                APP_PUSH -> R.drawable.ic_phone
                else -> R.drawable.ic_email
            }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_delivetype_addrs, parent, false)

        return ViewHolder(view).listener { pos ->
            onSelected(pos)
        }
    }

    private fun <T : RecyclerView.ViewHolder> T.listener(event: (Int) -> Unit): T {

        itemView.setOnClickListener {
            event.invoke(adapterPosition)
        }

        return this
    }
}