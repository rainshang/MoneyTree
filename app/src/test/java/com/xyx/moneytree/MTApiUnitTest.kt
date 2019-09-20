package com.xyx.moneytree

import com.xyx.moneytree.data.api.MTApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MTApiUnitTest {
    private lateinit var server: MockWebServer

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun clean() {
        server.shutdown()
    }

    @Test
    fun getAccountsTest() {
        server.enqueue(MockResponse().apply { setBody(ResourceUtil.getAccounts() ?: "") })

        val mtApi = MTApi.getInstance(server.url("").toString())
        val accountsWrapper = mtApi.getAccounts(0).execute().body()
        assertNotNull(accountsWrapper!!.data)
        assertEquals(accountsWrapper.data.size, 3)
    }

}