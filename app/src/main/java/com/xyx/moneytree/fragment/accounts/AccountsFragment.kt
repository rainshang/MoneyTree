package com.xyx.moneytree.fragment.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.xyx.moneytree.R
import com.xyx.moneytree.data.MTRepo
import com.xyx.moneytree.vo.Account
import kotlinx.android.synthetic.main.accounts_fragment.*
import java.text.DecimalFormat

class AccountsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.accounts_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.apply {
            title = getString(R.string.app_name)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        MTRepo.getAccountList()
            .observe(this, Observer { it?.run { updateUI(this) } })
    }

    private fun updateUI(accounts: List<Account>) {
        val s =
            "JPY${DecimalFormat("#,###").format(accounts.sumByDouble { it.currentBalanceInBase })}"
        tv_total_count.text = s
    }

}
