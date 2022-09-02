package com.knightboost.apm.util

import android.os.Handler
import android.os.HandlerThread
import java.util.concurrent.*

object ApmExecutors {
    private val bgHandlerThread:HandlerThread
    private val scheduledExecutorService:ScheduledExecutorService
    init {
         bgHandlerThread = HandlerThread("apm-background-ht")
        bgHandlerThread.start()
        scheduledExecutorService = Executors.newScheduledThreadPool(4,object :ThreadFactory{
            override fun newThread(r: Runnable?): Thread {
                val t = Thread(r)
                t.priority = Thread.MAX_PRIORITY
                t.name = "apm-scheduledExecutors"
                return t
            }
        })
    }

    fun createBackgroundHandler(): Handler {
        return Handler(bgHandlerThread.looper)
    }

    fun scheduledExecutorService(): ScheduledExecutorService {
        return scheduledExecutorService
    }


}