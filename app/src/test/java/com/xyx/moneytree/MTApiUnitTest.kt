package com.xyx.moneytree

import ResourceUtil
import com.xyx.moneytree.data.api.MTApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

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
        Assert.assertNotNull(accountsWrapper!!.data)
        Assert.assertEquals(accountsWrapper.data.size, 3)
    }
}