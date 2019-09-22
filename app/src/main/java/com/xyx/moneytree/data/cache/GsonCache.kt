package com.xyx.moneytree.data.cache

import android.content.Context
import androidx.annotation.WorkerThread
import com.xyx.moneytree.data.vo.Account
import com.xyx.moneytree.data.vo.Transaction
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.xyx.moneytree.data.api.ResponseListWrapper
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.Type

object GsonCache {
    private const val ACCOUNT_FILE = "accounts.json"
    private const val TRANSACTION_FILE = "transactions_%d.json"

    private val gson = Gson()
    private lateinit var cacheDir: String

    fun initWithContext(context: Context) {
        cacheDir = context.cacheDir.absolutePath + File.separator
    }

    fun <T> getResponseListWrapperType(): Type =
        object : TypeToken<ResponseListWrapper<T>>() {}.type

    @WorkerThread
    fun getAccounts(): ResponseListWrapper<Account>? {
        return try {
            gson.fromJson<ResponseListWrapper<Account>>(
                FileReader(cacheDir + ACCOUNT_FILE),
                getResponseListWrapperType<Account>()
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
                getResponseListWrapperType<Transaction>()
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
    fun saveTransactions(uid: Int, accountsWrapper: ResponseListWrapper<Transaction>) {
        gson.toJson(accountsWrapper, FileWriter(cacheDir + TRANSACTION_FILE.format(uid)))
    }
}