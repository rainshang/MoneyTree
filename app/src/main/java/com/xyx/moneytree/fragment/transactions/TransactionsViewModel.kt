package com.xyx.moneytree.fragment.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.xyx.moneytree.fragment.BaseViewModel
import com.xyx.moneytree.fragment.EmptyEvent
import com.xyx.moneytree.data.MTRepo
import com.xyx.moneytree.data.vo.Account
import com.xyx.moneytree.data.vo.Transaction

class TransactionsViewModel(val account: Account) : BaseViewModel() {

    private val _transactionsEvent = MutableLiveData<EmptyEvent>()
    val transactions: LiveData<List<Transaction>> =
        Transformations.switchMap(_transactionsEvent) { MTRepo.getTransactionList(account.id) }

    fun getTransactions() {
        _isLoading.value = true
        _transactionsEvent.value = EmptyEvent()
    }
}
