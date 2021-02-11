package com.chelinvest.notification.ui.fragments.address.edit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.chelinvest.notification.R
import com.chelinvest.notification.model.DeliveAddrBranch
import com.chelinvest.notification.ui.custom.stylable.CustomTextView
import com.chelinvest.notification.utils.Constants.ADDRESS_DATA
import com.chelinvest.notification.utils.Constants.DELIVE_NAME

class EmailFragment : Fragment() {

    companion object {
        fun create() = EmailFragment()
    }

    var addressData: DeliveAddrBranch? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_email, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressData = arguments?.getSerializable(ADDRESS_DATA) as DeliveAddrBranch?

        val addressEditText = view.findViewById<EditText>(R.id.addressEditText)
        if (addressData != null) {
            addressEditText.setText(addressData!!.address)
        }

        view.findViewById<CustomTextView>(R.id.addressTextView).text = arguments?.getString(DELIVE_NAME) ?:
            resources.getString(R.string.edit_email_descr_display)
    }

}
