package com.chelinvest.notification.presentation.screens.address.edit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.ADDRESS_DATA
import com.chelinvest.notification.model.DeliveAddrBranch
import com.chelinvest.notification.ui.custom.ModifiedEditText


class SmsFragment : Fragment() {

    companion object {
        fun create() = SmsFragment()
    }

    var addressData: DeliveAddrBranch? = null

    /*
    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = (context as EditAddressFragment).model
    }
     */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_sms, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressData = arguments?.getSerializable(ADDRESS_DATA) as DeliveAddrBranch?

        val addressEditText = view.findViewById<ModifiedEditText>(R.id.addressEditText)
        if (addressData != null) {
            addressEditText.setText(addressData!!.address)
        } else {
            addressEditText.setText("+7")
        }
    }

}
