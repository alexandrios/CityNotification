package com.chelinvest.notification.presentation.screens.address.edit.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_push.*
import com.chelinvest.notification.Preferences
import com.chelinvest.notification.R
import com.chelinvest.notification.model.DeliveAddrBranch
import com.chelinvest.notification.utils.Constants.ADDRESS_DATA


class PushFragment : Fragment() {

    companion object {
        fun create() = PushFragment()
    }

    var addressData: DeliveAddrBranch? = null
    var errorMsg: (s: String) -> Unit = {}

    /*
    override fun onAttach(context: Context) {
        super.onAttach(context)
        model = (context as EditAddressFragment).model

        //errorMsg = { message: String ->
        //    (context as EditAddressActivity).showExpandableError(message)
        //}
        //TODO слабая ссылка сыпется... Пришлось пока сделать try-catch
        val thisRef = WeakReference(context)
        // анонимную ф-ю (лямбду) помещаем в переменную
        errorMsg = { message: String ->
            val handler = Handler()
            handler.postDelayed({
                val that = thisRef.get()
                if (that != null) {
                    try {
                        that.onBackPressed()
                    }
                    catch (ex: Exception) {}
                }
            }, 3000 )
            thisRef.get()?.showExpandableError(message)
        }
    }
    */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_push, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressData = arguments?.getSerializable(ADDRESS_DATA) as DeliveAddrBranch?

        if (addressData != null) {
            addressEditText.setText(addressData!!.address)
            addressTextView.text = Build.DEVICE
        } else {
            val token = Preferences.getInstance().getFCMToken(view.context)
            addressEditText.setText(token ?: "")
            addressTextView.text = Build.DEVICE; // + Build.MANUFACTURER + Build.MODEL + Build.PRODUCT + Build.BOARD

            if (token.isNullOrEmpty()) {
                // выполнение лямбды с параметром
                errorMsg("Не удалось получить токен регистрации FCM. Проверьте соединение с Интернетом.")
            }
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

}
