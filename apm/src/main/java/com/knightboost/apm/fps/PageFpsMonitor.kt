package com.knightboost.apm.fps

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.knightboost.apm.ApmSafety
import com.knightboost.apm.fps.jankstats.FrameData
import com.knightboost.apm.fps.jankstats.FrameDataApi24
import com.knightboost.apm.util.ActivityLifeCycleCallbacksAdapter
import com.knightboost.apm.util.ApmExecutors
import java.lang.ref.WeakReference

class PageFpsMonitor(context: Context) : Application.ActivityLifecycleCallbacks, FpsListener {

    private val frameAggregator: FrameAggregator = FrameAggregator(context,
        1000L,ApmExecutors.scheduledExecutorService(),this)

    // 严重丢帧耗时系数 ，以60刷新率为例， 通常为 16.6ms * 25 = 132

    // 严重丢帧耗时系数 ，以60刷新率为例， 通常为 16.6ms * 25 = 132
    //冻帧耗时 (>700ms的帧)
    private val frozenFrameDuration = 700 * 1000000L
    //影响24帧
    private val heavyJankFactor = 24
    // 10帧 以上看做严重丢帧
    private val highJankFactor = 10
    //5~10帧 看做 中等丢帧
    private val middleJankFactor = 5
    // 3帧~5帧看做轻微丢帧
    private val lightJankFactor = 3
    //50 *3

    //50 *3
    private val fpsUploadRate = 0.01f
    private val frameJankUploadRate = 0.2f
    //每个页面活动最多采集的帧数
    private val maxCollectFrameCountPeerPage = 60 * 3
    private var collectedFrameInCurPage = 0

    private val activityLifeCycleCallbacksAdapter: ActivityLifeCycleCallbacksAdapter = object : ActivityLifeCycleCallbacksAdapter() {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            super.onActivityCreated(activity, savedInstanceState)
            collectedFrameInCurPage = 0
        }

        override fun onActivityStarted(activity: Activity) {
            super.onActivityStarted(activity)
            try {
                frameAggregator.add(activity)
            } catch (e: Exception) {
                ApmSafety.handleException("appFpsMonitor", "add_failed", e)
            }
        }

        override fun onActivityStopped(activity: Activity) {
            super.onActivityStopped(activity)
            try {
                frameAggregator.remove(activity)
            } catch (e: Exception) {
                ApmSafety.handleException("appFpsMonitor", "remove_failed", e)
            }
        }
    }

    fun install(app: Application) {

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }


    override fun onFrameAggregate(weakReference: WeakReference<Activity>, refreshRate: Int, frames: List<FrameData>) {
        handleFrameAggregateOnReleaseEnv(weakReference,refreshRate,frames)
    }

    private fun handleFrameAggregateOnReleaseEnv(activityReference: WeakReference<Activity>, refreshRate: Int, frames: List<FrameData>){
        if (frames.isEmpty()){ // 不存在实际帧绘制的数据 不进行上报
            return
        }
        val activity = activityReference.get()
        //todo extract mainFragment
        val frameIntervalNs = (1000_000_000L/refreshRate)

        //统计延迟帧的 的累计延迟时长
        var delayFrameTotalDuration = 0L
        val realFps = frames.size
        var frozenJankFrames = 0
        var heavyJankFrames = 0
        var highJankFrames = 0
        var middleJankFrames = 0
        var lightJankFrames = 0
        var normalFrames = 0

        for (frame in frames) {
            val frameDurationUiNanos = frame.frameDurationUiNanos
            //帧延迟
            if (frameDurationUiNanos>1.5*frameIntervalNs){
                delayFrameTotalDuration +=(frameDurationUiNanos-frameIntervalNs)
            }

            if (frameDurationUiNanos>frozenFrameDuration){
                frozenJankFrames++
            }else if (frameDurationUiNanos>heavyJankFactor *frameIntervalNs){
                heavyJankFrames++
            } else if (frameDurationUiNanos>highJankFactor*frameIntervalNs){
                highJankFrames++
            } else if (frameDurationUiNanos>middleJankFactor * frameIntervalNs){
                middleJankFrames++
            } else if (frameDurationUiNanos>lightJankFactor * frameIntervalNs){
                lightJankFrames++
            }else{
                normalFrames++
            }

            val affectedFrameCount:Int = (delayFrameTotalDuration/frameDurationUiNanos).toInt()

            // 计算fps

            var fps = 0
            if (affectedFrameCount<refreshRate){
                fps = refreshRate - affectedFrameCount
            }
            //TODO 数据上报

        }


    }

    override fun onJankFrame(activityRef: WeakReference<Activity>, refreshRate: Int, detectedWallTime: Long, frameData: FrameData) {
        //todo jank 数据上报
        val costMills = frameData.frameDurationUiNanos/1000_000

        //只上报 api24 以上设备的Jank
        if (frameData !is FrameDataApi24){
            return
        }
        val frameCost = frameData.frameDurationUiNanos + frameData.frameDurationCpuNanos

        if (frameCost> (1000_000_000L/refreshRate)/highJankFactor){ //上报严重丢帧详情数据 提供丢帧数据分析能力

        }
    }

}