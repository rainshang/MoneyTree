package com.xyx.moneytree.data.cache

import androidx.annotation.WorkerThread
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.xyx.moneytree.data.api.ResponseListWrapper
import com.xyx.moneytree.vo.Account
import com.xyx.moneytree.vo.Transaction
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter

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
        } catch (e: Exception) {
            when (e) {
                is JsonIOException,
                is JsonSyntaxException,
                is FileNotFoundException -> null
                else -> throw e
            }
        }
    }

    @WorkerThread
    @Throws(JsonIOException::class)
    fun saveAccounts(accountsWrapper: ResponseListWrapper<Account>) {
        gson.toJson(accountsWrapper, FileWriter(cacheDir + ACCOUNT_FILE))
    }


    @WorkerThread
    fun getTransactions(uid: Int): ResponseListWrapper<Transaction>? {
        return try {
            gson.fromJson<ResponseListWrapper<Transaction>>(
                FileReader(cacheDir + TRANSACTION_FILE.format(uid)),
                object : TypeToken<ResponseListWrapper<Transaction>>() {}.type
            )
        } catch (e: Exception) {
            when (e) {
                is JsonIOException,
                is JsonSyntaxException -> null
                else -> throw e
            }
        }
    }
}