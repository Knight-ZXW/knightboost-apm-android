package com.knightboost.apm.blockcanary

import android.app.Application
import android.os.*
import com.knightboost.apm.looper.LooperMonitor
import com.knightboost.apm.stacksampler.StackSampler
import com.knightboost.apm.util.ApmExecutors
import com.knightboost.apm.util.FastTimer

internal object BlockCanaryInternal {

    @Suppress("ObjectPropertyName")
    private var _application: Application? = null

    @Suppress("ObjectPropertyName")
    private var _blockCanaryConfig: BlockCanaryConfig? = null

    var curBlockInfo: BlockInfo? = null

    private val watchDogHandler = HandlerThread("block-canary-handler")

    private val handler: Handler by lazy {
        watchDogHandler.start()
        Handler(watchDogHandler.looper)
    }

//    lateinit var blockInfoRepository: BlockInfoRepository

    private val blockCanaryListeners = mutableListOf<BlockDetectListener>()

    val stackSampler: StackSampler by lazy {
        StackSampler(
            Looper.getMainLooper().thread,
            blockCanaryConfig.stackSampleInterval
        )
    }

    val application: Application
        get() {
            check(_application != null) {
                "BlockCanary not installed"
            }
            return _application!!
        }

    val blockCanaryConfig: BlockCanaryConfig
        get() {
            check(_blockCanaryConfig != null) {
                "BlockCanary not installed"
            }
            return _blockCanaryConfig!!
        }

    fun install(app: Application, config: BlockCanaryConfig) {
        if (this._application != null) {
            //already installed
            return
        }
        this._application = app
        this._blockCanaryConfig = config
        if (config.blockDetectListener!=null){
            this.blockCanaryListeners.add(config.blockDetectListener)
        }
        start()
    }

    private val messageListener = object : LooperMonitor.MessageDispatchListener() {

        override fun onDispatchStart(x: String?) {
            super.onDispatchStart(x)
            val messageInfo = BlockInfo()
            messageInfo.startTime = FastTimer.currentTimeMillis()
            curBlockInfo = messageInfo
            handler.postDelayed(SlowMessageWatchdog(messageInfo), blockCanaryConfig.blockMaxThresholdTime.toLong())
        }

        override fun onDispatchEnd(x: String?) {
            super.onDispatchEnd(x)
            handler.removeCallbacksAndMessages(null)
            val messageInfo = curBlockInfo ?: return
            messageInfo.endTime = FastTimer.currentTimeMillis()
            if (messageInfo.costTime() > blockCanaryConfig.blockThresholdTime) {
                onBlockDetect(messageInfo)
            }
            messageInfo.dispatchFinish = true
        }

    }

    fun start() {
        LooperMonitor.ofMainThread().addListener(messageListener)
        stackSampler.startSampling()
    }

    fun stop() {
        LooperMonitor.ofMainThread().removeListener(messageListener)
        stackSampler.stopSampling()
    }

    fun addBlockDetectListener(blockDetectListener: BlockDetectListener){
        blockCanaryListeners.add(blockDetectListener)
    }

    fun removeBlockDetectListener(blockDetectListener: BlockDetectListener){
        blockCanaryListeners.remove(blockDetectListener)
    }


    private fun onBlockDetect(blockInfo: BlockInfo) {
        blockInfo.sampleInterval = blockCanaryConfig.stackSampleInterval
        ApmExecutors.scheduledExecutorService().execute {
            val dispatchStartTime = blockInfo.startTime
            val stackSamples = stackSampler.getStackSamplesBetweenWallTime(dispatchStartTime, blockInfo.endTime)
            blockInfo.stackTraceSamples.addAll(stackSamples)
            val listeners = blockCanaryListeners.toTypedArray()
            for (listener in listeners) {
                listener.onBlockDetected(blockInfo = blockInfo)
            }
        }

    }


    private class SlowMessageWatchdog(private val blockInfo: BlockInfo) : Runnable {
        override fun run() {
            blockInfo.dispatchFinish = false
            blockInfo.endTime = FastTimer.currentTimeMillis()
            onBlockDetect(blockInfo)
        }
    }
}