package com.chelinvest.notification.ui.limit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chelinvest.notification.R
import com.chelinvest.notification.additional.BRANCH_NAME
import com.chelinvest.notification.ui.CustomFragment
import kotlinx.android.synthetic.main.fragment_limit.*
import kotlinx.android.synthetic.main.fragment_limit.branchNameTextView
import kotlinx.android.synthetic.main.fragment_limit.vAddButton
import kotlinx.android.synthetic.main.fragment_limit.vBackButton


class LimitFragment : CustomFragment<LimitPresenter>(), ILimitView {

    companion object {
        fun create() = LimitFragment()
    }

    override fun createPresenter(): LimitPresenter = LimitPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_limit, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vBackButton.setOnClickListener { findNavController().popBackStack() }
        vAddButton.visibility = View.INVISIBLE

        val nameBranch = arguments?.getString(BRANCH_NAME)
        branchNameTextView.setText(nameBranch)

        getPresenter().getAgentLimit(view.context, this) { orgName, limit ->
            limit?.let {
                agentTextView.text = orgName?.name ?: "Агент"
                limitTextView.text = String.format("%,18.2f руб.", it.toDoubleOrNull() ?: "").trim() //.replace(',', ' ')
            }
        }
    }

}


