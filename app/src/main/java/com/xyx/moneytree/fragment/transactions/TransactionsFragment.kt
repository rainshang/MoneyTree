package com.xyx.moneytree.fragment.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.getmoneytree.moneytreelight.R
import com.getmoneytree.moneytreelight.databinding.LayoutItemTransactionBinding
import com.getmoneytree.moneytreelight.databinding.LayoutItemTransactionHeaderBinding
import com.getmoneytree.moneytreelight.databinding.TransactionsFragmentBinding
import com.xyx.moneytree.AppExecutors
import com.xyx.moneytree.data.vo.Transaction
import com.xyx.moneytree.fragment.DataBindingVH
import kotlinx.android.synthetic.main.accounts_fragment.toolbar
import kotlinx.android.synthetic.main.layout_item_transaction.view.*
import kotlinx.android.synthetic.main.transactions_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class TransactionsFragment : Fragment() {

    private val args: TransactionsFragmentArgs by navArgs()
    private lateinit var viewModel: TransactionsViewModel
    private val adapter = Adapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.transactions_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar.apply {
            title = args.account.institution
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        rv_transactions.adapter = adapter

        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TransactionsViewModel(args.account) as T
            }
        }).get(TransactionsViewModel::class.java).apply {
            DataBindingUtil.bind<TransactionsFragmentBinding>(view)!!.also {
                it.lifecycleOwner = viewLifecycleOwner
                it.transactionViewModel = this
            }
        }
        viewModel.transactions.observe(viewLifecycleOwner, Observer {
            viewModel.setNotLoading()
            it?.run {
                adapter.setData(this)
            }
        })
        viewModel.getTransactions()
    }

}

class Adapter : RecyclerView.Adapter<DataBindingVH<ViewDataBinding>>() {

    val items = ArrayList<Any>()

    class Group(val name: String) {
        var positiveAmount = 0.0
        var negativeAmount = 0.0
        val transactions = ArrayList<Transaction>()
    }

    fun setData(transactions: List<Transaction>) {
        AppExecutors.computing.execute {
            val parseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val headerFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())

            transactions.forEach {
                it.calendar = Calendar.getInstance().apply { time = parseFormat.parse(it.date)!! }
            }
            transactions.sortedBy { it.calendar }

            val map = LinkedHashMap<String, Group>()
            transactions.forEach { trans ->
                val headerName = headerFormat.format(trans.calendar!!.time)
                map[headerName]?.transactions?.add(trans) ?: run {
                    map[headerName] =
                        Group(headerName).also { newGroup -> newGroup.transactions.add(trans) }
                }
                map[headerName]!!.apply {
                    if (trans.amount > 0) {
                        positiveAmount += trans.amount
                    } else {
                        negativeAmount += trans.amount
                    }
                }
            }
            val groups = ArrayList(map.values)
            val tmpItems = ArrayList<Any>()
            groups.forEach { group ->
                tmpItems.add(group)
                group.transactions.forEach { trans -> tmpItems.add(trans) }
                group.transactions.clear()
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
        is Transaction -> 1
        else -> -1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBindingVH<ViewDataBinding> {
        val binding = when (viewType) {
            0 -> DataBindingUtil.inflate<LayoutItemTransactionHeaderBinding>(
                LayoutInflater.from(parent.context),
                R.layout.layout_item_transaction_header,
                parent,
                false
            )
            1 -> DataBindingUtil.inflate<LayoutItemTransactionBinding>(
                LayoutInflater.from(parent.context),
                R.layout.layout_item_transaction,
                parent,
                false
            ).apply {
                root.setOnClickListener {
                    println()
                }
            }
            else -> throw Exception("not expected viewholder type: $viewType")
        }
        return DataBindingVH(binding)
    }

    override fun onBindViewHolder(holder: DataBindingVH<ViewDataBinding>, position: Int) {
        when (holder.binding) {
            is LayoutItemTransactionHeaderBinding -> holder.binding.group =
                items[position] as Group?
            is LayoutItemTransactionBinding -> {
                holder.binding.transaction =
                    items[position] as Transaction?
                val day =
                    holder.binding.transaction!!.calendar!!.get(Calendar.DAY_OF_MONTH)
                holder.binding.root.date.text = when (day % 10) {
                    1 -> "${day}st"
                    2 -> "${day}nd"
                    3 -> "${day}rd"
                    else -> "${day}th"
                }
            }
        }
        holder.binding.executePendingBindings()
    }

}
