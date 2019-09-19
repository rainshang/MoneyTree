package com.xyx.moneytree

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xyx.moneytree.data.cache.GsonCache
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CacheNoInitTest {

    @Test(expected = UninitializedPropertyAccessException::class)
    fun initDirMustCallTest() {
        GsonCache.cacheDir
    }
}