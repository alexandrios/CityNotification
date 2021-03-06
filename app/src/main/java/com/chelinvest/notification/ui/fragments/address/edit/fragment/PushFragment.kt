package com.chelinvest.notification.ui.fragments.address.edit.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.chelinvest.notification.R
import com.chelinvest.notification.model.DeliveAddrBranch
import com.chelinvest.notification.utils.Constants.ADDRESS_DATA
import com.chelinvest.notification.utils.Constants.ADDRESS_FCM_TOKEN
import com.chelinvest.notification.utils.Constants.DELIVE_NAME

class PushFragment : Fragment() {

    companion object {
        fun create() = PushFragment()
    }

    private var addressData: DeliveAddrBranch? = null
    var errorMsg: (s: String) -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_push, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressData = arguments?.getSerializable(ADDRESS_DATA) as DeliveAddrBranch?

        if (addressData != null) {
            view.findViewById<EditText>(R.id.addressEditText).setText(addressData!!.address)
            view.findViewById<TextView>(R.id.nameTextView).text = Build.DEVICE
        } else {
            val token = arguments?.getSerializable(ADDRESS_FCM_TOKEN) as String?
            view.findViewById<EditText>(R.id.addressEditText).setText(token ?: "")
            view.findViewById<TextView>(R.id.nameTextView).text = Build.DEVICE // + Build.MANUFACTURER + Build.MODEL + Build.PRODUCT + Build.BOARD

            if (token.isNullOrEmpty()) {
                // выполнение лямбды с параметром
                errorMsg("Не удалось получить токен регистрации FCM. Проверьте соединение с Интернетом.")
            }
        }

        view.findViewById<TextView>(R.id.addressTextView).text = arguments?.getString(DELIVE_NAME) ?: resources.getString(R.string.edit_push_descr_display)
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

}
