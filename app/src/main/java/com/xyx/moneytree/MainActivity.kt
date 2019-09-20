package com.xyx.moneytree

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.xyx.moneytree.data.MTRepo
import com.xyx.moneytree.data.cache.GsonCache

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GsonCache.initWithContext(applicationContext)
        MTRepo.getAccountList()
            .observe(this, Observer {
                println(it)
            })
    }
}
