package com.xyx.moneytree

import ResourceUtil
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.xyx.moneytree.data.api.ResponseListWrapper
import com.xyx.moneytree.data.cache.GsonCache
import com.xyx.moneytree.data.vo.Account
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CacheTest {

    @Before
    fun initDir() {
        GsonCache.initWithContext(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun getAccountsWhenNoCacheTest() {
        Assert.assertNull(GsonCache.getAccounts())
    }

    @Test
    fun saveAccountsTest() {
        val accountsWrapper = Gson().fromJson<ResponseListWrapper<Account>>(
            ResourceUtil.getAccounts(),
            GsonCache.getResponseListWrapperType<Account>()
        )
        // Unit test should not write file system
        GsonCache.saveAccounts(accountsWrapper)
    }
}