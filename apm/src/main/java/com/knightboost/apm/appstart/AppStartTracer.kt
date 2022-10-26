package com.knightboost.apm.appstart

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.knightboost.apm.ApmSdkInitializer
import com.knightboost.apm.common.util.Clock
import com.knightboost.apm.common.util.Timer
import java.lang.ref.WeakReference

/**
 * 应用启动(冷启动)性能追踪
 */
class AppStartTracer(
) : Application.ActivityLifecycleCallbacks {

    class  Launch(
        val trampoline: Boolean,
        val startUptimeMillis: Long,
        val startRealtimeMillis: Long,
        val endUptimeMillis: Long,
    )

    companion object {

        private var instance: AppStartTracer? = null
        @JvmStatic
        fun getInstance(): AppStartTracer {
            val instance_ = instance
            if (instance_ == null) {
                synchronized(this) {
                    val tracer = AppStartTracer()
                    instance = tracer
                    return tracer
                }
            } else {
                return instance_
            }
        }

    }

    private var isRegisteredForLifecycleCallbacks = false;
    private var appContext: Context? = null

    /**
     * The first time onCreate() of any activity is called, the activity is saved as launchActivity
     */
    private var launchActivity: WeakReference<Activity>? = null

    /**
     * The first time onResume() of any activity is called, the activity is saved as appStartActivity
     */

    private var appStartActivity: WeakReference<Activity>? = null

    /**
     * If the time difference between app starts and creation of any Activity is larger than
     * maxLatencyBeforeUiInit, set mTooLateToInitUI to true and we don't send AppStart Trace
     */
    private var isTooLateToInitUI = false;

    private var clock = Clock()

    private var appStartTime: Timer? = null
    private var onCreateTime: Timer? = null
    private var onStartTime: Timer? = null
    private var onResumeTime: Timer? = null

    private var isStartedFromBackground = false;

    @Synchronized
    fun registerActivityLifecycleCallbacks(@NonNull context: Context) {
        if (isRegisteredForLifecycleCallbacks) {
            return
        }
        val appContext = context.applicationContext
        if (appContext is Application) {
            appContext.registerActivityLifecycleCallbacks(this)
            isRegisteredForLifecycleCallbacks = true

        }
    }
    @Synchronized
    fun unRegisterActivityLifecycleCallbacks() {
        if (!isRegisteredForLifecycleCallbacks) {
            return
        }
        appContext?.let {
            it as Application
            it.unregisterActivityLifecycleCallbacks(this)
            isRegisteredForLifecycleCallbacks = false
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (isStartedFromBackground || onCreateTime != null //
        ) {
            return
        }
        launchActivity = WeakReference(activity)
        onCreateTime = clock.getTime()
        //todo tooLateToInitUi detect
    }

    private fun getDurationMicrosSinceAppStart(end: Timer): Long {
        return  end.micros - ApmSdkInitializer.getAppStartTime().micros
    }

    override fun onActivityStarted(activity: Activity) {
        if (onStartTime!=null){
            return
        }
        onStartTime =clock.getTime()
    }

    override fun onActivityResumed(activity: Activity) {
        if (onStartTime!=null){
            return
        }
        onResumeTime =clock.getTime()
        appStartActivity =WeakReference(activity)

        //Log the app start trace in a non-main thread


        //first resumed activity detected, unregister listeners
        if (isRegisteredForLifecycleCallbacks){
            unRegisterActivityLifecycleCallbacks()
        }

    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    private fun logAppStartupTrace(){



    }

}

data class AppStartupTraceConfig(
    /**
     * 闪屏页类名，默认到该页面时 应用启动性能检测的流程结束
     */
    val splashActivityClassName: String,

    /**
     * 手动配置启动流程结束点
     */
    val customStartupEnd: Boolean,
) {

}