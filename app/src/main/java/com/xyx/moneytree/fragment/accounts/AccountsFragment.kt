package com.xyx.moneytree.fragment.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.getmoneytree.moneytreelight.R
import com.getmoneytree.moneytreelight.databinding.AccountsFragmentBinding
import com.getmoneytree.moneytreelight.databinding.LayoutItemAccountBinding
import com.getmoneytree.moneytreelight.databinding.LayoutItemAccountHeaderBinding
import com.xyx.moneytree.AppExecutors
import com.xyx.moneytree.data.vo.Account
import com.xyx.moneytree.fragment.AmountUtil
import com.xyx.moneytree.fragment.DataBindingVH
import kotlinx.android.synthetic.main.accounts_fragment.*

class AccountsFragment : Fragment() {

    private lateinit var viewModel: AccountsViewModel
    private val adapter = Adapter()

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
        rv_accounts.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(AccountsViewModel::class.java).apply {
            DataBindingUtil.bind<AccountsFragmentBinding>(view)!!.also {
                it.lifecycleOwner = viewLifecycleOwner
                it.accountsViewModel = this
            }
            accounts.observe(viewLifecycleOwner,
                Observer {
                    viewModel.setNotLoading()
                    it?.run { updateUI(this@run) }
                })
            getAccounts()
        }
    }

    private fun updateUI(accounts: List<Account>) {
        tv_total_count.text = getString(
            R.string.jpy_format,
            AmountUtil.formatAmount(accounts.sumByDouble { it.currentBalanceInBase })
        )
        adapter.setAccountData(accounts)
    }
}

class Adapter : RecyclerView.Adapter<DataBindingVH<ViewDataBinding>>() {

    val items = ArrayList<Any>()

    class Group(val name: String) {
        val accounts = ArrayList<Account>()
    }

    fun setAccountData(accounts: List<Account>) {
        AppExecutors.computing.execute {
            accounts.sortedBy { it.name }
            val map = HashMap<String, Group>()
            accounts.forEach {
                map[it.institution]?.accounts?.add(it) ?: run {
                    map[it.institution] =
                        Group(it.institution).also { newGroup -> newGroup.accounts.add(it) }
                }
            }
            val groups = ArrayList(map.values)
            groups.sortBy { it.name }
            val tmpItems = ArrayList<Any>()
            groups.forEach { group ->
                tmpItems.add(group)
                group.accounts.forEach { account -> tmpItems.add(account) }
                group.accounts.clear()
            }
            AppExecutors.mainThread.execute {
                items.clear()
                items.addAll(tmpItems)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = when (items[position]) {
        is Group -> 0
        is Account -> 1
        else -> -1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBindingVH<ViewDataBinding> {
        val binding = when (viewType) {
            0 -> DataBindingUtil.inflate<LayoutItemAccountHeaderBinding>(
                LayoutInflater.from(parent.context),
                R.layout.layout_item_account_header,
                parent,
                false
            )
            1 -> DataBindingUtil.inflate<LayoutItemAccountBinding>(
                LayoutInflater.from(parent.context),
                R.layout.layout_item_account,
                parent,
                false
            ).apply {
                root.setOnClickListener {
                    val direction =
                        AccountsFragmentDirections.actionAccountsFragmentToTransactionsFragment(
                            account!!
                        )
                    it.findNavController().navigate(direction)
                }
            }
            else -> throw Exception("not expected viewholder type: $viewType")
        }
        return DataBindingVH(binding)
    }

    override fun onBindViewHolder(holder: DataBindingVH<ViewDataBinding>, position: Int) {
        when (holder.binding) {
            is LayoutItemAccountHeaderBinding -> holder.binding.group = items[position] as Group?
            is LayoutItemAccountBinding -> holder.binding.account = items[position] as Account?
        }
        holder.binding.executePendingBindings()
    }

}
