package com.xyx.moneytree

import com.xyx.moneytree.data.MTRepo
import com.xyx.moneytree.data.api.MTApi
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.memberProperties


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
        val uri = javaClass.classLoader!!.getResource("accounts.json")
        val file = File(uri.path)
        println(String(file.readBytes()))
    }

}