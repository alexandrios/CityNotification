package com.chelinvest.notification.ui.address.edit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.ADDRESS_DATA
import com.chelinvest.notification.model.DeliveAddrBranch
import com.chelinvest.notification.ui.custom.ModifiedEditText


class EmailFragment : Fragment() {

    companion object {
        fun create() = EmailFragment()
    }

    var addressData: DeliveAddrBranch? = null

    /*
    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = (context as EditAddressActivity).model
    }
    */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_email, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressData = arguments?.getSerializable(ADDRESS_DATA) as DeliveAddrBranch?

        val addressEditText = view.findViewById<ModifiedEditText>(R.id.addressEditText)
        if (addressData != null) {
            addressEditText.setText(addressData!!.address)
        }
    }

}
