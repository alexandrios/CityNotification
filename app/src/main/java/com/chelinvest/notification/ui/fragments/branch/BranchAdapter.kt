package com.chelinvest.notification.ui.fragments.branch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chelinvest.notification.R
import com.chelinvest.notification.model.ObjParam
import com.chelinvest.notification.ui.custom.CardItem
import com.chelinvest.notification.utils.Constants.LIMIT_VALUE
import kotlinx.android.synthetic.main.card_item_layout.view.*

class BranchAdapter(private val branches: ArrayList<ObjParam>,
                    val onBranchClick: (ObjParam) -> Unit
)
    : RecyclerView.Adapter<BranchAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val holdId = view.findViewById<TextView>(R.id.branchIdTextView)
        //val holdName = view.findViewById<TextView>(R.id.branchNameTextView)
        //val holdValue = view.findViewById<TextView>(R.id.branchValueTextView)
        //val clickableLayout = view.findViewById<View>(R.id.)
        val clickableLayout = view.findViewById<CardItem>(R.id.cardItem)
        val cardItem = view.findViewById<CardItem>(R.id.cardItem)
    }

    override fun getItemCount(): Int {
        return if (branches.size == 0)
            0
        else
            branches.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)     {
        //holder.holdId.text = branches.get(position).id
        //holder.holdName.text = branches.get(position).name
        //holder.holdValue.text = branches.get(position).value
        holder.cardItem.text_view.text = branches.get(position).name
        if (branches[position].value == LIMIT_VALUE)
            holder.cardItem.background_image_view.setImageResource(R.drawable.background_money_color)
        else
            holder.cardItem.background_image_view.setImageResource(R.drawable.background_calculator_color)

        holder.clickableLayout.setOnClickListener {
            onBranchClick(branches[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_branch, parent, false)
        return ViewHolder(view)
    }

}