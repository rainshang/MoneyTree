package com.xyx.moneytree.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xyx.moneytree.AppExecutors
import com.xyx.moneytree.data.api.MTApi
import com.xyx.moneytree.data.cache.GsonCache
import com.xyx.moneytree.vo.Account
import java.io.IOException

object MTRepo {
    var mtApi = MTApi.getInstance()

    fun getAccountList(): LiveData<List<Account>> {
        val mLiveData = MutableLiveData<List<Account>>()
        val fetchNetwork = { timestamp: Int ->
            AppExecutors.networkIO.execute {
                try {
                    mtApi.getAccounts(timestamp).execute().body()?.run {
                        mLiveData.postValue(data)
                        GsonCache.saveAccounts(this)
                    } ?: run {
                        mLiveData.postValue(null)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
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