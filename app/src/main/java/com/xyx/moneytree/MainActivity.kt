package com.xyx.moneytree

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.getmoneytree.moneytreelight.R
import com.xyx.moneytree.data.cache.GsonCache

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GsonCache.initWithContext(applicationContext)

        val navHostFragment = NavHostFragment.create(R.navigation.nav)
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, navHostFragment)
            .setPrimaryNavigationFragment(navHostFragment)
            .commit()
    }
}
