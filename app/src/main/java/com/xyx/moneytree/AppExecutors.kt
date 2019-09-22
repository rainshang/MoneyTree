package com.xyx.moneytree

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object AppExecutors {

    val diskIO: Executor
    val networkIO: Executor
    val computing: Executor
    val mainThread: Executor

    init {
        diskIO = Executors.newSingleThreadExecutor()
        networkIO = Executors.newFixedThreadPool(3)
        computing = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1)
        mainThread = object : Executor {
            val mainThreadHandler = Handler(Looper.getMainLooper())
            override fun execute(command: Runnable) {
                mainThreadHandler.post(command)
            }
        }
    }
}