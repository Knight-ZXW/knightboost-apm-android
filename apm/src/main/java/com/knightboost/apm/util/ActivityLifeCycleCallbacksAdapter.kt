package com.knightboost.apm.util

import android.app.Application.ActivityLifecycleCallbacks
import android.app.Activity
import android.os.Bundle

open class ActivityLifeCycleCallbacksAdapter : ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // Override it if needed.
    }

    override fun onActivityStarted(activity: Activity) {
        // Override it if needed.
    }

    override fun onActivityResumed(activity: Activity) {
        // Override it if needed.
    }

    override fun onActivityPaused(activity: Activity) {
        // Override it if needed.
    }

    override fun onActivityStopped(activity: Activity) {
        // Override it if needed.
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // Override it if needed.
    }

    override fun onActivityDestroyed(activity: Activity) {
        // Override it if needed.
    }
}