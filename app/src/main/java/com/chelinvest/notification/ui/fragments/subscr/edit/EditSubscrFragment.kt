package com.chelinvest.notification.ui.fragments.subscr.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chelinvest.notification.R
import com.chelinvest.notification.model.DeliveSubscriptionForBranch
import com.chelinvest.notification.ui.CustomFragment
import com.chelinvest.notification.ui.fragments.subscr.ISubscrView
import com.chelinvest.notification.ui.fragments.subscr.SubscrViewModel
import com.chelinvest.notification.utils.Constants.SUBSCR_INFO
import kotlinx.android.synthetic.main.fragment_edit_subscr.*


class EditSubscrFragment : CustomFragment<EditSubscrPresenter>(), ISubscrView {

    lateinit var id: String

    private val model: SubscrViewModel by activityViewModels()

    /*
    companion object {
        fun getStartIntent(context: Context, subscrInfo: DeliveSubscriptionForBranch): Intent {
            return Intent(context, EditSubscrFragment::class.java)
                .putExtra(SUBSCR_INFO, subscrInfo)
        }
    }
     */

    override fun createPresenter(): EditSubscrPresenter = EditSubscrPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_edit_subscr, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val subscrInfo =  arguments?.getSerializable(SUBSCR_INFO)

        id = (subscrInfo as DeliveSubscriptionForBranch).id
        descriptEditText.setText(subscrInfo.name)
        activeSwitch.isChecked = subscrInfo.value == "Y"

        backImageView.setOnClickListener {
            Log.wtf("EDITSUBSCRFRAGMENT", "model.save(false)")
            model.setEditSave(false)
            findNavController().popBackStack()
        }

        saveTextView.setOnClickListener {
            // Выполнить команду 1.7. update_delivery_subscription_for_branch
            getPresenter().updateSubscr(view.context, this) { list: ArrayList<DeliveSubscriptionForBranch> ->
                Log.wtf("EDITSUBSCRFRAGMENT", "list.size=" + list.size)
                if (list.size > 0) {
                    Log.wtf("EDITSUBSCRFRAGMENT", "model.save(true)")
                    model.setEditSave(true)
                }
                findNavController().popBackStack()
            }
        }
    }

}
