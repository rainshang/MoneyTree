package com.xyx.moneytree

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MTApiUnitTest {
    private lateinit var server: MockWebServer

    @Before
    fun setup() {
        server = MockWebServer()
    }

    @After
    fun clean() {
        server.shutdown()
    }

    @Test
    fun getAccountsTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        server.enqueue(MockResponse().setBody(Mockre))
    }

}