package com.xyx.moneytree.data.api

import com.xyx.moneytree.BuildConfig
import com.xyx.moneytree.vo.Account
import com.xyx.moneytree.vo.Transaction
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface MTApi {

    companion object Factory {
        private const val MT_SERVER = "https://api.moneytree.mock/"

        // Theoretically, object is a language-level singleton. Well since Google recommends to do like this...
        private val apiMap = HashMap<String, MTApi>()

        @Synchronized
        fun getInstance(baseUrl: String = MT_SERVER): MTApi {
            apiMap[baseUrl] ?: synchronized(this) {
                apiMap[baseUrl] = buildRetrofit(baseUrl).create(MTApi::class.java)
            }
            return apiMap[baseUrl]!!
        }

        private fun buildRetrofit(baseUrl: String) = Retrofit.Builder()
            .baseUrl(baseUrl)
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
    }

    @GET("account/{timestamp}")
    fun getAccounts(@Path("timestamp") timestamp: Int): Call<ResponseListWrapper<Account>>

    @GET("account/{id}/transaction/{timestamp}")
    fun getTransactions(@Path("id") id: Int): Call<ResponseListWrapper<Transaction>>

}