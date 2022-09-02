package com.knightboost.apm.blockcanary

import android.annotation.SuppressLint
import android.app.Application

@SuppressLint("StaticFieldLeak")
object BlockCanary {

    fun install(app: Application, config: BlockCanaryConfig) = BlockCanaryInternal.install(app, config)

    fun start() = BlockCanaryInternal.start()

    fun stop() = BlockCanaryInternal.stop()

    fun addBlockDetectListener(blockDetectListener: BlockDetectListener) =
        BlockCanaryInternal.addBlockDetectListener(blockDetectListener)

    fun removeBlockDetectListener(blockDetectListener: BlockDetectListener) =
        BlockCanaryInternal.removeBlockDetectListener(blockDetectListener)

    fun applicationContext() = BlockCanaryInternal.application

}