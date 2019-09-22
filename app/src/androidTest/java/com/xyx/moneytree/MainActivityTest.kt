package com.xyx.moneytree

import ResourceUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.getmoneytree.moneytreelight.R
import com.xyx.moneytree.data.MTRepo
import com.xyx.moneytree.data.api.MTApi
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java, true, false)

    private lateinit var server: MockWebServer

    @Before
    fun setupMockServer() {
        server = MockWebServer()
        server.dispatcher = object : Dispatcher() {
            val accountRegex = """^/account[?]timestamp=([0-9]+)$""".toRegex()
            val transactionRegex =
                """^/account/([0-9]+)/transaction[?]timestamp=([0-9]+)$""".toRegex()

            override fun dispatch(request: RecordedRequest): MockResponse {
                return request.path?.let { path ->
                    accountRegex.matchEntire(path)?.let {
                        MockResponse().apply {
                            setBodyDelay(2, TimeUnit.SECONDS)
                            setBody(ResourceUtil.getAccounts() ?: "")
                        }
                    } ?: run {
                        transactionRegex.matchEntire(path)?.let {
                            MockResponse().apply {
                                setBodyDelay(2, TimeUnit.SECONDS)
                                setBody(
                                    ResourceUtil.getTransactions(it.groups[1]!!.value.toInt()) ?: ""
                                )
                            }
                        } ?: run {
                            MockResponse()
                        }
                    }
                } ?: run {
                    MockResponse()
                }
            }
        }
        server.start()
    }

    @After
    fun clean() {
        server.shutdown()
    }

    @Test
    fun getAccountsAndDisplayTest() {
        MTRepo.mtApi = MTApi.getInstance(server.url("").toString())
        activityRule.launchActivity(null)
        Thread.sleep(3000)
        onView(ViewMatchers.withText("JPY5,341"))
            .check(ViewAssertions.matches(isDisplayed()))
        onView(ViewMatchers.withText("マークからカード"))
            .check(ViewAssertions.matches(isDisplayed()))
//        while (true) {
//        }
    }

    @Test
    fun clickListGotoTransactionTest() {
        getAccountsAndDisplayTest()
        onView(ViewMatchers.withId(R.id.rv_accounts))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    click()
                )
            )
        Thread.sleep(3000)
        onView(ViewMatchers.withText("JPY-1,305"))
            .check(ViewAssertions.matches(isDisplayed()))
//        while (true) {
//        }
    }

}