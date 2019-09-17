package com.xyx.moneytree.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xyx.moneytree.AppExecutors
import com.xyx.moneytree.BuildConfig
import com.xyx.moneytree.data.api.MTApi
import com.xyx.moneytree.data.cache.GsonCache
import com.xyx.moneytree.vo.Account
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException

object MTRepo {
    private const val MT_SERVER = "https://api.moneytree.mock/"

    private val mtApi: MTApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(MT_SERVER)
            .client(
                OkHttpClient.Builder()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            addInterceptor(HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            })
                        }
                    }.build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mtApi = retrofit.create(MTApi::class.java)
    }

    fun initWithContext(context: Context) {
        GsonCache.cacheDir = context.cacheDir.absolutePath + File.separator
    }

    fun getAccountList(): LiveData<List<Account>> {
        val mLiveData = MutableLiveData<List<Account>>()
        val fetchNetwork = { timestamp: Int ->
            AppExecutors.networkIO.execute {
                try {
                    mtApi.getAccounts(timestamp).execute().body()?.run {
                        mLiveData.postValue(data)
                        // TODO cache it
                    } ?: run {
                        mLiveData.postValue(null)
                    }
                } catch (e: IOException) {
                    mLiveData.postValue(null)
                }
            }
        }
        AppExecutors.diskIO.execute {
            GsonCache.getAccounts()?.run {
                mLiveData.postValue(data)
                fetchNetwork(timestamp)
            } ?: run {
                fetchNetwork(0)
            }
        }
        return mLiveData
    }
}