package com.xyx.moneytree.data.api

import com.xyx.moneytree.vo.Account
import com.xyx.moneytree.vo.Transaction
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MTApi {

    @GET("account/{timestamp}")
    fun getAccounts(@Path("timestamp") timestamp: Int): Call<ResponseListWrapper<Account>>

    @GET("account/{id}/transaction/{timestamp}")
    fun getTransactions(@Path("id") id: Int): Call<ResponseListWrapper<Transaction>>

}