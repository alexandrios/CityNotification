package com.chelinvest.notification.ui.fragments.subscr.dialog

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
        val holdName: TextView = view.findViewById(R.id.subscriptNameTextView)
    }

    fun updateList(list: ArrayList<ObjParam>) {
        fieldValues = list
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return fieldValues.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)     {
        holder.holdName.text = fieldValues[position].name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_field_values, parent, false)

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