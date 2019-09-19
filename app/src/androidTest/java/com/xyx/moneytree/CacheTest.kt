package com.xyx.moneytree

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.xyx.moneytree.data.cache.GsonCache
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class CacheTest {

    @Before
    fun initDir() {
        GsonCache.cacheDir =
            InstrumentationRegistry.getInstrumentation().targetContext.cacheDir.absolutePath + File.separator
    }

    @Test
    fun getAccountsWhenNoCacheTest() {
        assertNull(GsonCache.getAccounts())
    }

    @Test
    fun saveAccountsTest() {
        println(ResourceUtil.getAccounts())
    }
}