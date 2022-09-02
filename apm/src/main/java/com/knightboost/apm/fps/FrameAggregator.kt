package com.knightboost.apm.fps

import android.app.Activity
import android.content.Context
import androidx.annotation.MainThread
import com.knightboost.apm.ApmSafety
import com.knightboost.apm.fps.jankstats.FrameData
import com.knightboost.apm.fps.jankstats.JankStats
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.*

/**
 * 监控应用帧信息
 */
class FrameAggregator(
    private val context: Context,
    private val aggregateIntervalMs:Long,
    private val executor: ScheduledExecutorService,
    private val fpsListener: FpsListener
) {

    private val activities = mutableListOf<Activity>()
    private val frameTracerMap = mutableMapOf<Activity, JankStats>()
    private val frameAggregatorMap = mutableMapOf<Activity, ScheduledFuture<*>>()

    @MainThread
    @Synchronized
    fun add(activity: Activity) {
        activities.add(activity)
        if (activity.window != null) {
            val listener = FramePeriodAggregateListener(activity)
            val jankStats = JankStats.createAndTrack(activity.window, executor, listener)
            val cancelFuture = executor.scheduleAtFixedRate({
                listener.notifyAndReset()
            }, 0, aggregateIntervalMs, TimeUnit.MILLISECONDS);
            frameAggregatorMap[activity] = cancelFuture
            frameTracerMap[activity] = jankStats;
            jankStats.isTrackingEnabled = true
        }

    }

    @MainThread
    @Synchronized
    fun remove(activity: Activity) {
        val added = activities.remove(activity)
        if (added) {
            frameTracerMap[activity]?.isTrackingEnabled = false
            frameTracerMap.remove(activity)
            frameAggregatorMap[activity]?.cancel(true)
            frameAggregatorMap.remove(activity)
        }

    }

    @Synchronized
    fun stop() {
        for (activity in activities) {
            remove(activity)
        }
    }

    inner class FramePeriodAggregateListener(activity: Activity) : JankStats.OnFrameListener {
        private val activityRef: WeakReference<Activity> = WeakReference(activity)
        private var frames: MutableList<FrameData> = Collections.synchronizedList(mutableListOf<FrameData>())
        //统计大于16ms的所有帧的信息
        var refreshRate = 60;

        init {
            try {
                refreshRate = activity.window.windowManager.defaultDisplay.refreshRate.toInt()
            } catch (e: Exception) {
                ApmSafety.handleException("FrameAggregator","notifyAndReset",e)
            }
        }

        fun notifyAndReset() {
            val clone = frames.toList()
            this.frames.clear()
            executor.execute {
                try {
                    //todo add state
                    fpsListener.onFrameAggregate(activityRef, refreshRate, clone)
                } catch (e: Exception) {
                    ApmSafety.handleException("FrameAggregator","notifyAndReset",e)
                }
            }
        }

        override fun onFrame(frameData: FrameData) {
            frames.add(frameData)
            if (frameData.isJank) {
                val wallTime = System.currentTimeMillis()
                executor.execute {
                    try {
                        fpsListener.onJankFrame(activityRef, refreshRate,wallTime, frameData)
                    } catch (e: Exception) {
                        ApmSafety.handleException("FrameAggregator","onFrame",e)
                    }
                }
            }
        }

    }
}