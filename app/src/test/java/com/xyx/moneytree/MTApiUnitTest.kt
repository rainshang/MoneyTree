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
import java.io.File


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
        server.enqueue(MockResponse().apply {
            val uri = javaClass.classLoader!!.getResource("accounts.json")
            val file = File(uri.path)
            setBody(String(file.readBytes()))
        })

        val mtApi = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(MTApi::class.java)
        val accountWrapper = mtApi.getAccounts(0).execute().body()
        assertNotNull(accountWrapper!!.data)
        assertEquals(accountWrapper.data.size, 3)
    }

}