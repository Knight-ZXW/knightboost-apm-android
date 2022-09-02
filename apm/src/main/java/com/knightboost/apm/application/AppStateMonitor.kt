package com.knightboost.apm.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.knightboost.apm.common.util.Clock
import com.knightboost.apm.common.util.Timer
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object AppStateMonitor : Application.ActivityLifecycleCallbacks {

    private val activityToResumedMap: WeakHashMap<Activity, Boolean> = WeakHashMap()

    /* Count for TRACE_STARTED_NOT_STOPPED */
    private val tsnsCount: AtomicInteger = AtomicInteger(0)

    private val clock = Clock()

    private var isRegisteredForLifecycleCallbacks = false

    private var resumeTime: Timer? = null
    private var stopTime: Timer? = null

    private var isColdStart = true

    private val appColdStartSubscribers = mutableSetOf<AppColdStartCallback>()

    fun registerAppColdStartupCallback(appColdStartCallback: AppColdStartCallback){
        synchronized(appColdStartSubscribers){
            appColdStartSubscribers.add(appColdStartCallback)
        }
    }

    private fun notifyAppColdStartup(){
        synchronized(appColdStartSubscribers) {
            val i: Iterator<AppColdStartCallback> = appColdStartSubscribers.iterator()
            while (i.hasNext()) {
                val callback = i.next()
                if (callback != null) {
                    callback.onAppColdStart()
                }
            }
        }
    }



    @Synchronized
    fun registerActivityLifecycleCallbacks(context: Context) {
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
    fun unregisterActivityLifecycleCallbacks(context: Context) {
        if (!isRegisteredForLifecycleCallbacks) {
            return;
        }
        val appContext = context.applicationContext
        if (appContext is Application) {
            appContext.unregisterActivityLifecycleCallbacks(this)
            isRegisteredForLifecycleCallbacks = false
        }
    }

    fun isColdStart():Boolean{
        return isColdStart
    }

    //todo
    fun isForeground():Boolean{
        return  false
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        // cases:
        // 1. At app startup, first activity comes to foreground.
        // 2. app switch from background to foreground.
        // 3. app already in foreground, current activity is replaced by another activity, or the
        // current activity was paused then resumed without onStop, for example by an AlertDialog
        if (activityToResumedMap.isEmpty()) {
            resumeTime = clock.getTime()
            activityToResumedMap[activity] = true
            if (isColdStart) {
                // case 1:app startup
                //todo notify app state change
                // send app coldStartup Update

                isColdStart = false
            } else {
                // case 2: app switch from background to foreground.
                //todo updateAppState foreground
            }
        } else {
            // case 3: app already in foreground, current activity is replaced by another activity, or the
            // current activity was paused then resumed without onStop, for example by an AlertDialog
            activityToResumedMap.put(activity,true)
        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        if (activityToResumedMap.containsKey(activity)){
            activityToResumedMap.remove(activity)
            if (activityToResumedMap.isEmpty()){
                stopTime = clock.getTime()
                //todo update appState background
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

}

public interface AppColdStartCallback{
    fun onAppColdStart()
}