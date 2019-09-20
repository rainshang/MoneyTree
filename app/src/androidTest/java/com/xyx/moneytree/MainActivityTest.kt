package com.xyx.moneytree

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.xyx.moneytree.data.MTRepo
import com.xyx.moneytree.data.api.MTApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java, false, false)

    private lateinit var server: MockWebServer

    @Before
    fun setupMockServer() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun clean() {
        server.shutdown()
    }

    @Test
    fun getAccountsAndDisplayTest() {
        server.enqueue(MockResponse().apply { setBody(ResourceUtil.getAccounts() ?: "") })
        MTRepo.mtApi = MTApi.getInstance(server.url("").toString())
        activityRule.launchActivity(null)
        onView(withText("JPY5,341")).check(matches(isDisplayed()))
//        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
//        onView(withId(R.id.tv_total_count)).check(matches(withText("JPY5,341")))
    }
}