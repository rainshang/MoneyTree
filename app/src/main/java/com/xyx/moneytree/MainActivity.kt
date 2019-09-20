package com.xyx.moneytree

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xyx.moneytree.data.cache.GsonCache
import com.xyx.moneytree.fragment.accounts.AccountsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GsonCache.initWithContext(applicationContext)

        supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, AccountsFragment())
            .commit()
    }
}