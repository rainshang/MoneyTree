package com.xyx.moneytree.fragment.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.xyx.moneytree.data.MTRepo
import com.xyx.moneytree.data.vo.Account
import com.xyx.moneytree.fragment.BaseViewModel
import com.xyx.moneytree.fragment.EmptyEvent

class AccountsViewModel : BaseViewModel() {

    private val _accountsEvent = MutableLiveData<EmptyEvent>()
    val accounts: LiveData<List<Account>> =
        Transformations.switchMap(_accountsEvent) { MTRepo.getAccountList() }

    fun getAccounts() {
        _isLoading.value = true
        _accountsEvent.value = EmptyEvent()
    }
}