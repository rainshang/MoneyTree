package com.xyx.moneytree.data.cache

import androidx.annotation.WorkerThread
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xyx.moneytree.data.api.ResponseListWrapper
import com.xyx.moneytree.vo.Account
import com.xyx.moneytree.vo.Transaction
import java.io.FileReader
import java.io.IOException

object GsonCache {
    private const val ACCOUNT_FILE = "accounts.json"
    private const val TRANSACTION_FILE = "transactions_%d.json"

    private val gson = Gson()
    lateinit var cacheDir: String

    @WorkerThread
    fun getAccounts(): ResponseListWrapper<Account>? {
        return try {
            gson.fromJson<ResponseListWrapper<Account>>(
                FileReader(cacheDir + ACCOUNT_FILE),
                object : TypeToken<ResponseListWrapper<Account>>() {}.type
            )
        } catch (e: IOException) {
            null
        }
    }


    @WorkerThread
    fun getTransactions(uid: Int): ResponseListWrapper<Transaction>? {
        return try {
            gson.fromJson<ResponseListWrapper<Transaction>>(
                FileReader(cacheDir + TRANSACTION_FILE.format(uid)),
                object : TypeToken<ResponseListWrapper<Transaction>>() {}.type
            )
        } catch (e: Exception) {
            null
        }
    }
}