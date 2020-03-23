package com.chelinvest.notification.ui.subscr.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chelinvest.notification.R
import com.chelinvest.notification.model.ObjParam

class FieldValuesAdapter(
    private var fieldValues: ArrayList<ObjParam>,
    private val onSelected: (selectedElement: ObjParam) -> Unit
) : RecyclerView.Adapter<FieldValuesAdapter.ViewHolder>() {

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val holdId = view.findViewById<TextView>(R.id.subscriptIdTextView)
        val holdName = view.findViewById<TextView>(R.id.subscriptNameTextView)
        //val holdValue = view.findViewById<TextView>(R.id.subscriptValueTextView)
    }

    fun updateList(list: ArrayList<ObjParam>) {
        fieldValues = list
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return fieldValues.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)     {
        //holder.holdId.text = fieldValues[position].id
        holder.holdName.text = fieldValues[position].name
        //holder.holdValue.text = fieldValues[position].value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_field_values, parent, false)

        //view.subscriptImageView.
        return ViewHolder(view).listener { pos ->
            onSelected(fieldValues[pos])
        }
    }

    private fun <T : RecyclerView.ViewHolder> T.listener(event: (Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(adapterPosition)
        }

        return this
    }

}